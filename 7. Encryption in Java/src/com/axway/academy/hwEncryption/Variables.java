package com.axway.academy.hwEncryption;

/**
 * Interface for the variables needed in Encryption class and Decryption class
 * @author Mila I
 *
 */
public interface Variables {
	/**
	 * Initialization vector value - initial value to be used for encryption of
	 * the first block of data.
	 */
	String IV = "MMMMMMMMMMMMMMMM";
	
	/**
	 * String variable used to create the file where the random string key will be stored
	 */
	String RANDOM_STRING_KEY_FILE_NAME = "random_string_key";
	
	/**
	 * String variable used to create the file where the private key will be stored
	 */
	String PRIVATE_KEY_FILE_NAME = "private_key";
	FileOperations fo = new FileOperations();
}
