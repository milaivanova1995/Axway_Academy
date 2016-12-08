package com.axway.academy.hwEncryption;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOperations {

	/**
	 * Writes to file some encrypted/decrypted text
	 * @param text - byte array containing some encrypted/decrypted text
	 * @param fileName
	 */
	public void writeToFile(byte[] text, String fileName) {
		Path file = Paths.get(fileName);
		try {
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
			Files.write(file, text);
			System.out.println("Writing to file succeeded.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates file with a configurable file name for both random string and private
	 * keys, depending of the given parameters 
	 * @param keyName - gives information whether configurable file name for the 
	 * random string key will be used or for the private key
	 * @param whichFile - user input of the file to be encrypted/decrypted
	 * @return
	 */
	public String getCreatedFileName(String keyName, String whichFile) {
		// the full file name is created here
		String fileName = keyName.concat("_".concat(whichFile));
		
		// if the file has no extension - .txt extension is added
		if (!fileName.toLowerCase().endsWith(".txt")) {
			fileName = fileName.concat(".txt");
		}
		Path file = Paths.get(fileName);
		try {
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	/**
	 * Method to read from a file
	 * @param fileName
	 */
	private static void readFile(String fileName) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ioe) {
				System.out.println("Cannot close streams." + ioe);
			}
		}
	}
	
	
	/**
	 * Prints the encrypted file content 
	 * @param fileName - User input for the file name
	 */
	public void printEncryptedText(String fileName) {
		System.out.println("Encrypted text:");
		readFile(fileName);
	}
	
}
