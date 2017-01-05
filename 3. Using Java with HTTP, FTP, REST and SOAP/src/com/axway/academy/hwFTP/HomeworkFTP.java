package com.axway.academy.hwFTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Creating an FTP example that: lists all file inside a folder downloads a
 * file, uploads a file, deletes a file
 * 
 * @author Mila I
 *
 */

public class HomeworkFTP {

	private static LoginCredentials lc = new LoginCredentials();

	// login credentials
	// String server = "192.168.0.103";
	// String username = "AdminM";
	// String password = "1234";
	// final static String localPath = "D:/My Pictures/image.jpg";
	// final static String remoteFile = "love.jpg";
	// final static String downloadedFileName = "testDownload.txt";
	static Scanner scan = new Scanner(System.in, "UTF-8");

	/**
	 * Connect to server and authenticate
	 * 
	 * @param f
	 * @throws SocketException
	 * @throws IOException
	 */
	public static void connectToServer(FTPClient f) throws SocketException, IOException {
		f.connect(lc.getServer());
		f.login(lc.getUsername(), lc.getPassword());
	}

	/**
	 * List files from the server /one folder level only/
	 * 
	 * @param f
	 * @throws IOException
	 */
	public static void listFiles(FTPClient f) throws IOException {
		FTPFile[] files = f.listFiles();
		for (FTPFile file : files) {
			String fileName = file.getName();
			System.out.println(fileName);
		}
	}

	/**
	 * Downloads file from the server
	 * 
	 * @param f
	 * @param out
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void downloadFile(FTPClient f, OutputStream out) throws FileNotFoundException, IOException {
		System.out.println("Choose a file to be downloaded: ");
		String fileToBeDownloaded = scan.nextLine();
		System.out.println("Save file as:");
		String downloadedFileName = scan.nextLine();
		File downloadedFile = new File(downloadedFileName);
		File retrieveFile = new File(fileToBeDownloaded);
		boolean successDownloading = false;
		if (!downloadedFile.exists() && retrieveFile.exists()) {
			out = new FileOutputStream(downloadedFile);
			successDownloading = f.retrieveFile(fileToBeDownloaded, out);
		} else {
			System.out.println(
					"A file with that name already exists/The file that you are trying to download is missing");
		}
		if (successDownloading) {
			System.out.println("Downloading succeeded!");
		} else {
			System.out.println("Downloading failed!");
		}

		// closing stream if opened
		if (out != null) {
			out.close();
		}

	}

	/**
	 * Uploads file to the server
	 * 
	 * @param f
	 * @param in
	 * @throws IOException
	 */
	public static void uploadFile(FTPClient f, InputStream in) throws IOException {
		boolean isDone = false;
		System.out.println("Choose a file to upload and enter its full path:");
		String localPath = scan.nextLine();
		System.out.println("Enter the remote file name:");
		String remoteFile = scan.nextLine();
		if (!checkIfFileExists(f, remoteFile) && checkIfFileExists(f, localPath)) {
			f.enterLocalPassiveMode();
			f.setFileType(FTP.BINARY_FILE_TYPE);
			File localFile = new File(localPath);
			in = new FileInputStream(localFile);
			System.out.println("Unploading started...");
			isDone = f.storeFile(remoteFile, in);
		} else {
			System.out.println("A file with that name already exists");
		}
		if (isDone) {
			System.out.println("Uploading succeeded");
		} else {
			System.out.println("Uploading failed");
		}
		// closing stream if opened
		if (in != null) {
			in.close();
		}

	}

	/**
	 * Deletes a file from the server
	 * 
	 * @param f
	 * @param fileName
	 * @throws IOException
	 */
	public static void deleteFile(FTPClient f) throws IOException {
		boolean isDone = false;
		System.out.println("Enter a file to be deleted: ");
		String fileToBeDeleted = scan.nextLine();
		if (checkIfFileExists(f, fileToBeDeleted)) {
			isDone = f.deleteFile(fileToBeDeleted);
		} else {
			System.out.println("File does not exist!");
		}
		if (isDone) {
			System.out.println("Deleting succeeded");
		} else {
			System.out.println("Deleting failed");
		}
	}

	/**
	 * Check if file exists on the server
	 * 
	 * @param f
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static boolean checkIfFileExists(FTPClient f, String filename) throws IOException {
		FTPFile[] files = f.listFiles();
		for (FTPFile file : files) {
			String fileName = file.getName();
			if (fileName.equals(filename)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Inputs login credentials
	 */
	private static void inputLoginCredentials() {
		System.out.println("Enter hostname:");
		lc.setServer(scan.nextLine());
		System.out.println("Enter username:");
		lc.setUsername(scan.nextLine());
		System.out.println("Enter password:");
		lc.setPassword(scan.nextLine());
	}

	/**
	 * Menu method to execute user's choice of listing, uploading, downloading
	 * or deleting files
	 */
	private static void menu() {
		FTPClient f = new FTPClient();
		OutputStream out = null;
		InputStream in = null;
		int choice;
		inputLoginCredentials();
		try {
			connectToServer(f);
			System.out.println("Connected to server");
		} catch (SocketException se) {
			System.out.println("Problem with the socket: " + se.getMessage());
			se.printStackTrace();
		} catch (IOException e) {
			System.out.println("Input/Output problem: " + e.getMessage());
			e.printStackTrace();
		}
		do {
			System.out.println("Choose an option:");
			System.out.println("1. List all files\n2. Download file\n3. Upload file\n4. Delete file\n5. Exit.");
			choice = scan.nextInt();
			scan.nextLine();
			try {
				if (choice == 1) {
					listFiles(f);
				}
				if (choice == 2) {
					downloadFile(f, out);
				}
				if (choice == 3) {
					uploadFile(f, in);
				}
				if (choice == 4) {
					deleteFile(f);
				}
				if (choice == 5) {
					System.out.print("Exit.");
					System.exit(5);
				}
			} catch (FileNotFoundException fnfe) {
				System.out.println("Error finding the file: " + fnfe.getMessage());
				fnfe.printStackTrace();
			} catch (IOException e) {
				System.out.println("Input/Output problem: " + e.getMessage());
				e.printStackTrace();
			} finally {
				// closing streams and disconnect
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}
					if (f != null) {
						f.disconnect();
					}
				} catch (IOException e) {
					System.out.println("Problem disconnecting");
					e.printStackTrace();
				}
			}
		} while (choice != 5);
		if (scan != null) {
			scan.close();
		}
	}

	/**
	 * Main method to execute the menu
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		menu();
	}

}
