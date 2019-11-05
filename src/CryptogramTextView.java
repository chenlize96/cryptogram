import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;



public class CryptogramTextView {


	private   List<Character> original;
	private    Map<Character, Character> encryption;
	private   List<Character> encoded;
	private  List<Character> decoded;
	private   Map<Character, Character> record;

	public CryptogramTextView() {
		startUp(null, decoded);
		boolean ifBreak = false;
		Scanner reader = null;
		while (true) {
			reader = new Scanner(System.in);
			System.out.print("Enter a command (help to see commands): ");
			String strCommand = reader.nextLine().toUpperCase().trim();
			ifBreak = process(strCommand);
			if (ifBreak) break;
			print(encoded, decoded, record);
		}
		print(encoded, decoded, record);
		System.out.println();
		System.out.println("You got it!");
		reader.close();
	}

	/**
	 * Read the command and execute it,
	 * and check if the player succeeds in guessing
	 * 
	 * @param  strCommand the input command in uppercase
	 * @return true       if the player got the correct answer, otherwise false
	 */
	public  boolean process(String strCommand) {
		String[] command = strCommand.split("\\s+");
		int index = readCommand(command, record);
		execute(index, encryption, record, encoded);
		decoded = decode(record, encoded, decoded);
		System.out.println();
		if (original.equals(decoded)) {
			return true; // break the loop if succeed in completing
		}
		return false;
	}

	/**
	 * Display one correct mapping that has not yet been guessed
	 *
	 * @param encryption  maps Characters to Characters and for each letter
	 * @param record      store the player decryption mappings
	 * @param encoded     the encoded row
	 * @return record     the updated player solution
	 */
	public   Map<Character, Character> 
	getHint(Map<Character, Character> encryption,
			Map<Character, Character> record, List<Character> encoded) {
		for (char key: encryption.keySet()) {
			if (encoded.contains(encryption.get(key)) && 
					record.get(encryption.get(key)) == null ) {
				record.replace(encryption.get(key), key);
				break;
			}else if (encoded.contains(encryption.get(key)) && 
					key != record.get(encryption.get(key))){
				record.replace(encryption.get(key), key);
				break;
			}
		}
		return record;
	}

	/**
	 * Display the letter frequencies in the encrypted quotation 
	 *
	 * @param encoded     the encoded row
	 * @return freq       mapping the letter to its occurence
	 */
	public  Map<Character, Integer> getFreq(List<Character> encoded) {
		Map<Character, Integer> freq = new HashMap<>();
		for (char i = 65; i < 91; i++) 
			freq.put(i, 0); 
		for (char c: encoded) {
			if (checkChar(String.valueOf(c)) && freq.containsKey(c)) {
				freq.replace(c, freq.get(c) + 1) ;
			}
		}
		int i = 0;
		for (char key: freq.keySet()) {
			System.out.print(key + ": " + freq.get(key) + " ");
			i += 1;
			// print 7 per line
			if (i == 7) {
				System.out.println();
				i = 0;
			}
		}
		System.out.println();
		return freq; // for assertTest
	}

	/**
	 * Execute the function corresponding to the index
	 *
	 * @param index       the input instruction
	 * @param encryption  maps Characters to Characters and for each letter
	 * @param record      store the player decryption mappings
	 * @param encoded     the encoded row
	 */	
	public   void execute(int index, Map<Character, Character> encryption,
			Map<Character, Character> record, List<Character> encoded) {
		if (index == 1) {
			getFreq(encoded);
		}else if (index == 2) {
			getHint(encryption, record, encoded);
		}else if (index == 3) {
			System.exit(0);
		}else if (index == 4) {
			System.out.println("a. replace X by Y 每 replace letter X by "
					+ "letter Y in our attempted solution");
			System.out.println("   X = Y 每 a shortcut for this same command");
			System.out.println("b. freq 每 Display the letter frequencies in"
					+ " the encrypted quotation");
			System.out.println("c. hint 每 display one correct mapping that "
					+ "has not yet been guessed");
			System.out.println("d. exit 每 Ends the game early");
			System.out.println("e. help 每 List these commands");
		}else if (index == -1) {
			System.out.println("Invalid input!");
		}


	}

