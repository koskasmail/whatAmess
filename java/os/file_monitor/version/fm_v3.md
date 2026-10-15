
# file monitor

* version 3

###### fm.java

```java
package os;


import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FM01 implements Runnable {

	private final WatchService watchService;

	private final Map<WatchKey, Path> keyMap = new HashMap<>();

	// Stores the previous state of each file
	private final Map<Path, FileInfo> fileStates = new HashMap<>();
	private Path rootPath;

	public FM01(Path rootPath) throws IOException {
		this.rootPath = rootPath.toAbsolutePath().normalize();
		this.watchService = FileSystems.getDefault().newWatchService();

		// Save the initial state before monitoring begins
		scanInitialFiles(this.rootPath);

		registerAllSubFolders(this.rootPath);
	}

	private static class FileInfo {
		long size;
		Instant lastModified;

		FileInfo(Path file) throws IOException {
			BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);

			this.size = attrs.size();
			this.lastModified = attrs.lastModifiedTime().toInstant();
		}
	}

	private void scanInitialFiles(Path start) throws IOException {
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

				if (attrs.isRegularFile()) {
					fileStates.put(file.toAbsolutePath().normalize(), createInfo(attrs));
				}

				return FileVisitResult.CONTINUE;
			}

			private FileInfo createInfo(BasicFileAttributes attrs) {
				FileInfo info = null;
				try {
					info = new FileInfoData(attrs.size(), attrs.lastModifiedTime().toInstant());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return info;
			}
		});

		System.out.println("Initial file state saved.");
	}

	// Helper constructor is defined below through FileInfoData
	private static class FileInfoData extends FileInfo {
		FileInfoData(long size, Instant modified) throws IOException {
			super(Paths.get("."));
			this.size = size;
			this.lastModified = modified;
		}
	}

	private void registerAllSubFolders(Path start) throws IOException {
		try (Stream<Path> paths = Files.walk(start)) {
			paths.filter(Files::isDirectory).forEach(path -> {
				try {
					WatchKey key = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
							StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

					keyMap.put(key, path);
					System.out.println("Watching folder: " + path);

				} catch (IOException e) {
					System.err.println("Cannot watch folder: " + path + ": " + e.getMessage());
				}
			});
		}
	}

	@Override
	public void run() {
		System.out.println("File monitor started for: " + rootPath);

		while (!Thread.currentThread().isInterrupted()) {
			WatchKey key;

			try {
				key = watchService.take();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}

			Path dir = keyMap.get(key);

			if (dir != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
						System.out.println("Some file events may have been lost.");
						continue;
					}

					Path file = dir.resolve((Path) event.context()).toAbsolutePath().normalize();

					try {
						if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {

							if (Files.isDirectory(file)) {
								registerAllSubFolders(file);
							} else {
								handleCreate(file);
							}

						} else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {

							if (Files.isRegularFile(file)) {
								handleModify(file);
							}

						} else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {

							handleDelete(file);
						}

					} catch (IOException e) {
						System.err.println("Could not inspect " + file + ": " + e.getMessage());
					}
				}
			}

			if (!key.reset()) {
				keyMap.remove(key);
				if (keyMap.isEmpty()) {
					break;
				}
			}
		}

		try {
			watchService.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("File monitor stopped.");
	}

	private void handleCreate(Path file) throws IOException {
		// A newly created file may still be empty or being written.
		FileInfo current = new FileInfo(file);
		fileStates.put(file, current);

		System.out.println("\n=== NEW FILE ===");
		System.out.println("File: " + file);
		System.out.println("Size: " + current.size + " bytes");
		System.out.println("Modified: " + current.lastModified);
	}

	private void handleModify(Path file) throws IOException {
		FileInfo current = new FileInfo(file);
		FileInfo previous = fileStates.get(file);

		if (previous == null) {
			fileStates.put(file, current);
			System.out.println("\n=== FILE BASELINE RECORDED ===");
			printInfo("Current", current);
			return;
		}

		if (previous.size != current.size || !previous.lastModified.equals(current.lastModified)) {

			System.out.println("\n=== FILE CHANGED ===");
			System.out.println("File: " + file);

			printInfo("BEFORE", previous);
			printInfo("AFTER_", current);

			System.out.println("Size difference: " + (current.size - previous.size) + " bytes");

			fileStates.put(file, current);
		}
	}

	private void handleDelete(Path file) {
		FileInfo previous = fileStates.remove(file);

		System.out.println("\n=== FILE DELETED ===");
		System.out.println("File: " + file);

		if (previous != null) {
			printInfo("LAST KNOWN STATE", previous);
		}
	}

	private void printInfo(String label, FileInfo info) {
//		System.out.println("--- " + label + " ---");
//		System.out.println("Size (bytes): " + info.size);
//		System.out.printf("Size (KB): %.2f%n", info.size / 1024.0);
//		System.out.printf("Size (MB): %.2f%n", info.size / (1024.0 * 1024));
//		System.out.printf("Size (GB): %.4f%n", info.size / (1024.0 * 1024 * 1024));
//		System.out.println("Last modified: " + info.lastModified);
		System.out.printf("[ " + label + " ] " +  "Size (KB): %.2f%n", info.size / 1024.0);
	}

	public static void main(String[] args) throws IOException {
		

		Path path = Paths.get("\\\\mami.gov.il\\shares\\ApplicFiles\\MMILogFiles\\Prod\\RmiDocs\\FileNetWebApi\\");
//		Path path = Paths.get("C:\\json");
//		private final Path rootPath = "C:\\json";
//		
//		if (args.length == 0) {
//			System.out.println("Usage: java monitor.FileMonitor <folder-path>");
//			return;
//		}

		FM01 monitor = new FM01(path);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Stopping monitor...");
			try {
				monitor.watchService.close();
			} catch (IOException ignored) {
			}
		}));

		monitor.run();
	}
}
```

-----

###### xx

```java
```

-----

###### xx

```java
```

-----
