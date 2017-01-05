package com.axway.academy.hw8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseHandler {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/axway_academy_8?autoReconnect=true&useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASS = "mila0103";
	
	String username;
	String password;
	String fullName;
	int age;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public void setPersonData(String userInput) {
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			Class.forName(DRIVER);
			System.out.println("Connecting to database...");
			con = DriverManager.getConnection(URL, USERNAME, PASS);
			System.out.println("Creating statement...");
			String sql = "select username, password, fullName, age from users where username=" + userInput + ";";
			prepStmt = con.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			setUsername(rs.getString("username"));
			setPassword(rs.getString("password"));
			setFullName(rs.getString("fullName"));
			setAge(rs.getInt("age"));
		} catch (ClassNotFoundException ce) {
			System.out.println(ce.getMessage());
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (prepStmt != null) {
					prepStmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			System.out.println("Reading from database succeeded.");
		}
	}
	
}
