package com.axway.academy.hwEncryption;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A class to perform decryption of an encrypted text
 * @author Mila I
 *
 */
public class Decryption implements Variables {

	static String fileNameInput;

	/**
	 * Decrypts the random string key
	 * @param randomStringKey - byte array containing the encrypted random string key
	 * @param pk - private key used to decrypt the random string key
	 * @return decrypted random string key
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static String decryptRandomStringKey(byte[] randomStringKey, PrivateKey pk)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		// create the cipher with the correct asymmetric algorithm used
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

		// initialize the cipher by providing the encryption key and encryption
		// mode
		// no initialization vector used this time as it provides no sensible
		// value
		cipher.init(Cipher.DECRYPT_MODE, pk);

		// perform decryption
		return new String(cipher.doFinal(randomStringKey), "UTF-8");
	}

	/**
	 * Decrypts an encrypted text using random string key
	 * @param cipherText - encrypted file content
	 * @param randomStringKey - used to decrypt the text
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static String decryptText(byte[] cipherText, String randomStringKey) throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// create an instance of the Cipher
		// specify the algorithm sued for encryption
		// the provider SunJCE is optional
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");

		// transform the key in the needed format by specifying the algorithm
		SecretKeySpec key = new SecretKeySpec(randomStringKey.getBytes("UTF-8"), "AES");

		// initialize the cipher
		// not that an initialization vector is also provided - it is also
		// optional
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

		// perform decryption
		return new String(cipher.doFinal(cipherText), "UTF-8");
	}

	/**
	 * Decrypts the private key which decrypts the random string key
	 * @return decrypted random string key
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static String decryptKeys() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Path privateKeyPath = Paths.get(fo.getCreatedFileName(PRIVATE_KEY_FILE_NAME, fileNameInput));
		byte[] privateKeyArray = Files.readAllBytes(privateKeyPath);
		PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyArray));
		Path randomStringKeyPath = Paths.get(fo.getCreatedFileName(RANDOM_STRING_KEY_FILE_NAME, fileNameInput));
		byte[] randomStringKeyArray = Files.readAllBytes(randomStringKeyPath);
		return decryptRandomStringKey(randomStringKeyArray, privateKey);
	}

	/**
	 * The complete method for the dencryption.
	 * This method is called in the menu() method in HomeworkEncryption class
	 */
	public void performDecryption() {
		Scanner scan = new Scanner(System.in, "UTF-8");
		System.out.println("Enter file name:");
		fileNameInput = scan.nextLine();
		if (!fileNameInput.toLowerCase().endsWith(".txt")) {
			fileNameInput = fileNameInput.concat(".txt");
		}
		Path path = Paths.get(fileNameInput);
		File file = new File(fileNameInput);
		File file2 = new File(fo.getCreatedFileName(RANDOM_STRING_KEY_FILE_NAME, fileNameInput));
		File file3 = new File(fo.getCreatedFileName(PRIVATE_KEY_FILE_NAME, fileNameInput));
		//check if random_string_key_fileName.txt and private_key_fileName.txt exist
		if (!file2.exists() && !file3.exists()) {
			System.out.println("File was not encrypted. Decryption can't be performed.");
			return;
		}
		
		byte[] encryptedText;
		try {
			if (file.exists() && !file.isDirectory()) {
				String decryptedRandomStringKey = decryptKeys();
				encryptedText = Files.readAllBytes(path);
				String decryptedText = decryptText(encryptedText, decryptedRandomStringKey);
				System.out.println("Decrypted text hash code: " + decryptedText.hashCode());
				fo.writeToFile(decryptedText.getBytes(), fileNameInput);
				
				//deletes the random string key and private keys files
				//because they are useless after the decryption
				//file2.delete();
				//file3.delete();
			} else {
				System.out.println("Decryption can't be performed.");
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
		} catch (InvalidKeySpecException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchPaddingException e) {
			System.out.println(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			System.out.println(e.getMessage());
		} catch (BadPaddingException e) {
			System.out.println(e.getMessage());
		} catch (NoSuchProviderException e) {
			System.out.println(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println(e.getMessage());
		}
	}
}
