/**
 *Program:Controller.java
 *Purpose:
 *Author:Gang,An
 *Date:Jul. 27, 2019
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
	private  Connection myConn ;
	private  Statement myStmt ;
	private PreparedStatement myPreparedStatement;
	private  ResultSet myRslt; 
	 //constructor
	 public Controller() throws SQLException {
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/INFO3136_Books?useSSL=false&allowPublicKeyRetrieval=true", "root","hangang");			
			myStmt=	myConn.createStatement();
	 }
	 
	 //Get a list of all books in the library
	 public ResultSet queryBooksInLibrary() throws SQLException {
		 myRslt = myStmt.executeQuery("SELECT Title \r\n" + 
		 		                        "FROM book\r\n" + 
		 		                        "WHERE Available >0");
			return myRslt;	 
		 
	 }
	 //Get a list of all books currently OUT ON LOAN from the library and the full name of the borrower
	 public ResultSet queryBooksOnLoan () throws SQLException {
		 myRslt = myStmt.executeQuery("SELECT DISTINCT b.Title, br.First_Name ,br.Last_Name\r\n" + 
		 		"FROM book AS b\r\n" + 
		 		"INNER JOIN book_loan AS bl\r\n" + 
		 		"ON b.BookID = bl.Book_BookID \r\n" + 
		 		"INNER JOIN borrower AS br\r\n" + 
		 		"ON br.Borrower_ID = bl.Borrower_Borrower_ID\r\n" +
		 		"WHERE bl.Date_returned is null");
			return myRslt;	 
		 
	 }
	 
	 //Get a list of all books on a particular subject
	 public ResultSet queryBookBySubject(String subjString) throws SQLException {		
			myRslt = myStmt.executeQuery("SELECT title ,Subject\r\n" + 
					"FROM book \r\n" + 
					"WHERE Subject like '" +subjString+"%'");
			return myRslt;	 
	 }
	 //Get a list of all books written by a particular author
	 public ResultSet queryBookByAuthor(String firstname,String lastName) throws SQLException {		
		
		 myPreparedStatement = myConn.prepareStatement("SELECT DISTINCT b.title,b.Edition_Number\r\n" + 
			 		"FROM book AS b\r\n" + 
			 		"INNER JOIN book_author AS ba\r\n" + 
			 		"ON b.BookID =ba.Book_BookID\r\n" + 
			 		"INNER JOIN author AS a\r\n" + 
			 		"ON a.AuthorID = ba.Author_AuthorID\r\n" + 
			 		"WHERE a.First_name = ? AND a.Last_name = ?");
		 myPreparedStatement.setString(1, firstname);
		 myPreparedStatement.setString(2, lastName);
		 myRslt = myPreparedStatement.executeQuery();
			return myRslt;	 
	 }
	
	 //Get all authors
	 public ResultSet queryAllAuthors()throws SQLException{
			myRslt = myStmt.executeQuery("SELECT last_name,first_name FROM Author");
			return myRslt;	 
	 }
	 //Get all subjects
	 public ResultSet queryAllSubjects()throws SQLException{
			myRslt = myStmt.executeQuery("SELECT DISTINCT Subject\r\n" + 
					"FROM book ");
			return myRslt;	 
	 }
	 	 
	 //Get a list of all borrowers
	 public ResultSet queryAllBorrowers() throws SQLException {
		 myRslt = myStmt.executeQuery("SELECT First_Name, Last_Name,Borrower_email\r\n" + 
		 		                        "FROM borrower ");
			return myRslt;	 
		 
	 }
	
	 public int addBorrower (String lastName, String firstName, String email)throws SQLException{
	  
		 myPreparedStatement =myConn.prepareStatement("INSERT INTO borrower (Last_Name,First_Name, Borrower_email)  VALUES  (?,?,?) ");
		 myPreparedStatement.setString(1, lastName);
		 myPreparedStatement.setString(2, firstName);
		 myPreparedStatement.setString(3, email);
			
					
			//Step 4: pass INSERT String to method executeUpdate()
			int returnedValue = myPreparedStatement.executeUpdate();
		   return returnedValue;
	 }
	 
	 public int updateBorrower(String lastName, String firstName, String email) throws SQLException 
	 {
		 myPreparedStatement =myConn.prepareStatement("UPDATE borrower\r\n" + 
		 		"SET Borrower_email = ?\r\n" + 
		 		"WHERE First_Name = ?AND Last_Name =?;\r\n" + 
		 		"");
		 myPreparedStatement.setString(1, email);
		 myPreparedStatement.setString(3, lastName);
		 myPreparedStatement.setString(2, firstName);
		 int returnedValue = myPreparedStatement.executeUpdate();
		   return returnedValue;
	 }
	 
	 public void addBook(String titleString,	String isbnString ,String editionNumString,	String subjectString,String lastNameString,String firstNameString) throws SQLException {
		 //add a author
		 myStmt.executeUpdate("INSERT INTO author\r\n" + 
		 		"(`Last_Name`,`First_name`)\r\n" + 
		 		"VALUES( '" +lastNameString+"', '"+firstNameString+"' );");
		 
		 myPreparedStatement =myConn.prepareStatement("INSERT INTO book\r\n" + 
			 		"(`Title`,`ISBN`,`Edition_Number`,`Subject`,`Available`)\r\n" + 
			 		"VALUES(?,?,?,?,Available);"); 
		 myPreparedStatement.setString(1, titleString);
		 myPreparedStatement.setString(2, isbnString);
		 myPreparedStatement.setString(3, editionNumString);
		 myPreparedStatement.setString(4, subjectString);
		 myPreparedStatement.executeUpdate();
		 
		 myPreparedStatement =myConn.prepareStatement("INSERT INTO book_author\r\n" + 
			 		"(Book_BookID,Author_AuthorID)VALUES((SELECT BookID FROM book WHERE Title =? ),(SELECT AuthorID FROM author WHERE Last_Name =? AND First_name =?));");
		 myPreparedStatement.setString(1, titleString);
		 myPreparedStatement.setString(2, lastNameString);
		 myPreparedStatement.setString(3, firstNameString);
		 myPreparedStatement.executeUpdate();
		 
		
	 }
	 
	 public void addMoreAuthor (String titleString,String lastNameString,String firstNameString)throws Exception {
		 myStmt.executeUpdate("INSERT INTO author\r\n" + 
			 		"(`Last_Name`,`First_name`)\r\n" + 
			 		"VALUES( '" +lastNameString+"', '"+firstNameString+"' );");
		 myPreparedStatement =myConn.prepareStatement("INSERT INTO book_author\r\n" + 
			 		"(Book_BookID,Author_AuthorID)VALUES((SELECT BookID FROM book WHERE Title =? ),(SELECT AuthorID FROM author WHERE Last_Name =? AND First_name =?));");
		 myPreparedStatement.setString(1, titleString);
		 myPreparedStatement.setString(2, lastNameString);
		 myPreparedStatement.setString(3, firstNameString);
		 myPreparedStatement.executeUpdate();
	 } 
	 
	 
	 public void bookLoan(String titleString, String comment,String lastName, String firstName,String dateDueString)throws Exception {
		 myPreparedStatement =myConn.prepareStatement("INSERT INTO book_loan(Book_BookID,Borrower_Borrower_ID,comment,Date_out,Date_due)\r\n" + 
		 		"VALUES((SELECT BookID FROM book WHERE Title =? AND Available =1),(SELECT Borrower_ID FROM borrower WHERE First_Name =? AND Last_Name =?),?, sysdate(),?);");
		 myPreparedStatement.setString(1, titleString);
		 myPreparedStatement.setString(2, firstName);
		 myPreparedStatement.setString(3, lastName);
		 myPreparedStatement.setString(4, comment);
		 myPreparedStatement.setString(5, dateDueString);
		 myPreparedStatement.executeUpdate();
		 myPreparedStatement =myConn.prepareStatement("UPDATE book SET Available = 0 WHERE Title =? AND Available =1;");
		 myPreparedStatement.setString(1, titleString);
		 myPreparedStatement.executeUpdate();
		
	 }
	 
	 public void bookReturn(String title,String firstnameString, String lastName)throws Exception {
		 int bookID=0, borrowerID=0 ;
	
		 myPreparedStatement =myConn.prepareStatement("SELECT BookID FROM book WHERE Title =? AND Available =0; ");
		 myPreparedStatement.setString(1, title);
		 myRslt = myPreparedStatement.executeQuery();
		 myRslt.next();
			 bookID = myRslt.getInt("BookID");
		
	      myRslt  =myStmt.executeQuery("SELECT Borrower_ID FROM borrower WHERE First_Name ='"+firstnameString+"' AND Last_Name ='"+lastName+"' ");
	      myRslt.next();
			 borrowerID = myRslt.getInt("Borrower_ID");
			 
			myStmt.executeUpdate("UPDATE book_loan SET Date_returned =sysdate() WHERE Book_BookID = "+bookID+"  AND Borrower_Borrower_ID =" +borrowerID+";");
		
			 myPreparedStatement =myConn.prepareStatement("UPDATE book SET Available = 1 WHERE Title =? AND Available =0;");
			 myPreparedStatement.setString(1, title);
			 myPreparedStatement.executeUpdate();
		
		}
	 }

//end class