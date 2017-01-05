package com.axway.academy.hwEncryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A class to perform encryption of a text
 * @author Mila I
 *
 */
public class Encryption implements Variables {

	/**
	 *  private final String is used to generate the random String 
	 */
	private static final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
	static String fileNameInput;

	/**
	 * Generates a random string 
	 * @return string with different sequence of symbols each time the method is called
	 */
	private static String generateRandomString() {
		final int len = 16;
		SecureRandom rand = new SecureRandom();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(SYMBOLS.charAt(rand.nextInt(SYMBOLS.length())));
		}
		return sb.toString();
	}

	/**
	 * Performs text padding with whitespaces to be multiple of 16 bytes.
	 * 
	 * @param text
	 *            - text to be padded
	 * @return the padded text
	 */
	private static String padTextToBeMultipleTo16(String text) {
		int textSize = text.getBytes().length;
		int leftover = textSize % 16;
		if (leftover > 0) {
			for (int i = 0; i < 16 - leftover; i++) {
				text = text + " ";
			}
		}
		return text;
	}

	/**
	 * Encrypts the given text using random string key
	 * @param text - string to be encrypted
	 * @param randomStringKey - random string key
	 * @return - encrypted text
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private byte[] encryptFileContent(String text, String randomStringKey) throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// create an instance of the Cipher
		// specify the algorithm sued for encryption
		// the provider SunJCE is optional
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");

		// transform the key in the needed format by specifying the algorithm
		SecretKeySpec key = new SecretKeySpec(randomStringKey.getBytes("UTF-8"), "AES");

		// initialize the cipher
		// not that an initialization vector is also provided - it is also
		// optional
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

		// perform encryption
		return cipher.doFinal(text.getBytes("UTF-8"));
	}

	/**
	 * Encrypts the random string key using the public key
	 * @param text - random string key to be encrypted 
	 * @param pk - public key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] encryptRandomStringKey(String text, PublicKey pk)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		// create the cipher with the correct asymmetric algorithm used
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

		// initialize the cipher by providing the encryption key and encryption
		// mode
		// no initialization vector used this time as it provides no sensible
		// value
		cipher.init(Cipher.ENCRYPT_MODE, pk);

		// perform encryption
		return cipher.doFinal(text.getBytes("UTF-8"));
	}

	private static void encryptKeys(String randomStringKey) throws NoSuchAlgorithmException, InvalidKeyException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		// create a keypair generator and specify the algorithm with which keys
		// are going to be used.
		// the provide SUN is optional
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

		// generate the public-private key pair
		KeyPair keyPair = keyGen.generateKeyPair();

		// obtain the private key from the keypair
		PrivateKey privateKey = keyPair.getPrivate();
		// obtain the public key from the keypair
		PublicKey publicKey = keyPair.getPublic();

		byte[] encryptedRandomStringKey = encryptRandomStringKey(randomStringKey, publicKey);
		fo.writeToFile(encryptedRandomStringKey, fo.getCreatedFileName(RANDOM_STRING_KEY_FILE_NAME, fileNameInput));
		fo.writeToFile(privateKey.getEncoded(), fo.getCreatedFileName(PRIVATE_KEY_FILE_NAME, fileNameInput));
	}

	/**
	 * The complete method for the encryption.
	 * This method is called in the menu() method in HomeworkEncryption class
	 */
	public void performEncryption() {
		String randomStringKey = generateRandomString();
		System.out.println("Enter file name:");
		Scanner scan = new Scanner(System.in, "UTF-8");
		fileNameInput = scan.nextLine();
		File fileToBeEncrypted = new File(fileNameInput);
		File symmetricKeyFile = new File(fo.getCreatedFileName(RANDOM_STRING_KEY_FILE_NAME, fileNameInput));
		File privateKeyFile = new File(fo.getCreatedFileName(PRIVATE_KEY_FILE_NAME, fileNameInput));
		
		//check if random_string_key_fileName.txt and private_key_fileName.txt exist
		if (!symmetricKeyFile.exists() && !privateKeyFile.exists()) {
			System.out.println("Encryption of the file was once done.");
			return;
		}
		
		String fileContent = null;
		byte[] text = null;
		Path path = Paths.get(fileNameInput);
		try {
			if (fileToBeEncrypted.exists() && !fileToBeEncrypted.isDirectory()) {
				text = Files.readAllBytes(path);
				fileContent = new String(text, "UTF-8");
				System.out.println("File content hash code: " + fileContent.hashCode());
				fileContent = padTextToBeMultipleTo16(fileContent);
				
				//encrypts the file content
				byte[] encryptedText = encryptFileContent(fileContent, randomStringKey);
				
				//write to file the encrypted file content
				fo.writeToFile(encryptedText, fileNameInput);
				
				// encrypts the random string key and the private key
				encryptKeys(randomStringKey);
				fo.printEncryptedText(fileNameInput);
				System.out.println("Reading of file was successful.\nFile content: " + fileContent);
			} else {
				System.out.println("Encryption can't be performed.");
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("Cannot read from file." + fnfe);
		} catch (IOException ioe) {
			System.out.println("Cannot read from file." + ioe);
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchProviderException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchPaddingException e) {
			System.out.println(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			System.out.println(e.getMessage());
		} catch (BadPaddingException e) {
			System.out.println(e.getMessage());
		} 
	}

}