	/**
	 * Create decoded list which has the same space and punctuation as encoded
	 *
	 * @param encodedList    the encoded row
	 * @param decodedList    new list of character
	 * @return decoded       the decoded row for test
	 */	
	public  List<Character> startUp(List<Character> encodedList, 
			List<Character> decodedList){
		original = selectRandomRow(openFile("quotes.txt"));
		encryption = getEncryptionKey();
		if (encodedList == null) 
			encodedList = encode(encryption, original);	
		for (String word: display(encodedList))
			System.out.println(word);
		record = new HashMap<>();
		for (char i = 65; i < 91; i++) 
			record.put(i, null); // initialize a mapping only has keys
		decodedList = new ArrayList<>();
		for (int i = 0; i < encodedList.size(); i++) {
			char word = encodedList.get(i);
			if (word >= 65 && word <= 90) {
				decodedList.add((char) 32); // add space
			}else {
				decodedList.add(word);
			}
		}
		encoded = encodedList;
		decoded = decodedList;
		return decodedList; // for test
	}

	/**
	 * Decode the encoded row according to player mappings
	 *
	 * @param record     the player decryption mappings
	 * @param encoded    the encoded row
	 * @param decoded    the user solution
	 * @return decoded   the updated user solution
	 */	
	public  List<Character> decode(Map<Character, Character> record, 
			List<Character> encoded, List<Character> decoded){
		for (int i = 0; i < encoded.size(); i++) {
			if (!record.containsKey(encoded.get(i))) {
				continue;
			}
			if (record.get(encoded.get(i)) == null) {
				// null in mapping means space in row
				decoded.set(i, (char) 32);
				continue;
			}
			decoded.set(i, record.get(encoded.get(i)));
		}
		return decoded;
	}

	/**
	 * Check if the input string is a single word and if it is alphabetic
	 *
	 * @param  aChar    the input string
	 * @return true     if the input is valid, otherwise false
	 */	
	private   boolean checkChar(String aChar) {
		char first = aChar.charAt(0);
		if (aChar.length() != 1 || first < 65 || first > 90) 
			return false;
		else
			return true;
	}

	/**
	 * Read command, then give the index
	 * Something change in this function, if the command is "X =" OR 
	 * "REPLACE X BY", then the guess of X will be erased
	 *
	 * @param  command   the input string
	 * @param  record    the player decryption mappings
	 * @return index     different indices mean different instructions,
	 *                   if there is an error input, return -1
	 */	
	public   int readCommand(String[] command, 
			Map<Character, Character> record) {
		if (command[0].equals("FREQ"))
			return 1;
		if (command[0].equals("HINT"))
			return 2;
		if (command[0].equals("EXIT"))
			return 3;
		if (command[0].equals("HELP"))
			return 4;
		if (command.length == 4 && command[0].equals("REPLACE")
				&& checkChar(command[1]) && checkChar(command[3])) {
			record.replace(command[1].charAt(0), command[3].charAt(0));
		}else if(command.length == 3 && command[0].equals("REPLACE")
				&& checkChar(command[1])){
			record.replace(command[1].charAt(0), null);
		}else if (command.length == 3 && command[1].equals("=")
				&& checkChar(command[0]) && checkChar(command[2])){
			record.replace(command[0].charAt(0), command[2].charAt(0));
		}else if (command.length == 2 && command[1].equals("=")
				&& checkChar(command[0])) {
			record.replace(command[0].charAt(0), null);
		}else {
			return -1; // invalid input
		}
		return 0;
	}

