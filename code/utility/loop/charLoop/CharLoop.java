public class CharLoop {

	public CharLoop(char starChar, char endChar) {
		char i;
		char j;

		for (i = starChar; i <= endChar; i++) {
			for (j = starChar; j <= endChar; j++) {
				System.out.print(i);
			}
			System.out.println();
		}
	}
	
}
