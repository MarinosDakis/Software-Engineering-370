import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class HashTable {
	//###########################################################################
	//                            hashCode METHOD                               #
	//###########################################################################
	//this method will implement custom hashCode for hashTable
	
	//###########################################################################
	//                            equals METHOD                                 #
	//###########################################################################
	// this method will override inbuilt .equals method for hashTable so that
	// it can compare against key values, rather than multiple hashTables
	
	//###########################################################################
	//                            clearTable METHOD                             #
	//###########################################################################
	// this method clears hashTable and its keys & values if it isn't already empty
	public void clearTable(Hashtable<String, Integer> hash) { // start of clearTable method
		
		if (hash.isEmpty() == true) { // if checks if empty, if true state empty
			System.err.println("Hashtable is already empty");
			return;
		} else 
			hash.clear(); //clear hashTable

		// add to transaction log
		
	} // end of clearTable method

	//###########################################################################
	//                            addToTable METHOD                             #
	//###########################################################################
	// method checks if a key value is already contained within the table, and if
	// not adds to hashTable
	public static void addToTable(Hashtable<String, Integer> hash, String key) { // start of addToTable method
		
		if (hash.containsKey(key) == true) { // if true exists
			System.err.println("Key is already contained within hashtable" + "\n");
			return;
		} else
			hash.put(key, key.hashCode());

		
		// add to transaction log
		

	}// end of addToTable method

	//###########################################################################
	//                            removeFromTable METHOD                        #
	//###########################################################################
	// method checks if a key value is already contained within the table, and if not
	// removes from hashTable	
	public static void removeFromTable(Hashtable<String, Integer> hash, String key) { // start of removeToTable method
		if (hash.containsKey(key) == false) {
			System.err.println("Key does not exist" + "\n");
			return;
		} else
			hash.remove(key);

		// add to transaction log
		

	}// end of removeFromTable method

	//###########################################################################
	//                            printTable METHOD                             #
	//###########################################################################
	// this method prints all the keys & mappings
	public static void printTable(Hashtable<String, Integer> hash) { // start of printTable method
		for (String keys : hash.keySet()) { // start for reach loop
			System.out.println(keys + " " + hash.get(keys));
		} // end for each loop

		// add to transaction log

	}// end of printTable method

	//###########################################################################
	//                            ModifyTable METHOD                            #
	//###########################################################################
	// this method inputs a current key in which you want to replace with a
	// different key (also implements add/remove methods above
	public static void modifyTable(Hashtable<String, Integer> hash, String currentKey, String keyToReplace) { // start of
																										// modifyTable
																								     	// method
		// check if hashTable is empty
		if (hash.isEmpty() == true) {
			System.err.println("Hashtable is empty");
			return;
		}

		// check if new key is already inside hashTable
		if (hash.containsKey(keyToReplace) == false) {
			System.err.println("Key does not exist" + "\n");
			return;
		}

		// apply methods to remove and replace old key value with new, along with new
		// hashCode
		removeFromTable(hash, currentKey);
		addToTable(hash, keyToReplace);

		// add to transaction log

	} // end of modifyTable method

	//###########################################################################
	//                            locate METHOD                                 #
	//###########################################################################
	// method to find certain keys
	public static String locate(Hashtable<String, Integer> hash, String wordToFind) {
		return wordToFind;
	}
	
	//###########################################################################
	//                            importFile METHOD                             #
	//###########################################################################
	// method to import file
	
	public static void importFile(Hashtable<String, Integer> hash, String path) { //start importFile
		// Instantiating variables for file
		FileInputStream inputFile = null;
		
		try {//try to import file
			inputFile = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// creating Scanner to read inputFile
		Scanner scanInput = new Scanner(inputFile);
		
		//Copying contents from file to HashTable
		while (scanInput.hasNextLine()) {
			addToTable(hash, scanInput.nextLine());
		}
		
		//closing Scanner
		scanInput.close();
		

	} // end importFile method
	
	//###########################################################################
	//                            exportFile METHOD                             #
	//###########################################################################
	// method to export file
	public static void exportFile(Hashtable<String, Integer> hash, String outputPath) throws Exception { //start exportFile
		// Instantiating variables for file
		File targetFile = new File(outputPath);
		FileWriter out = new FileWriter(targetFile, true); // true so file is not overwritten
		
	    //copying contexts from HashTable to outputFile
		for (String keys : hash.keySet()) { // start for reach loop
			String keyData = keys;
			Integer MapData = hash.get(keys);
			out.write(keyData + " " + MapData + "\n");
		} // end for each loop
		
		//flushing FileWriter
		out.close();


	} // end exportFile method
	
	//###########################################################################
	//                              setLogger METHOD                            #
	//###########################################################################
	// method that creates a logger that displays information
	public static void setLogger(String logInput) { //start of setLogger method
		
		 //create logger (takes into account package)
		 final Logger logify = Logger.getLogger(Logger.class.getName());
		
		//resets all default handlers to put custom created in LogFormatter class
		 LogManager.getLogManager().reset(); 
	 
		 //creating new custom Handler()
	     ConsoleHandler logHandler = new ConsoleHandler(); //logHandler control console output
	     Formatter format = new LogFormatter(); // format is based off custom LogFormatter class
	     
	     logHandler.setFormatter(format); // logHandler (controls console) sets custom format
	     logify.addHandler(logHandler); //current logger adds custom Handler to itself
	     
	     logify.severe(logInput);
	     
	} //end of setLogger method
}
