package com.axway.academy.hw3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Creating an FTP example that: lists all file inside a folder downloads a file
 * uploads a file deletes a file
 * 
 * @author Mila I
 *
 */

public class HomeworkFTP {

	// login credentials
	final static String server = "192.168.1.100";
	final static String username = "AdminM";
	final static String password = "1234";
	final static String localPath = "D:/My Pictures/image.jpg";
	final static String remoteFile = "love.jpg";
	final static String downloadedFileName = "testDownload.txt";
	

	public static void main(String[] args) {
		FTPClient f = new FTPClient();
		OutputStream out = null;
		InputStream in = null;
		try {
			connectToServer(f);
			listFiles(f);
			downloadFile(f, out, "Hello.txt");
			uploadFile(f, in);
			deleteFile(f, "Mila.txt");
			listFiles(f);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (IOException e) {
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

	}

	// connect to server and authenticate
	public static void connectToServer(FTPClient f) throws SocketException, IOException {
		f.connect(server);
		f.login(username, password);
	}

	// list files from the server /one folder level only/
	public static void listFiles(FTPClient f) throws IOException {
		FTPFile[] files = f.listFiles();
		for (FTPFile file : files) {
			String fileName = file.getName();
			System.out.println(fileName);
		}
	}

	// download file from the server
	public static void downloadFile(FTPClient f, OutputStream out, String fileName) throws FileNotFoundException, IOException {
		File file = new File(downloadedFileName);
		boolean successDownloading = false;
		if (!file.exists()) {
			out = new FileOutputStream(file);
			successDownloading = f.retrieveFile(fileName, out);
		} else {
			System.out.println("A file with that name already exists");
		}
		if (successDownloading) {
			System.out.println("Downloading succeeded!");
		} else {
			System.out.println("Downloading failed!");
		}

	}
	
	// upload file to the server
	public static void uploadFile(FTPClient f, InputStream in) throws IOException {
		boolean isDone = false;
		if (!checkIfFileExists(f, remoteFile)) {
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
	}
	
	// delete a file from the server
	public static void deleteFile(FTPClient f, String fileName) throws IOException {
		boolean isDone = false;
		if (checkIfFileExists(f, fileName)) {
			isDone = f.deleteFile(fileName);
		} else {
			System.out.println("File does not exist!");
		}
		if (isDone) {
			System.out.println("Deleting succeeded");
		} else {
			System.out.println("Deleting failed");
		}
	}
	
	// check if file exists on the server
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

}
