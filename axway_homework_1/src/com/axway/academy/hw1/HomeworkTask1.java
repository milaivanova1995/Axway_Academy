package com.axway.academy.hw1;

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
import java.util.Scanner;

public class HomeworkTask1 {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in, "UTF-8");
		System.out.println("Enter file name:");
		String fileName = input.nextLine();
		StringBuilder sb = readFiles(fileName, "Properties.txt");
		fileName = changeFileName(fileName);
		writeFile(sb, fileName);
		System.out.println("Done saving file.");
		input.close();
	}

	public static StringBuilder readFiles(String fileName, String propertiesFileName) {
		Properties properties = new Properties();
		File propertiesFile = new File(propertiesFileName);
		InputStream propertiesStream = null;
		
		// Reading from properties file
		try {
			propertiesStream = new FileInputStream(propertiesFile);
			properties.load(propertiesStream);
		} catch (FileNotFoundException e) {
			System.out.println("Properties file not found:");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Error reading from properties file:");
			System.out.println(e.getMessage());
		}
		
		FileReader fr = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		
		// Reading from clients file
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			
			while ((line = br.readLine()) != null) {
				Enumeration propertiesWords = properties.keys();
				String originalWord = null;
				String replacingWord = null;

				while ((originalWord = propertiesWords.nextElement().toString()) != null) {
					replacingWord = properties.getProperty(originalWord);
					line = line.replaceAll(originalWord, replacingWord);
					if (!(propertiesWords.hasMoreElements())) {
						break;
					}
				}
				sb.append(line + System.getProperty("line.separator"));
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

	public static void writeFile(StringBuilder text, String fileName) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			bw.write(text.toString());
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

	public static String changeFileName(String fileName) {
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Date date = new Date();
		String newFileName = getFileNameWithoutExtension(fileName) 
				+ "_moddified_" + dateFormat.format(date).toString()
				+ ".txt";
		return newFileName;
	}

	public static String getFileNameWithoutExtension(String fileName) {
		String[] name = fileName.split("[.]");
		return name[0];
	}

}