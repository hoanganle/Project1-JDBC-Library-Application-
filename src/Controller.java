/**
 *Program:Controller.java
 *Purpose:
 *Author:Gang
 *Date:Jul. 27, 2019
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
	private  Connection myConn ;
	private  Statement myStmt ;
	private  ResultSet myRslt; 
	 
	 public Controller() throws SQLException {
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/INFO3136_Books?useSSL=false&allowPublicKeyRetrieval=true", "root","hangang");			
			myStmt = myConn.createStatement();	 
	 }
	 
	 public ResultSet queryAuthor() throws SQLException {		
			myRslt = myStmt.executeQuery("SELECT last_name,first_name FROM Author");
			return myRslt;	 
	 }
	
	 public ResultSet queryBorrower() throws SQLException {
		 myRslt = myStmt.executeQuery("SELECT Subject FROM book");
			return myRslt;	 
		 
	 }
	 

}
//end class