
/**
 * @author      Lize Chen <lizechen @ email.arizona.edu.com>
 * @assignment  Programming Assignment 2
 * @course      CSc 335; Fall 2019
 * @purpose     This program reads a file and selects a random quote to be the 
 * puzzle. Generate a randomly shuffled List to encode the chosen quotation. 
 * Have an additional HashMap for the user's attempt to solve the puzzle. As 
 * the user enters their guesses, add the mapping to their HashMap and show 
 * them their progress by using the HashMap to replace the encrypted 
 * characters with their guess ones. A user is always free to change a letter
 * to something else. The game is over when the user's guesses turn the 
 * encrypted quote back into the original quotation. 
 */



import javafx.application.Application;

/**
 * @author   Lize Chen [Project 4]
 * This program reads a file and selects a random quote to be the 
 * puzzle. Generate a randomly shuffled List to encode the chosen quotation. 
 * Have an additional HashMap for the user's attempt to solve the puzzle. As 
 * the user enters their guesses, add the mapping to their HashMap and show 
 * them their progress by using the HashMap to replace the encrypted 
 * characters with their guess ones. A user is always free to change a letter
 * to something else. The game is over when the user's guesses turn the 
 * encrypted quote back into the original quotation. 
 */
public class cryptogram {
		
	public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("-text")) {
			@SuppressWarnings("unused")
			CryptogramTextView tv = new CryptogramTextView();
			System.exit(0);
		}else if (args.length == 1 && args[0].equals("-window")) {
			Application.launch(CryptogramGUIView.class, args);
			System.exit(0);
		}else if (args.length == 0 ) {
			Application.launch(CryptogramGUIView.class, args);
			System.exit(0);
		}
	}
}
	
	