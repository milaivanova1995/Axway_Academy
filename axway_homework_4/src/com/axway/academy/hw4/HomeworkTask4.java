package com.axway.academy.hw4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeworkTask4 {

	public static void main(String[] args) {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/axway_academy_4?autoReconnect=true&useSSL=false";
		String username = "root";
		String pass = "mila0103";
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			Class.forName(driver);
			System.out.println("Connecting to database...");
			con = DriverManager.getConnection(url, username, pass);
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

	public static void getPerson(String sql, Connection con) throws SQLException {
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
