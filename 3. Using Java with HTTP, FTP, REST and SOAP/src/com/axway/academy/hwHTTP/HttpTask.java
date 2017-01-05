package com.axway.academy.hwHTTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * A program to connect to http://www.google.com and write the response to a
 * file.
 * 
 * @author Mila I
 *
 */
public class HttpTask {

	private static final String URL = "http://www.google.com";

	/**
	 * Executes the methods in the class. Connects to the chosen url and save
	 * the response in a file.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		installTrustManager();
		StringBuilder sb = readFromResource();
		String fileName = getFileName();
		writeToFile(fileName, sb.toString());
	}

	/**
	 * Install the all-trusting trust manager
	 */
	public static void installTrustManager() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reads the data from the resource
	 * 
	 * @return
	 */
	public static StringBuilder readFromResource() {
		BufferedReader br = null;
		InputStreamReader ir = null;
		InputStream is = null;
		HttpURLConnection connection = null;
		URL url = null;
		int statusCode = 0;
		StringBuilder sb = null;

		try {
			// initialize the resource
			url = new URL(URL);
			connection = (HttpURLConnection) url.openConnection();

			if (connection != null) {
				// set request method
				connection.setRequestMethod("GET");

				// execute the request and get status code and message
				statusCode = connection.getResponseCode();
				System.out.println("The status code is " + statusCode);
				System.out.println("Response message is " + connection.getResponseMessage());

				// read the response and print it
				is = connection.getInputStream();
				ir = new InputStreamReader(is);
				br = new BufferedReader(ir);
				sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
			}
		} catch (MalformedURLException e) {
			System.out.println("URL error: " + e.getMessage());
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.out.println("Protocol error: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Input/Output error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// close all streams
			try {
				if (br != null) {
					br.close();
				}
				if (ir != null) {
					ir.close();
				}
				if (is != null) {
					is.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (IOException e) {
				System.out.println("Error closing streams: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return sb;
	}

	/**
	 * A method to get the file name entered by the user
	 * 
	 * @return
	 */
	public static String getFileName() {
		System.out.print("Enter file name: ");
		Scanner scanner = new Scanner(System.in);
		String fileName = scanner.next();
		scanner.close();
		return fileName;
	}

	/**
	 * 
	 * @param fileName
	 *            - String variable taken as a user input
	 * @param sb
	 */
	public static void writeToFile(String fileName, String text) {
		File file = new File(fileName);
		 OutputStream out = null;
		 OutputStreamWriter osw = null;
		 BufferedWriter bw = null;
		if (file.exists()) {
			System.out.println("A file with that name already exists!");
			return;
		}

		 try {
		 out = new FileOutputStream(file);
		 osw = new OutputStreamWriter(out);
		 bw = new BufferedWriter(osw);
		 bw.write(text);
		 } catch (FileNotFoundException e) {
		 e.printStackTrace();
		 } catch (IOException e) {
		 e.printStackTrace();
		 } finally {
		 // close all streams
		 try {
		 if (bw != null) {
		 bw.close();
		 }
		 if (osw != null) {
		 osw.close();
		 }
		 if (out != null) {
		 out.close();
		 }
		 } catch (IOException e) {
		 System.out.println("Error closing streams.");
		 e.printStackTrace();
		 }
		 if (file.exists()) {
		 System.out.println("Writing succeeded!");
		 } else {
		 System.out.println("Writing failed!");
		 }
		 }

	}

}
