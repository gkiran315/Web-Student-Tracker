package com.lovetocode.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	public List<Student> getStudents() throws Exception{
		
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			
			//Getting the connection
			
			myConn = dataSource.getConnection();
			
			//Creating the sql statement
			
			String sql = "select * from student order by last_name";
			
			myStmt = myConn.createStatement();
			
			//Executing the query
			
			myRs = myStmt.executeQuery(sql);
			
			//process the Result Set
			
			while(myRs.next()) {
				
				//retrieve data from Result Set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String  lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//Creating a new student object
				
				Student tempStudent = new Student(id,firstName,lastName,email);
				
				//Adding it to the list of students
				
				students.add(tempStudent);
			}
				
			return students;
		}
		finally {
			
			//close the JDBC objects
			close(myConn, myStmt, myRs);
		}
		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		
		try {
			
			if(myConn!=null) {
				myConn.close();
			}
			
			if(myStmt!=null) {
				myStmt.close();
			}
			
			if(myRs!=null) {
				myRs.close();
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = 	null;
		
		try {
			
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql for inserting the students
			String sql = "insert into student"
					   + "(first_name,last_name,email)"
					   + "values(?,?,?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			//set the param values to the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
						
			//execute sql insert
			myStmt.execute();
			
		}
		
		finally {
			//clean up jdbc objects
			
			close(myConn, myStmt,null);
		}
		
	}

	public Student getStudent(String theStudentId) throws Exception {
		
		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			
			//convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn = dataSource.getConnection();
			
			//create sql to get selected student
			String sql = "select * from student where id=?";
			
			//create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute statement
			myRs = myStmt.executeQuery();
			
			//retrieve data from result set row
			if(myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//using the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find the student id: "+studentId);
			}
			
			return theStudent;
		}
		finally {
			
			//clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
		
	}

	public void updateStudent(Student theStudent) throws Exception {
		
		
			Connection myConn = null;
			PreparedStatement myStmt = null;
			
		try {	
			//Get the db connection
			myConn = dataSource.getConnection();
			
			//creating the sql update statement
			String sql = "update student "
					   + "set first_name=?, last_name=?, email=? "
					   + "where id=?";
			
			
			//Prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			//execute the SQL statement
			myStmt.execute();
		}
		finally {
			close(myConn, myStmt, null);
		}
	}

	public void deleteStudent(String theStudentId) throws Exception{
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			
			//converting the student id to int
			int studentId = Integer.parseInt(theStudentId);
			
			//get connetion to the database
			myConn = dataSource.getConnection();
			
			//create sql to delete statement
			String sql = "delete from student where id=? ";
			
			//Prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute the SQL statement
			myStmt.execute();
		}
		finally {
			close(myConn, myStmt, null);
		}
		
	}
}
