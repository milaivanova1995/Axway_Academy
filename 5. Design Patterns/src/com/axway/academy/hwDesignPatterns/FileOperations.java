package com.axway.academy.hwDesignPatterns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * This class is used to handle the file operatons 
 * such as reading files and writing files
 * @author Mila I
 *
 */
public class FileOperations {

	static String sFileName;
	private final static String PROPERTIES_FILE_NAME = "Properties.txt";
	private final static String NEW_LINE = System.getProperty("line.separator");

	/**
	 * Reads from the properties file the pairs of words to be replaced Reads
	 * from the client's file to find the words to be replaced
	 * 
	 * @param fileName
	 *            - a String taken as a user input for the file name
	 * @return
	 */
	public StringBuilder readFiles() {
		Properties properties = new Properties();
		File propertiesFile = new File(PROPERTIES_FILE_NAME);
		InputStream propertiesStream = null;
		// Reading from properties file
		try {
			if (propertiesFile.exists() && propertiesFile.isFile() && !(propertiesFile.isDirectory())) {
				propertiesStream = new FileInputStream(propertiesFile);
				properties.load(propertiesStream);
			} else {
				System.out.println("");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Properties file not found:");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Error reading from properties file:");
			System.out.println(e.getMessage());
		}

		File f = new File(sFileName);
		FileReader fr = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		// Reading from clients file
		try {
			if (!(f.isDirectory())) {
				if (f.exists()) {
					fr = new FileReader(sFileName);
					br = new BufferedReader(fr);

					while ((line = br.readLine()) != null) {
						Enumeration propertiesWords = properties.keys();
						String originalWord = null;
						String replacingWord = null;

						while ((originalWord = propertiesWords.nextElement().toString()) != null) {
							if (line.contains(originalWord)) {
								replacingWord = properties.getProperty(originalWord);
								line = line.replaceAll(originalWord, replacingWord);
							}
							if (!(propertiesWords.hasMoreElements())) {
								break;
							}
						}
						sb.append(line).append(NEW_LINE);
					}
				}
			}
		} catch (NoSuchElementException e) {
			System.out.println("Error finding element:");
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println("File not found:");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Error reading from file:");
			System.out.println(e.getMessage());
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				System.out.println("Error closing the streams:");
				System.out.println(e.getMessage());
			}
		}
		return sb;
	}

	/**
	 * writing to a new file with the new filename
	 * 
	 * @param text
	 */
	public void writeFile() {
		File f1 = new File(sFileName);
		String newFileName = changeFileName();
		File f2 = new File(newFileName);
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			if (f1.exists()) {
				if (!(f2.exists())) {
					if (!(f1.isDirectory()) && !(f2.isDirectory())) {
						fw = new FileWriter(newFileName);
						bw = new BufferedWriter(fw);
						bw.write(readFiles().toString());
						System.out.println("Done saving file.");
					} else {
						System.out.println("The name exists as a directory.");
					}
				} else {
					System.out.println("A file with that name already exists.");
				}
			} else {
				System.out.println("A file with that name does not exist.");
			}
		} catch (IOException e) {
			System.out.println("Error reading from file:");
			System.out.println(e.getMessage());
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				System.out.println("Error closing the streams:");
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * returns the new full filename
	 * 
	 * @return
	 */
	private static String changeFileName() {
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Date date = new Date();
		String newFileName = getFileNameWithoutExtension() + "_moddified_" + dateFormat.format(date).toString()
				+ ".txt";
		return newFileName;
	}

	/**
	 * if the file has extension, it removes it return the filename without
	 * extension
	 * 
	 * @param fileName
	 * @return
	 */
	private static String getFileNameWithoutExtension() {
		File f = new File(sFileName);
		String fileName = f.getName();
		int last = fileName.lastIndexOf(".");
		return last >= 1 ? fileName.substring(0, last) : fileName;
	}

}