	/**
	 * Break each at whitespace or punctuation so that each part does not 
	 * exceed 80 characters
	 *
	 * @param  encoded   the encoded row
	 * @return words     the length of strings in this list do not exceed 80
	 */	
	public   List<String> display(List<Character> encoded) {
		int j = 0; // it should not exceed 80
		int k = 0; // renew when meet a space
		String str = "";
		List<String> words = new ArrayList<>();
		for (int i = 0; i < encoded.size(); i++) {
			str += encoded.get(i);
			j++; k++;
			// meet a space
			if (encoded.get(i) == ' ') 
				k = 0;
			if (j == 80 && i + 1 < encoded.size() && 
					encoded.get(i + 1) != ' '){
				str = str.substring(0, str.length() - k);
				words.add(str);
				i = i - k;
				str = ""; j = 0; k = 0;
			}else if (j <= 80 && i + 1 == encoded.size()) {
				words.add(str);
			}else if (j == 80 && i + 1 < encoded.size() && 
					encoded.get(i + 1) == ' ') {
				words.add(str);
				str = ""; j = 0; k = 0; i++;
			}
		}
		return words;
	}

	/**
	 * Print out the user solution and encoded row
	 *
	 * @param encoded    the encoded row
	 * @param record     the player decryption mappings
	 * @param decoded    the user solution
	 * @return encodedDisplay   for test
	 */	
	public  List<String> print(List<Character> encoded, 
			List<Character> decoded, Map<Character, Character> record) {
		List<String> encodedDisplay = display(encoded);
		// convert list of characters to list of strings with proper length
		for (int i = 0; i < encodedDisplay.size(); i++) {
			String temp = encodedDisplay.get(i);
			for (int j = 0; j < temp.length(); j++) {
				if (!record.containsKey(temp.charAt(j))) {
					System.out.print(temp.charAt(j));
					continue;
				}
				if (record.get(temp.charAt(j)) == null) {
					System.out.print(" "); // print space
				}else {
					System.out.print(record.get(temp.charAt(j)));
				}
			}
			System.out.println();
			System.out.println(encodedDisplay.get(i));
		}
		System.out.println();
		return encodedDisplay; // for assertTest
	}

	/**
	 * Encode the original row according to the encryption key
	 *
	 * @param  encryption  the encryption key
	 * @param  original    the row selected
	 * @return encoded     encoded row
	 */	
	public  List<Character> encode(Map<Character, Character> encryption, 
			List<Character> original) {
		List<Character> encoded = new ArrayList<>();
		for (int i = 0; i < original.size(); i++) {
			char word = original.get(i);
			if (encryption.containsKey(word)){
				encoded.add(encryption.get(word));
			}else {
				// keep the same if this character is not in the map
				encoded.add(word);
			}
		}
		return encoded;
	}

	/**
	 * Store the random encryption key
	 *
	 * @return encryption  the encryption key
	 */	
	public   Map<Character, Character> getEncryptionKey() {
		List<Character> keys = new ArrayList<>();
		List<Character> values = new ArrayList<>();
		Map<Character, Character> encryption = new HashMap<>();
		for (char i = 65; i < 91; i++) {
			keys.add(i);
			values.add(i);
		}
		int j = -1; // use as a flag to reloop
		while (j == -1) {
			j = 0;
			Collections.shuffle(values); // random ordering
			for (int i = 0; i < keys.size(); i++) {
				// it cannot map itself
				if (keys.get(i) == values.get(i)) {
					j = -1;
					break;
				}
				encryption.put(keys.get(i), values.get(i));
			}
		}
		return encryption;
	}

	/**
	 * Select a random row for encryption
	 *
	 * @param  rows      the list of rows
	 * @return original  the row selected
	 */	
	public  List<Character> selectRandomRow(List<String> rows){
		Random rand = new Random(); 
		int index = rand.nextInt(rows.size()); 
		List<Character> original = new ArrayList<>();
		for (int i = 0; i < rows.get(index).length(); i++) {
			original.add(rows.get(index).charAt(i));
		}
		return original;
	}

	/**
	 * Open the file and change all to upper case
	 *
	 * @param  fileName   the name of file
	 * @return rows       the list of rows
	 */	
	public   List<String> openFile(String fileName) {
		Scanner sc = null;
		List<String> rows = new ArrayList<>();
		try {
			sc = new Scanner(new File(fileName));
			while (sc.hasNextLine()) {
				rows.add(sc.nextLine().toUpperCase());
			}
			sc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rows;
	}


}




