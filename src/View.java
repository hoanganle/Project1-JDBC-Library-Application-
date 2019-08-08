/**
 *Program:View.java
 *Purpose:
 *Author:Gang
 *Date:Jul. 27, 2019
 *
 */
import javax.swing.*;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class View extends JFrame {
   Controller controller;
	TableView tableView;
	JTabbedPane t1; //main tab panel
	JTabbedPane jTabP; 
	JPanel p2;	

	JButton addBookButton;
	JRadioButton rb1,rb2,rb3,rb4,rb5;
	JComboBox<String> jc1, jc2;
	JButton jb1;
	 DefaultComboBoxModel<String> authorRsltComBox,subjectRsltComBox,borrowerRsltComBox;
	JTextField lastNameTextField,firstNameTextFieldtext,emailTextField;
   JRadioButton addBorroweRadioButton ,updateBorroweRadioButton;
  	
   JTextField titleTextField,isbnJTextField,editionNumJTextField,subjecTextField,authorLastNameJTextField,authorFirstNameJTextField;
   JLabel BorrowMessagJLabel,bookMessagJLabel;
   JCheckBox addMoreAuthourCheckBox;
	public View () throws SQLException {
		super("Gui");//will go into title bar		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//object becomes garbage when closed
		this.setSize(450, 400);//width and height of the JFrame in PIXELS
		this.setLocationRelativeTo(null);//centres our JFrame on the screen	
		controller =new Controller();
	  //main tab panel
		 t1=new JTabbedPane();
	  
	   
	  
//set search book panel
	    //set comboBox to show all authors
	    p2=new JPanel();
	    queryAllAuthors();
	    queryAllSubject();
	    rb1=new JRadioButton("All books in the library");
	    rb1.setSelected(true);
	    rb1.addItemListener((e)->{
	   	 jc2.setVisible(false);
	   	 jc1.setVisible(false);

	    });
	    rb2=new JRadioButton("All books on loan and the borrower");
	    rb2.addItemListener((e)->{
	   	 jc2.setVisible(false);
	   	 jc1.setVisible(false);
	    });
	    rb3=new JRadioButton("Books by subject");
	    rb3.addItemListener((e)->{
	   	 jc2.setVisible(false);
	   	 jc1.setVisible(true);
	    });
	    rb4=new JRadioButton("Books by author");
	    rb4.addItemListener((e)->{
	   	 jc2.setVisible(true);
	   	 jc1.setVisible(false);
	    });
	    
	    rb5=new JRadioButton("All borrowers");
	    rb5.addItemListener((e)->{
	   	 jc2.setVisible(false);
	   	 jc1.setVisible(false);
	    });
	    jc1 = new JComboBox<>(subjectRsltComBox);
	    jc1.setVisible(false);
	    jc2 =new JComboBox<>(authorRsltComBox);
	    jc2.setVisible(false);
	    jb1 = new JButton("View");
	  
	 
	    ButtonGroup bg=new ButtonGroup();  
	    bg.add(rb1);
	    bg.add(rb2);
	    bg.add(rb3);
	    bg.add(rb4);
       bg.add(rb5);
       
       p2.setLayout(new GridLayout(6, 2));
	    p2.add(rb1);
	    p2.add(new JLabel(""));
	    p2.add(rb2);
	    p2.add(new JLabel(""));
	    p2.add(rb3);
	    p2.add(jc1);
	    p2.add(rb4);
	    p2.add(jc2);
       jb1.addActionListener(new viewButtonListener());		 
		 p2.add(rb5);
	    p2.add(new JLabel(""));
       p2.add(jb1);       
       t1.addTab("Search Books", p2);
	   
  //set tab panel for updating books and borrowers
     jTabP = new JTabbedPane(JTabbedPane.LEFT);
     t1.addTab("Add",jTabP);

     JPanel borrowerPanel=new JPanel();
     borrowerPanel.setLayout(new GridLayout(6,2));
    
     //set combo box 
     queryAllBorrower();
     
	  //Set all components
      addBorroweRadioButton = new JRadioButton("Add a new borrower");   
     addBorroweRadioButton.setSelected(true);
      updateBorroweRadioButton =new JRadioButton("Update borrower");
     JComboBox<String> borrowerComboBox =new JComboBox<String>(borrowerRsltComBox);
     borrowerComboBox.setVisible(false);
     
     JLabel   lastNameJLabel = new JLabel("Last Name:");
     JLabel firstNameJLabel = new JLabel("First Name:");
     JLabel emaiLabel = new JLabel("Email:");
     
     lastNameTextField = new JTextField();
     firstNameTextFieldtext =new JTextField();
     emailTextField = new JTextField();
     
     JButton  borrowButton =new JButton("Add");
   
     ButtonGroup bgBorrower=new ButtonGroup();  
     bgBorrower.add(addBorroweRadioButton);
     bgBorrower.add(updateBorroweRadioButton);
    
     //Add listener for radio button
     addBorroweRadioButton.addItemListener((e)->{
   	 BorrowMessagJLabel.setText("");
        borrowerComboBox.setVisible(false);
        lastNameTextField.setEditable(true);
   	  firstNameTextFieldtext.setEditable(true);
   	  borrowButton.setText("Add");
   	  lastNameTextField.setText("");
	   	firstNameTextFieldtext.setText("");
     });
     
     updateBorroweRadioButton.addItemListener((e)->{
   	  BorrowMessagJLabel.setText("");
   	  borrowerComboBox.setVisible(true);
   	  lastNameTextField.setEditable(false);
   	  firstNameTextFieldtext.setEditable(false);
   	  borrowButton.setText("Update");
   	  String nameString = borrowerComboBox.getSelectedItem().toString();
	   	String []nameStrings = nameString.split(",");
	   	String lastName = nameStrings[0];
	   	String firstName = nameStrings[1];
	   	lastNameTextField.setText(lastName);
	   	firstNameTextFieldtext.setText(firstName);
     });
     
     borrowerComboBox.addItemListener((e)->{
   	  
   	  String nameString = borrowerComboBox.getSelectedItem().toString();
	   	String []nameStrings = nameString.split(",");
	   	String lastName = nameStrings[0];
	   	String firstName = nameStrings[1];
	   	lastNameTextField.setText(lastName);
	   	firstNameTextFieldtext.setText(firstName);
     });
     
     //Add components to panel
     borrowerPanel.add(addBorroweRadioButton);
     borrowerPanel.add(new JLabel(""));
     borrowerPanel.add(updateBorroweRadioButton);
     borrowerPanel.add(borrowerComboBox);
     borrowerPanel.add(lastNameJLabel);
     borrowerPanel.add(lastNameTextField);
     borrowerPanel.add(firstNameJLabel); 
     borrowerPanel.add(firstNameTextFieldtext);
     borrowerPanel.add(emaiLabel);
     borrowerPanel.add(emailTextField);
     borrowerPanel.add(borrowButton);
     	    
     //set action listener for button
     borrowButton.addActionListener(new borrowButtonListener());
     
     JPanel borrowerMainPanel =new JPanel();
     borrowerMainPanel.setLayout(new BorderLayout());
     
     borrowerMainPanel.add(borrowerPanel,BorderLayout.CENTER);
     BorrowMessagJLabel =new JLabel(" ");
     BorrowMessagJLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
     borrowerMainPanel.add(BorrowMessagJLabel,BorderLayout.SOUTH);
     jTabP.addTab("Borrower", borrowerMainPanel);
	      
     
 //set book panel
     JPanel  bookJPanel = new JPanel();
     bookJPanel.setLayout(new GridLayout(7, 2));
     
     JLabel titleJLabel =new JLabel("Title:");
      titleTextField =new JTextField();
     JLabel isbnJLabel =new JLabel("ISBN:");
      isbnJTextField =new JTextField();
     JLabel editionNumJLabel =new JLabel("Edition Number:");
      editionNumJTextField =new JTextField();
     JLabel subjectJLabel =new JLabel("Subject:");
      subjecTextField =new JTextField();
     JLabel authorLastNameJLabel =new JLabel("Authour Last Name:");
      authorLastNameJTextField =new JTextField();
     JLabel authorFirstNameJLabel =new JLabel("Authour First Name:");
      authorFirstNameJTextField =new JTextField();
      addBookButton = new JButton("Add");
     addBookButton.addActionListener(new bookButtonListener() );
      addMoreAuthourCheckBox = new JCheckBox("Add More Authors");
      
      addMoreAuthourCheckBox.addItemListener((e)->{
	   	 if(!addMoreAuthourCheckBox.isSelected()) {
	   		 titleTextField.setVisible(true);
					isbnJTextField.setVisible(true);
					editionNumJTextField.setVisible(true);
					subjecTextField.setVisible(true);
	   		   bookMessagJLabel.setText("");
	   		   
				   titleTextField.setText("");
				   isbnJTextField.setText("");
				   editionNumJTextField.setText("");
				   subjecTextField.setText("");
				   authorLastNameJTextField.setText("");
				   authorFirstNameJTextField.setText("");
				   addBookButton.setText("Add");
	   	 }
	    });
      
     bookJPanel.add(titleJLabel);
     bookJPanel.add(titleTextField);
     bookJPanel.add(isbnJLabel);
     bookJPanel.add(isbnJTextField);
     bookJPanel.add(editionNumJLabel);
     bookJPanel.add(editionNumJTextField);
     bookJPanel.add(subjectJLabel);
     bookJPanel.add(subjecTextField);
     bookJPanel.add(authorLastNameJLabel);
     bookJPanel.add(authorLastNameJTextField);
     bookJPanel.add(authorFirstNameJLabel);
     bookJPanel.add(authorFirstNameJTextField);
     bookJPanel.add(addBookButton);
     bookJPanel.add(addMoreAuthourCheckBox);
     
     JPanel bookMainPanel =new JPanel();
     bookMainPanel.setLayout(new BorderLayout());
     
     bookMainPanel.add(bookJPanel,BorderLayout.CENTER);
     bookMessagJLabel =new JLabel(" ");
     bookMessagJLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
     bookMainPanel.add(bookMessagJLabel,BorderLayout.SOUTH);
     jTabP.addTab("Book", bookMainPanel);
	
   //set panel for book loan    
     
     JPanel bookLoanJPanel=new JPanel(new BorderLayout());  
     t1.addTab("Loan",bookLoanJPanel); 
	  // 
	  JTable booklisTable =new JTable(queryBooksInLibrary());   
	  JScrollPane bookListJPanel =new JScrollPane(booklisTable); 
	  bookListJPanel.setPreferredSize(new Dimension(0,150));
	  bookLoanJPanel.add(bookListJPanel,BorderLayout.NORTH);
	  JPanel bookFormJPanel =new JPanel(new GridLayout(5,2));
	 
	  JLabel borrererJLabel = new JLabel("Borrower:");
	  JComboBox<String> borrowListBox =new JComboBox<String>(borrowerRsltComBox);
	 
	  JLabel comnentJLabel = new JLabel("Comment:");
	  JTextField commenTextField = new JTextField();
	  JLabel dateJLabel = new JLabel("Date Out:");  
	  //get current date
	  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
	  LocalDateTime now = LocalDateTime.now();
	  JLabel dateJLabel2 =new JLabel(dtf.format(now)); 
	  
	  //due date
	  JLabel dateDueJLabel = new JLabel("Date Due:");	 
	  JLabel datedueJLabel2 = new JLabel(dtf.format( now.plusDays(15)));
	  JButton submitButton =new JButton("Submit");
	  
	  
	  bookFormJPanel.add(borrererJLabel);
	  bookFormJPanel.add(borrowListBox);
	  bookFormJPanel.add(comnentJLabel);
	  bookFormJPanel.add(commenTextField);
	  bookFormJPanel.add(dateJLabel);
	  bookFormJPanel.add(dateJLabel2);
	  bookFormJPanel.add(dateDueJLabel);
	  bookFormJPanel.add(datedueJLabel2);
	  bookFormJPanel.add(submitButton);
	  bookLoanJPanel.add(bookFormJPanel, BorderLayout.CENTER);
	  
	  JLabel  bookLoanMessageJLabel = new JLabel("Message:");
	  bookLoanMessageJLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
	  bookLoanJPanel.add(new JPanel().add(bookLoanMessageJLabel),BorderLayout.SOUTH);
	  
	  
	  submitButton.addActionListener((e)->{
		  try {
		  int rowIndex = booklisTable.getSelectedRow();
		 String title = (String) booklisTable.getValueAt(rowIndex, 0);
		 
		 String commentString  = commenTextField.getText();
		 String nameString = borrowListBox.getSelectedItem().toString();
	   	String []nameStrings = nameString.split(",");
	   	String lastName = nameStrings[0];
	   	String firstName = nameStrings[1];
		   controller.bookLoan(title,commentString,lastName,firstName);
		   commenTextField.setText("");
		  
		   bookLoanMessageJLabel.setText(lastName +" "+firstName+" borrowed "+title);
		   
		   //update book list
		   TableModel tableModel =queryBooksInLibrary();
		   booklisTable.setModel(tableModel);
		  }
			catch(SQLException ex)
			{
				System.out.println("SQL Exception, message is: " + ex.getMessage());
			}
			catch(Exception ex)
			{
				System.out.println("Some other Exception, message is: " + ex.getMessage());
			}
	  });
	  
	    //add main tab panel to JFrame
	    this.add(t1);
	    this.setVisible(true);
	}
	
	//query Books In Library
	private  TableModel queryBooksInLibrary()throws SQLException{
		 
  	 ResultSet resultSet =controller.queryBooksInLibrary();
  	  Model model =  new Model(resultSet);
	   
    	return  model.getTableModel();
		
	}
	//query all borrower
	private void queryAllBorrower()throws SQLException{
		 ResultSet borrResultSetRslt = new Controller().queryAllBorrowers();
		  Model borrowerRsltmodel = new Model(borrResultSetRslt);
		  borrowerRsltComBox = borrowerRsltmodel.getComboBoxModel("Last_name","first_Name");	
	}

	//query all authors
	private void queryAllAuthors() throws SQLException{
		ResultSet authorRslt = controller.queryAllAuthors();
	   Model authorRsltmodel = new Model(authorRslt);
	   authorRsltComBox = authorRsltmodel.getComboBoxModel("Last_name","first_Name");
	}   
   
	//set ComboBox to show all subjects
	private void queryAllSubject()throws SQLException{
		  ResultSet subjectRslt = controller.queryAllSubjects();
		    Model subjectRsltmodel = new Model(subjectRslt);	   
		    subjectRsltComBox = subjectRsltmodel.getComboBoxModel("Subject","");
	} 
    
	//viewButtonListener
	 private class viewButtonListener  implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
		    if(rb1.isSelected()) {
		   	TableModel tableModel =queryBooksInLibrary();
		   	if(tableView ==null)
		   		tableView =new TableView(tableModel);
				 	
				 	else {
				 		tableView.show();
				 		tableView.jTable.setModel(tableModel);
				 	}
		    }
		    
		    else if(rb2.isSelected()) {
		   	 ResultSet resultSet = controller.queryBooksOnLoan();
		   	  Model model =  new Model(resultSet);
		 	   
		   	TableModel tableModel =model.getTableModel();
		   	if(tableView ==null)
		   		tableView =new TableView(tableModel);
				 	
				 	else {
				 		tableView.show();
				 		tableView.jTable.setModel(tableModel);
				 	}
		    }
			
		    else if(rb3.isSelected()) {
		   		String subjectString = jc1.getSelectedItem().toString();
			   	
			   	
			   	ResultSet resultSet =controller.queryBookBySubject(subjectString);
			   	  Model model =  new Model(resultSet);
			 	   
			   	TableModel tableModel =model.getTableModel();
			   	if(tableView ==null)
			   		tableView =new TableView(tableModel);
					 	
					 	else {
					 		tableView.show();
					 		tableView.jTable.setModel(tableModel);
					 	}
		    }

		    
			else if(rb4.isSelected()) {								
				String nameString = jc2.getSelectedItem().toString();
		   	String []nameStrings = nameString.split(",");
		   	String lastName = nameStrings[0];
		   	String firstName = nameStrings[1];
		   	
		   	ResultSet resultSet =controller.queryBookByAuthor(firstName,lastName);
		   	  Model model =  new Model(resultSet);
		 	   
		   	TableModel tableModel =model.getTableModel();
		   	if(tableView ==null)
		   		tableView =new TableView(tableModel);
				 	
				 	else {
				 		tableView.show();
				 		tableView.jTable.setModel(tableModel);
				 	}
			}
			
			else if(rb5.isSelected()) {
				 ResultSet resultSet = controller.queryAllBorrowers();
		   	  Model model =  new Model(resultSet);
		 	   
		   	TableModel tableModel =model.getTableModel();
		   	if(tableView ==null)
		   		tableView =new TableView(tableModel);
				 	
				 	else {
				 		tableView.show();
				 		tableView.jTable.setModel(tableModel);
				 	}
				
			}
		}//end try
			catch(SQLException ex)
			{
				System.out.println("SQL Exception, message is: " + ex.getMessage());
			}
			catch(Exception ex)
			{
				System.out.println("Some other Exception, message is: " + ex.getMessage());
			}
		}//end 
	 }// end method 

	 //borrow Button Listener 
	 private class borrowButtonListener  implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
			if(addBorroweRadioButton.isSelected()) {
				BorrowMessagJLabel.setText("");
		   String lastName =	lastNameTextField.getText();
			String fristName =firstNameTextFieldtext.getText();
			String email = emailTextField.getText();
			
			int returnedValue =controller.addBorrower(lastName, fristName, email);
			BorrowMessagJLabel.setText("Add completed, "+lastName+" "+fristName+" Added.");
			//clear text field
			lastNameTextField.setText("");
			firstNameTextFieldtext.setText("");
			emailTextField.setText("");
			
			if(tableView.isShowing()) {
				 ResultSet resultSet = controller.queryAllBorrowers();
		   	 Model model =  new Model(resultSet);	 	   
		   	 TableModel tableModel =model.getTableModel();
		   	 tableView.jTable.setModel(tableModel);
			}
		
			}
			
			
			else if (updateBorroweRadioButton.isSelected()) {		
				
			   String lastName =	lastNameTextField.getText();
				String fristName =firstNameTextFieldtext.getText();
				String email = emailTextField.getText();
				
				int returnedValue =controller.updateBorrower(lastName, fristName, email);
				BorrowMessagJLabel.setText("Update completed, "+lastName+"'s new email is "+ email);
				emailTextField.setText("");
				if(tableView.isShowing()) {
					
					 ResultSet resultSet = controller.queryAllBorrowers();
			   	 Model model =  new Model(resultSet);
			 	   
			   	 TableModel tableModel =model.getTableModel();
			   	 tableView.jTable.setModel(tableModel);
				}
					
				
			}
				
			}//end try
			catch(SQLException ex)
			{
				System.out.println("SQL Exception, message is: " + ex.getMessage());
			}
			catch(Exception ex)
			{
				System.out.println("Some other Exception, message is: " + ex.getMessage());
			}
			
		}}//end method
	 //book button listener
	 private class bookButtonListener  implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String titleString=   titleTextField.getText();
				String isbnString =   isbnJTextField.getText();
				String editionNumString =   editionNumJTextField.getText();
				String subjectString=   subjecTextField.getText();
				String lastNameString=   authorLastNameJTextField.getText();
				String firstNameString=   authorFirstNameJTextField.getText();
				
			if	(e.getActionCommand()=="Add more authors" &&addMoreAuthourCheckBox.isSelected()) {
	         controller.addMoreAuthor(titleString,lastNameString,firstNameString);
				
			   bookMessagJLabel.setText(lastNameString+" added.");
			   authorLastNameJTextField.setText("");
			   authorFirstNameJTextField.setText("");
				
			}
			else if(addMoreAuthourCheckBox.isSelected()) {
				controller.addBook(titleString,isbnString,editionNumString,subjectString,lastNameString,firstNameString);
				 bookMessagJLabel.setText(titleString+" added.");
				titleTextField.setVisible(false);
				isbnJTextField.setVisible(false);
				editionNumJTextField.setVisible(false);
				subjecTextField.setVisible(false);
			   
				addBookButton.setText("Add more authors");
				 authorLastNameJTextField.setText("");
				   authorFirstNameJTextField.setText("");
			}
			
			else {
			
				controller.addBook(titleString,isbnString,editionNumString,subjectString,lastNameString,firstNameString);
				
			   bookMessagJLabel.setText(titleString+" added.");
			   
			   titleTextField.setText("");
			   isbnJTextField.setText("");
			   editionNumJTextField.setText("");
			   subjecTextField.setText("");
			   authorLastNameJTextField.setText("");
			   authorFirstNameJTextField.setText("");
			}
			if(tableView.isShowing()) {
				TableModel tableModel =queryBooksInLibrary();
				tableView.jTable.setModel(tableModel);
			}
		
			  queryAllAuthors();
			  queryAllSubject();
			    jc1.setModel(subjectRsltComBox); //update  combo box
			    jc2.setModel(authorRsltComBox); //update  combo box
			}
			catch(SQLException ex)
			{
				System.out.println("SQL Exception, message is: " + ex.getMessage());
			}
			catch(Exception ex)
			{
				System.out.println("Some other Exception, message is: " + ex.getMessage());
			}
			
		}
		 
	 }
	 
	 
	 
	 public static void main(String[] args) {
		try {
			 View view =new View();
			
		}
		catch(SQLException ex)
		{
			System.out.println("SQL Exception, message is: " + ex.getMessage());
		}
		catch(Exception ex)
		{
			System.out.println("Some other Exception, message is: " + ex.getMessage());
		}
	}

}
//end class