package com.axway.academy.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Program to extract data from the database.
 * Finds the youngest and the oldest person in the table
 * and prints the information about them
 * @author Mila I
 *
 */
public class HomeworkJDBC {
	
	/**
	 * Static final variables used to establish the connection with the database
	 */
	private static final String SDRIVER = "com.mysql.jdbc.Driver";
	private static final String SURL = "jdbc:mysql://localhost:3306/axway_academy_4?autoReconnect=true&useSSL=false";
	private static final String SUSERNAME = "root";
	private static final String SPASS = "mila0103";

	/**
	 * Calls the execute(); method 
	 * @param args
	 */
	public static void main(String[] args) {
		execute();
	}
	
	/**
	 * Connects to database ad calls the getPerson(); method
	 */
	public static void execute() {
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			Class.forName(SDRIVER);
			System.out.println("Connecting to database...");
			con = DriverManager.getConnection(SURL, SUSERNAME, SPASS);
			System.out.println("Creating statement...");
			String youngest = "select name, surname, birthdate from employees where birthdate = (select max(birthdate) from employees);";
			String oldest = "select name, surname, birthdate from employees where birthdate = (select min(birthdate) from employees);";
			getPerson(youngest, con);
			getPerson(oldest, con);
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

	/**
	 * Gets the person's data dependent on the sql statement
	 * @param sql - a String which represents the query
	 * @param con
	 * @throws SQLException
	 */
	private static void getPerson(String sql, Connection con) throws SQLException {
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		prepStmt = con.prepareStatement(sql);
		rs = prepStmt.executeQuery();
		String name;
		String surname;
		String birthdate;
		while (rs.next()) {
			name = rs.getString("name");
			surname = rs.getString("surname");
			birthdate = rs.getString("birthdate");
			System.out.println(name + " " + surname + " " + birthdate);
		}
	}

}
