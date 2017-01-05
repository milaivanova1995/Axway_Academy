package com.axway.academy.hw8;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletHomework extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DataBaseHandler dbh = new DataBaseHandler();
		String username = req.getParameter("username");
		String pass = req.getParameter("password");
		dbh.setPersonData(username);
		if (username != null && username.equals(dbh.getUsername())) {
			if (pass.equals(dbh.getPassword())) {
				response(resp, "login OK");
				// save file with user's data
			} else {
				response(resp, "login FAILED");
			}
		}
		
	}
	
	public void response(HttpServletResponse resp, String msg)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(resp.getWriter());
		writer.write("<html>");
		writer.write("<body>");
		writer.write("<t1>" + msg + "</t1>");
		writer.write("</body>");
		writer.write("</html>");
	}

}
