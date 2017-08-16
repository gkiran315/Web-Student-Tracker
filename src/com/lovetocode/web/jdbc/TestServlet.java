package com.lovetocode.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Defining datasource/connection pool for Resource Injection
	@Resource(name="jdbc/ttu_web_student_tracker")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Step1: Set up the printwriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		//Step2: Getting a connection to the database
		java.sql.Connection myConn = null;
		java.sql.Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = dataSource.getConnection();
			
			//Step3: Creating the SQL statements
			String sql = "select * from student";
			myStmt = myConn.createStatement();
		
			//Step4: Executing the SQL Query
			myRs = myStmt.executeQuery(sql);
		
			//Step5: Processing the Result Set
			while(myRs.next()) {
				String email = myRs.getString("email");
				out.println(email);
			}
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
