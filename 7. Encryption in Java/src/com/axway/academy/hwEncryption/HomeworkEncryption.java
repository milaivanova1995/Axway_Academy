package com.axway.academy.hwEncryption;

import java.util.Scanner;

/**
 * Program to encrypt and decrypt files chosen by the user
 * @author Mila I
 *
 */
public class HomeworkEncryption {

	/**
	 * Main method of the program.
	 * Only the menu() method is called
	 * @param args
	 */
	public static void main(String[] args) {
		menu();
	}

	/**
	 * Simple menu to show the program functionality. 
	 * It has 3 choices - to encrypt, to decrypt and to exit of the program
	 */
	private static void menu() {
		int choice;
		Scanner scn = new Scanner(System.in, "UTF-8");
		do {
			System.out.println("Choose an option:");
			System.out.println("1. Encrypt\n2. Decrypt\n3. Exit");
			choice = scn.nextInt();
			scn.nextLine();
			if (choice == 1) {
				Encryption encryption = new Encryption();
				System.out.println("How many files would you like to encrypt?");
				int encryptNumber = scn.nextInt();
				scn.nextLine();
				for (int i = 0; i < encryptNumber; i++) {
					encryption.performEncryption();
				}
			}
			if (choice == 2) {
				Decryption decryption = new Decryption();
				System.out.println("How many files would you like to decrypt?");
				int decryptNumber = scn.nextInt();
				scn.nextLine();
				for (int i = 0; i < decryptNumber; i++) {
					decryption.performDecryption();
				}
			}
			if (choice == 3) {
				System.out.print("Exit.");
				System.exit(3);
			}
		} while (choice != 3);
		if(scn != null) {
			scn.close();
		}
	}
}