package com.axway.academy.hw6;

import java.util.Scanner;

public class FileOperationsSample {
	
	/**
	 * Program to find and replace word/words from Properties file to clients file
	 * using Command Pattern and Singleton
	 * @param args
	 */

	public static void main(String[] args) {
		// create an object
		FileOperations fo = new FileOperations();
		
		// get the user input for the file name
		Scanner input = new Scanner(System.in, "UTF-8");
		System.out.println("Enter file name:");
		String fileName = input.nextLine();
		
		// initialize commands that are going to be used
		ReadCommand rc = new ReadCommand(fo, fileName);
		WriteCommand wc = new WriteCommand(fo, fileName);
		
		// create a broker /using singleton/ and register commands
		CommandBrocker cb = CommandBrocker.getInstance();
		cb.registerCommand(rc);
		cb.registerCommand(wc);
		
		// execute commands through the broker
		cb.execute();
		
		if (input != null) {
			input.close();
		}	
	}

}
