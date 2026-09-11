package nofSearch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.google.gson.Gson;

public class NofSearch {

	private static final DateTimeFormatter OUTPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssSSS");

	public static void main(String[] args) {

//		if (args.length != 1) {
//			System.err.println("Usage:");
//			System.err.println("  java nofSearch.NofSearch search.json");
//			System.exit(1);
//		}

		try {

//			SearchRequest request = readJson(args[0]);

			SearchRequest request = readJson("C:\\00\\search.json");

			validateRequest(request);

			SearchResult result = search(request);

			System.out.println();
			System.out.println("========================================");
			System.out.println("Search completed");
			System.out.println("========================================");
			System.out.println("File       : " + request.file);
			System.out.println("Search name: " + request.searchName);
			System.out.println("Pattern    : " + request.pattern);
			System.out.println("Type       : " + request.searchType);
			System.out.println("Matches    : " + result.matches);
			System.out.println("Output     : " + result.outputFile);
			System.out.println("========================================");

		} catch (Exception e) {

			System.err.println("ERROR: " + e.getMessage());
			System.exit(2);
		}
	}

	/**
	 * Read the JSON search request using Gson.
	 */
	private static SearchRequest readJson(String jsonFile) throws IOException {

		Gson gson = new Gson();

		Path jsonPath = Paths.get(jsonFile);

		try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8)) {

			return gson.fromJson(reader, SearchRequest.class);
		}
	}

	/**
	 * Validate the JSON data.
	 */
	private static void validateRequest(SearchRequest request) {

		if (request == null) {
			throw new IllegalArgumentException("JSON is empty.");
		}

		if (isBlank(request.file)) {
			throw new IllegalArgumentException("'file' is missing.");
		}

		if (isBlank(request.searchName)) {
			throw new IllegalArgumentException("'searchName' is missing.");
		}

		if (isBlank(request.pattern)) {
			throw new IllegalArgumentException("'pattern' is missing.");
		}

		if (isBlank(request.searchType)) {
			throw new IllegalArgumentException("'searchType' is missing.");
		}

		Path file = Paths.get(request.file);

		if (!Files.exists(file)) {
			throw new IllegalArgumentException("File does not exist: " + request.file);
		}

		if (!Files.isRegularFile(file)) {
			throw new IllegalArgumentException("Path is not a file: " + request.file);
		}

		switch (request.searchType) {

		case "search_all":
			break;

		case "search_x..y":

			if (request.x == null || request.y == null) {
				throw new IllegalArgumentException("search_x..y requires x and y.");
			}

			if (request.x < 1 || request.y < 1) {
				throw new IllegalArgumentException("x and y must be >= 1.");
			}

			break;

		case "search_x..count(y)":

			if (request.x == null) {
				throw new IllegalArgumentException("search_x..count(y) requires x.");
			}

			if (request.x < 1) {
				throw new IllegalArgumentException("x must be >= 1.");
			}

			break;

		case "search_x..eol":

			if (request.x == null) {
				throw new IllegalArgumentException("search_x..eol requires x.");
			}

			if (request.x < 1) {
				throw new IllegalArgumentException("x must be >= 1.");
			}

			break;

		default:

			throw new IllegalArgumentException("Unknown searchType: " + request.searchType);
		}
	}

	/**
	 * Search the file.
	 */
	private static SearchResult search(SearchRequest request) throws IOException {

		Path inputFile = Paths.get(request.file);

		String outputFileName = LocalDateTime.now().format(OUTPUT_DATE_FORMAT) + "_"
				+ sanitizeFileName(request.searchName) + ".txt";

		Path outputFile = inputFile.toAbsolutePath().getParent().resolve(outputFileName);

		long matchCount = 0;
		long lineNumber = 0;
		String previousLine = null;

		try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8);

				BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8,
						StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {

			String currentLine;

			while ((currentLine = reader.readLine()) != null) {

				lineNumber++;

				SearchMatch match = findMatch(currentLine, request);

				if (match != null) {

					matchCount++;

					String nextLine = reader.readLine();

					writeResult(writer, lineNumber, previousLine, currentLine, nextLine, match);

					/*
					 * We consumed the next line to provide the context after the matching line.
					 *
					 * Therefore: - advance the line number - next iteration needs to know the line
					 * after the consumed line
					 */

					if (nextLine != null) {

						lineNumber++;

						previousLine = currentLine;

						/*
						 * We cannot simply continue here because nextLine itself could contain another
						 * match.
						 */

						MatchProcessing processing = processConsumedLine(reader, writer, request, lineNumber,
								previousLine, nextLine);

						matchCount += processing.additionalMatches;

						lineNumber = processing.lastLineNumber;

						previousLine = processing.previousLine;

					} else {

						previousLine = currentLine;
					}

				} else {

					previousLine = currentLine;
				}
			}
		}

		return new SearchResult(matchCount, outputFile.toString());
	}

	/**
	 * Process a line that was consumed while obtaining the "one line after"
	 * context.
	 */
	private static MatchProcessing processConsumedLine(BufferedReader reader, BufferedWriter writer,
			SearchRequest request, long currentLineNumber, String previousLine, String currentLine) throws IOException {

		long additionalMatches = 0;

		long lineNumber = currentLineNumber;

		String line = currentLine;

		String previous = previousLine;

		while (true) {

			SearchMatch match = findMatch(line, request);

			if (match != null) {

				additionalMatches++;

				String nextLine = reader.readLine();

				writeResult(writer, lineNumber, previous, line, nextLine, match);

				if (nextLine == null) {

					previous = line;

					break;
				}

				previous = line;

				line = nextLine;

				lineNumber++;

			} else {

				previous = line;

				break;
			}
		}

		return new MatchProcessing(additionalMatches, lineNumber, previous);
	}

	/**
	 * Find the pattern inside the required range of the line.
	 *
	 * Returns the actual found text and its character position.
	 */
	private static SearchMatch findMatch(String line, SearchRequest request) {

		if (line == null || line.isEmpty()) {
			return null;
		}

		String pattern = request.pattern;

		int from;
		int to;

		switch (request.searchType) {

		case "search_all":

			from = 0;
			to = line.length();

			break;

		case "search_x..y":

			from = request.x - 1;
			to = request.y;

			if (from >= line.length()) {
				return null;
			}

			if (to > line.length()) {
				to = line.length();
			}

			if (from >= to) {
				return null;
			}

			break;

		case "search_x..count(y)":

			from = request.x - 1;

			if (from >= line.length()) {
				return null;
			}

			to = Math.min(line.length(), from + pattern.length());

			break;

		case "search_x..eol":

			from = request.x - 1;

			if (from >= line.length()) {
				return null;
			}

			to = line.length();

			break;

		default:

			return null;
		}

		String searchArea = line.substring(from, to);

		int relativeIndex = searchArea.toLowerCase(Locale.ROOT).indexOf(pattern.toLowerCase(Locale.ROOT));

		if (relativeIndex < 0) {
			return null;
		}

		int absoluteIndex = from + relativeIndex;

		/*
		 * Make sure the pattern fits inside the line.
		 */
		if (absoluteIndex + pattern.length() > line.length()) {
			return null;
		}

		String foundText = line.substring(absoluteIndex, absoluteIndex + pattern.length());

		return new SearchMatch(foundText, absoluteIndex + 1);
	}

	/**
	 * Write one result.
	 */
	private static void writeResult(BufferedWriter writer, long lineNumber, String previousLine, String currentLine,
			String nextLine, SearchMatch match) throws IOException {

		writer.write("========================================");
		writer.newLine();

		writer.write("LINE NUMBER : " + lineNumber);
		writer.newLine();

		writer.write("FOUND AT    : character " + match.position);
		writer.newLine();

		writer.write("FOUND TEXT  : " + match.foundText);
		writer.newLine();

		writer.write("----------------------------------------");
		writer.newLine();

		writer.write("BEFORE      : " + (previousLine == null ? "<START OF FILE>" : previousLine));
		writer.newLine();

		writer.write("RESULT      : " + currentLine);
		writer.newLine();

		writer.write("AFTER       : " + (nextLine == null ? "<END OF FILE>" : nextLine));
		writer.newLine();

		writer.newLine();
	}

	/**
	 * Make searchName safe for use in a filename.
	 */
	private static String sanitizeFileName(String value) {

		return value.replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll("\\s+", "_");
	}

	private static boolean isBlank(String value) {

		return value == null || value.trim().isEmpty();
	}

	// ---------------------------------------------------------
	// JSON data
	// ---------------------------------------------------------

	public static class SearchRequest {

		public String file;

		public String searchName;

		public String pattern;

		public String searchType;

		public Integer x;

		public Integer y;
	}

	// ---------------------------------------------------------
	// Search result
	// ---------------------------------------------------------

	private static class SearchResult {

		final long matches;

		final String outputFile;

		SearchResult(long matches, String outputFile) {

			this.matches = matches;

			this.outputFile = outputFile;
		}
	}

	// ---------------------------------------------------------
	// Match
	// ---------------------------------------------------------

	private static class SearchMatch {

		final String foundText;

		final int position;

		SearchMatch(String foundText, int position) {

			this.foundText = foundText;

			this.position = position;
		}
	}

	// ---------------------------------------------------------
	// Internal processing
	// ---------------------------------------------------------

	private static class MatchProcessing {

		final long additionalMatches;

		final long lastLineNumber;

		final String previousLine;

		MatchProcessing(long additionalMatches, long lastLineNumber, String previousLine) {

			this.additionalMatches = additionalMatches;

			this.lastLineNumber = lastLineNumber;

			this.previousLine = previousLine;
		}
	}
}
