package com.axway.academy.hw3;

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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FirstTask {

	public static void main(String[] args) { 
		installTrustManager();
		StringBuilder sb = readFromResource();
		String fileName = getFileName();
		writeToFile(fileName, sb);
	}

	// Install the all-trusting trust manager
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
			url = new URL("http://www.google.com");
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
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
				System.out.println("Error closing streams.");
				e.printStackTrace();
			}
		}
		return sb;
	}

	public static String getFileName() {
		System.out.print("Enter file name: ");
		Scanner scanner = new Scanner(System.in);
		String fileName = scanner.next();
		scanner.close();
		return fileName;
	}

	public static void writeToFile(String fileName, StringBuilder sb) {
		File file = new File(fileName);
		OutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		if (!file.exists()) {
			try {
				out = new FileOutputStream(file);
				osw = new OutputStreamWriter(out);
				bw = new BufferedWriter(osw);
				bw.write(sb.toString());
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
		} else {
			System.out.println("A file with that name already exists!");
		}	
	}

}
