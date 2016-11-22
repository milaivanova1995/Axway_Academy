package com.axway.academy.hw6;

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

public class FileOperations {

	private final static String propertiesFileName = "Properties.txt";
	private final static String newLine = System.getProperty("line.separator");

	/**
	 * Reads from the properties file the pairs of words to be replaced Reads
	 * from the client's file to find the words to be replaced
	 * 
	 * @param fileName
	 *            - a String taken as a user input for the file name
	 * @return
	 */
	public StringBuilder readFiles(String fileName) {
		Properties properties = new Properties();
		File propertiesFile = new File(propertiesFileName);
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

		File f = new File(fileName);
		FileReader fr = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		// Reading from clients file
		try {
			if (!(f.isDirectory())) {
				if (f.exists()) {
					fr = new FileReader(fileName);
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
						sb.append(line).append(newLine);
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
	public void writeFile(StringBuilder text, String newFileName, String fileName) {
		File f1 = new File(fileName);
		File f2 = new File(newFileName);
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			if (f1.exists()) {
				if (!(f2.exists())) {
					if (!(f1.isDirectory()) && !(f2.isDirectory())) {
						fw = new FileWriter(newFileName);
						bw = new BufferedWriter(fw);
						bw.write(text.toString());
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
	public String changeFileName(String fileName) {
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Date date = new Date();
		String newFileName = getFileNameWithoutExtension(fileName) + "_moddified_" + dateFormat.format(date).toString()
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
	public static String getFileNameWithoutExtension(String filename) {
		File f = new File(filename);
		String fileName = f.getName();
		int last = fileName.lastIndexOf(".");
		return last >= 1 ? fileName.substring(0, last) : fileName;
	}

}
