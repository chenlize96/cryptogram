import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

public class CryptogramModel extends Observable{

	public List<Character> original;
	public Map<Character, Character> encryption;
	public List<Character> encoded;
	public List<Character> decoded;
	public Map<Character, Character> record;
	public List<String> encodeInitial; // fixed
	public List<String> decodeInitial; // used to update

	public CryptogramModel() {
		startUp(null, decoded);
	}



	public List<Character> decode(Map<Character, Character> record, 
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


	public boolean process(String strCommand) {
		String[] command = strCommand.split("\\s+");
		int index = readCommand(command, record);
		execute(index, encryption, record, encoded);
		decoded = decode(record, encoded, decoded);
		System.out.println();
		setChanged();                        //
		notifyObservers(record);             //
		if (original.equals(decoded)) {
			return true; // break the loop if succeed in completing
		}
		return false;
	}

	public void execute(int index, Map<Character, Character> encryption,
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

	public Map<Character, Character> 
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
		setChanged();
		notifyObservers(record);
		return record;
	}




	public int readCommand(String[] command, 
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







	public List<Character> startUp(List<Character> encodedList, 
			List<Character> decodedList){
		original = selectRandomRow(openFile("quotes.txt"));
		encryption = getEncryptionKey();
		if (encodedList == null) 
			encodedList = encode(encryption, original);	
		encodeInitial = display(encodedList, 30); // no more than 30 characters
		for (String word: display(encodedList, 80)) // no more than 80 characters
			System.out.println(word.trim());
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


	public List<String> display(List<Character> encoded, int num) {
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
			if (j == num && i + 1 < encoded.size() && 
					encoded.get(i + 1) != ' '){
				str = str.substring(0, str.length() - k);
				words.add(str);
				i = i - k;
				str = ""; j = 0; k = 0;
			}else if (j <= num && i + 1 == encoded.size()) {
				words.add(str);
			}else if (j == num && i + 1 < encoded.size() && 
					encoded.get(i + 1) == ' ') {
				words.add(str);
				str = ""; j = 0; k = 0; i++;
			}
		}
		return words;
	}

	public List<String> openFile(String fileName) {
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

	public List<Character> selectRandomRow(List<String> rows){
		Random rand = new Random(); 
		int index = rand.nextInt(rows.size()); 
		List<Character> original = new ArrayList<>();
		for (int i = 0; i < rows.get(index).length(); i++) {
			original.add(rows.get(index).charAt(i));
		}
		return original;
	}

	public Map<Character, Character> getEncryptionKey() {
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


	public List<Character> encode(Map<Character, Character> encryption, 
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


	public Map<Character, Integer> getFreq(List<Character> encoded) {
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

	private boolean checkChar(String aChar) {
		char first = aChar.charAt(0);
		if (aChar.length() != 1 || first < 65 || first > 90) 
			return false;
		else
			return true;
	}

}
