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
import java.util.regex.Pattern;
public class View extends JFrame  implements ActionListener{
   Controller controller;
	TableView tableView;
	JTabbedPane t1; //main tab panel
	JTabbedPane jTabP; 
	JPanel p2;	
	JTable returnbookJTable ,booklisTable ;
	JButton addBookButton;
	JRadioButton rb1,rb2,rb3,rb4,rb5;
	JComboBox<String> jc1, jc2 ,borrowListBox,borrowerComboBox;
	JButton jb1;
	 DefaultComboBoxModel<String> authorRsltComBox,subjectRsltComBox,borrowerRsltComBox,borrowerRsltComBox2;
	JTextField lastNameTextField,firstNameTextFieldtext,emailTextField,commenTextField;
   JRadioButton addBorroweRadioButton ,updateBorroweRadioButton;
  	
   JTextField titleTextField,isbnJTextField,editionNumJTextField,subjecTextField,authorLastNameJTextField,authorFirstNameJTextField;
   JLabel BorrowMessagJLabel,bookMessagJLabel,  returnMessJLabel,bookLoanMessageJLabel,datedueJLabel2;
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
	    JButton closeButton3 = new JButton("Close");
	    closeButton3.addActionListener(this);
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
       p2.add(closeButton3);
       t1.addTab("Search Books", p2);
	   
  //set tab panel for updating books and borrowers
     jTabP = new JTabbedPane(JTabbedPane.LEFT);
     t1.addTab("Add",jTabP);

     JPanel borrowerPanel=new JPanel();
     borrowerPanel.setLayout(new GridLayout(6,2));
    
     //set combo box 
     borrowerRsltComBox = queryAllBorrower();
     
	  //Set all components
      addBorroweRadioButton = new JRadioButton("Add a new borrower");   
     addBorroweRadioButton.setSelected(true);
      updateBorroweRadioButton =new JRadioButton("Update borrower");
      borrowerComboBox =new JComboBox<String>(borrowerRsltComBox);
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
   	 BorrowMessagJLabel.setText("Message:");
        borrowerComboBox.setVisible(false);
        lastNameTextField.setEditable(true);
   	  firstNameTextFieldtext.setEditable(true);
   	  borrowButton.setText("Add");
   	  lastNameTextField.setText("");
	   	firstNameTextFieldtext.setText("");
     });
     
     updateBorroweRadioButton.addItemListener((e)->{
   	  BorrowMessagJLabel.setText("Message:");
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
     borrowerComboBox.addActionListener((e)->{
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
     JButton closeBtn = new JButton("Close");
     closeBtn.addActionListener(this);
     borrowerPanel.add(closeBtn);
     //set action listener for button
     borrowButton.addActionListener(new borrowButtonListener());
     
     JPanel borrowerMainPanel =new JPanel();
     borrowerMainPanel.setLayout(new BorderLayout());
     
     borrowerMainPanel.add(borrowerPanel,BorderLayout.CENTER);
     BorrowMessagJLabel =new JLabel("Message:");
     BorrowMessagJLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
     borrowerMainPanel.add(BorrowMessagJLabel,BorderLayout.SOUTH);
     jTabP.addTab("Borrower", borrowerMainPanel);
	      
     
 //set book panel
     JPanel  bookJPanel = new JPanel();
     bookJPanel.setLayout(new GridLayout(8, 2));
     
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
      JLabel instructions = new JLabel("click check box first if adding more authors");
      addBookButton = new JButton("Add");
     addBookButton.addActionListener(new bookButtonListener() );
      addMoreAuthourCheckBox = new JCheckBox("Add More Authors");
      JButton closeButton5 = new JButton("Close");
      closeButton5.addActionListener(this);
      addMoreAuthourCheckBox.addItemListener((e)->{
	   	 if(!addMoreAuthourCheckBox.isSelected()) {
	   		 titleTextField.setVisible(true);
					isbnJTextField.setVisible(true);
					editionNumJTextField.setVisible(true);
					subjecTextField.setVisible(true);
	   		   bookMessagJLabel.setText("Message:");
	   		   
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
     bookJPanel.add(addMoreAuthourCheckBox);
     bookJPanel.add(instructions);
     bookJPanel.add(addBookButton);
     bookJPanel.add(closeButton5);
     
     JPanel bookMainPanel =new JPanel();
     bookMainPanel.setLayout(new BorderLayout());
     
     bookMainPanel.add(bookJPanel,BorderLayout.CENTER);
     bookMessagJLabel =new JLabel("Message:");
     bookMessagJLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
     bookMainPanel.add(bookMessagJLabel,BorderLayout.SOUTH);
     jTabP.addTab("Book", bookMainPanel);
	
   //set panel for book loan    
     
     JPanel bookLoanJPanel=new JPanel(new BorderLayout());  
     t1.addTab("Loan",bookLoanJPanel); 
	  // 
	   booklisTable =new JTable(queryBooksInLibrary());   
	  JScrollPane bookListJPanel =new JScrollPane(booklisTable); 
	  bookListJPanel.setPreferredSize(new Dimension(0,120));
	  bookLoanJPanel.add(bookListJPanel,BorderLayout.NORTH);
	  JPanel bookFormJPanel =new JPanel(new GridLayout(6,2));
	 
	  JLabel borrererJLabel = new JLabel("Borrower:");
	  borrowerRsltComBox2 = queryAllBorrower();
	  borrowListBox =new JComboBox<String>(borrowerRsltComBox2);
	 
	  JLabel comnentJLabel = new JLabel("Comment:");
	   commenTextField = new JTextField();
	  JLabel dateJLabel = new JLabel("Date Out:");  
	  //get current date
	  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
	  LocalDateTime now = LocalDateTime.now();
	  JLabel dateJLabel2 =new JLabel(dtf.format(now)); 
	  
	  //due date
	  JRadioButton oneWeekButton =new JRadioButton("One week");
	  JRadioButton twoWeekButton =new JRadioButton("Two week");
	  twoWeekButton.setSelected(true);
	  ButtonGroup bg2  =new ButtonGroup();
	  bg2.add(oneWeekButton);
	  bg2.add(twoWeekButton);
	  
	  JLabel dateDueJLabel = new JLabel("Date Due:");	 
	   datedueJLabel2 = new JLabel(dtf.format( now.plusDays(14)));
	  JButton submitButton =new JButton("Submit");
	  JButton closeButton = new JButton("Close");
	  closeButton.addActionListener(this);
	  // 
	  oneWeekButton.addItemListener((e)->{
		  datedueJLabel2.setText(dtf.format( now.plusDays(7)));
	  });
	  twoWeekButton.addItemListener((e)->{
		  datedueJLabel2.setText(dtf.format( now.plusDays(14)));
	  });
	  
	  bookFormJPanel.add(borrererJLabel);
	  bookFormJPanel.add(borrowListBox);
	  bookFormJPanel.add(comnentJLabel);
	  bookFormJPanel.add(commenTextField);
	  bookFormJPanel.add(dateJLabel);
	  bookFormJPanel.add(dateJLabel2);
	  bookFormJPanel.add(oneWeekButton);
	  bookFormJPanel.add(twoWeekButton);
	  bookFormJPanel.add(dateDueJLabel);
	  bookFormJPanel.add(datedueJLabel2);
	  bookFormJPanel.add(submitButton);
	 
	  bookFormJPanel.add(closeButton);
	  
	  bookLoanJPanel.add(bookFormJPanel, BorderLayout.CENTER);
	  
	    bookLoanMessageJLabel = new JLabel("Message:");
	  bookLoanMessageJLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
	  bookLoanJPanel.add(new JPanel().add(bookLoanMessageJLabel),BorderLayout.SOUTH);
	  
	  
	  submitButton.addActionListener(new bookLoanListener());
	  
	  //Set panel for returning books 
	  JPanel returnBookJPanel = new JPanel(new BorderLayout());
	  t1.add("Return", returnBookJPanel);
  
	  returnbookJTable = new JTable( new Model( controller.queryBooksOnLoan()).getTableModel());
	  JScrollPane returnbookJScrollPane =new JScrollPane(returnbookJTable);
	  returnbookJScrollPane.setPreferredSize(new Dimension(0,220));
	  returnBookJPanel.add(returnbookJScrollPane,BorderLayout.NORTH);
	  JPanel returnBookCenterJPanel = new JPanel(new GridLayout(2,2));
	  
	 
	  JLabel dateReturnJLabel = new JLabel("Date Returned:");
	  JLabel dateReturnShowJLabel = new JLabel(dtf.format(now));  
	  JButton returnButton = new JButton("Return");
	  returnButton.addActionListener(new bookReturnListener());
	  JButton closeButton2 = new JButton("Close");
	  closeButton2.addActionListener(this);
	  returnBookCenterJPanel.add(dateReturnJLabel);
	  returnBookCenterJPanel.add(dateReturnShowJLabel);
	  returnBookCenterJPanel.add(returnButton);
	  returnBookCenterJPanel.add(closeButton2);
	  returnBookJPanel.add(returnBookCenterJPanel, BorderLayout.CENTER);
	   returnMessJLabel = new JLabel("Message:");
	  returnMessJLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
	  returnBookJPanel.add(new JPanel().add(returnMessJLabel),BorderLayout.SOUTH); 
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
	//Get  borrower combo box string
	private DefaultComboBoxModel<String> queryAllBorrower()throws SQLException{
		 ResultSet borrResultSetRslt = new Controller().queryAllBorrowers();
		  Model borrowerRsltmodel = new Model(borrResultSetRslt);
		  return  borrowerRsltmodel.getComboBoxModel("Last_name","first_Name");	
	}

	//Get  authors combJbox string 
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
				 
			String lastName =	lastNameTextField.getText();				
			String fristName =firstNameTextFieldtext.getText();				
			String email = emailTextField.getText();
					
			//validate 				 
			if(lastName.trim().isEmpty() )
					 {					 
						 throw new EmptyStringException(1);
					 }
						 				 
			else if( fristName.trim().isEmpty())
					 {						 
						 throw new EmptyStringException(2);
					 }
						 				
			else if( email.trim().isEmpty())
					 {					 
						 throw new EmptyStringException(3);
					 }

					
			if(addBorroweRadioButton.isSelected()) {
				BorrowMessagJLabel.setText("");			
				int returnedValue =controller.addBorrower(lastName, fristName, email);			
				BorrowMessagJLabel.setText("Add completed, "+lastName+" "+fristName+" Added.");			
				//clear text field			
				lastNameTextField.setText("");		
				firstNameTextFieldtext.setText("");		
				emailTextField.setText("");
			}
			
			
			else if (updateBorroweRadioButton.isSelected()) {						
				int returnedValue =controller.updateBorrower(lastName, fristName, email);
				BorrowMessagJLabel.setText("Update completed, "+lastName+"'s new email is "+ email);
				emailTextField.setText("");		
			}
				
			  borrowerRsltComBox = queryAllBorrower();
		     borrowerRsltComBox2 = queryAllBorrower();
	        borrowListBox.setModel(borrowerRsltComBox2);
		     borrowerComboBox.setModel(borrowerRsltComBox);
			
		     if(tableView.isShowing()) {				
				 ResultSet resultSet = controller.queryAllBorrowers();
		   	 Model model =  new Model(resultSet);	 	   
		   	 TableModel tableModel =model.getTableModel();
		   	 tableView.jTable.setModel(tableModel);
			}
		
			}//end try
		
		catch (EmptyStringException  e1) {
			// TODO: handle exception
			 JOptionPane.showMessageDialog(null, "Empty Input. Please Enter again!");
			 if(e1.flag ==1) {
				 lastNameTextField.setText("");
				 lastNameTextField.requestFocusInWindow();
			 }
			 
			 
			 else if(e1.flag ==2) { 				
				 firstNameTextFieldtext.setText(""); 
				 firstNameTextFieldtext.requestFocusInWindow();
				 }
			 
			 else if(e1.flag ==3) { 				
				 emailTextField.setText(""); 
				 emailTextField.requestFocusInWindow();
				 }
				
		}
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
				
				 if(titleString.trim().isEmpty() )
				 {
					 
					 throw new EmptyStringException(1);
				 }
					 
				 else if( isbnString.trim().isEmpty())
				 {
					 
					 throw new EmptyStringException(2);
				 }
				 
				 else if( editionNumString.trim().isEmpty())
				 {
					 
					 throw new EmptyStringException(3);
				 }
				 else if( subjectString.trim().isEmpty())
				 {
					 
					 throw new EmptyStringException(4);
				 }
				 else if( lastNameString.trim().isEmpty())
				 {
					 
					 throw new EmptyStringException(5);
				 }
				 else if( firstNameString.trim().isEmpty())
				 {
					 
					 throw new EmptyStringException(6);
				 }
					 
				if( !Pattern.matches("\\d{13}", isbnString))
					 throw new NumberFormatException();
				
				if( !Pattern.matches("\\d{1,3}", editionNumString))
					 throw new EmptyStringException(8);
				
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
			TableModel tableModel =queryBooksInLibrary();

		   booklisTable.setModel(tableModel);
		   queryAllAuthors();
		   queryAllSubject();
		    jc1.setModel(subjectRsltComBox); //update  combo box
		    jc2.setModel(authorRsltComBox); //update  combo box
			
		    if(tableView.isShowing()) {	
				tableView.jTable.setModel(tableModel);
			}
			
			  
			}//end try
			
			catch (EmptyStringException  e1) {
				// TODO: handle exception
				  if(e1.flag ==8) { 
				    JOptionPane.showMessageDialog(null, "Input should be  1 to 3 digits. Please Enter again!");			
					 editionNumJTextField.setText("");
					 editionNumJTextField.requestFocusInWindow();	
				 }
				  else {
				 JOptionPane.showMessageDialog(null, "Empty Input. Please Enter again!");
				 if(e1.flag ==1) {
					 titleTextField.setText("");
					 titleTextField.requestFocusInWindow();
				 }
				 
				 
				 else if(e1.flag ==2) { 
					
					 isbnJTextField.setText(""); 
					 isbnJTextField.requestFocusInWindow();}
				 else if(e1.flag ==3) { 
						
					 editionNumJTextField.setText(""); 
					 editionNumJTextField.requestFocusInWindow();}
				 else if(e1.flag ==4) { 
						
					 subjecTextField.setText(""); 
					 subjecTextField.requestFocusInWindow();}
				 else if(e1.flag ==5) { 
						
					 authorLastNameJTextField.setText(""); 
					 authorLastNameJTextField.requestFocusInWindow();}
				 else if(e1.flag ==6) { 
						
					 authorFirstNameJTextField.setText(""); 
					 authorFirstNameJTextField.requestFocusInWindow();}
				  }
			}
			
			 catch (NumberFormatException e1) {
				 JOptionPane.showMessageDialog(null, "Input should be thirteen digits. Please Enter again!");			
				 isbnJTextField.setText("");
				 isbnJTextField.requestFocusInWindow();		 
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
	 
	 private class bookLoanListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			 try {
				  int rowIndex = booklisTable.getSelectedRow();
				 String title = (String) booklisTable.getValueAt(rowIndex, 0); //get book title  in the table
				 
				 String dateDueString =  datedueJLabel2.getText(); //get date due 
				 String commentString  = commenTextField.getText();
				 String nameString = borrowListBox.getSelectedItem().toString();
			   	String []nameStrings = nameString.split(",");
			   	String lastName = nameStrings[0];
			   	String firstName = nameStrings[1];
				   controller.bookLoan(title,commentString,lastName,firstName,dateDueString);
				   commenTextField.setText("");
				  
				   bookLoanMessageJLabel.setText(lastName +" "+firstName+" borrowed "+title);
				   
				   //update book list
				   TableModel tableModel =queryBooksInLibrary();
				   booklisTable.setModel(tableModel);
				   returnbookJTable.setModel(new Model( controller.queryBooksOnLoan()).getTableModel());
				   if(tableView.isShowing()) {
				   	  if(rb1.isSelected()) 
				   	 tableView.jTable.setModel(tableModel);	
				   	  
				   	  if(rb2.isSelected()) 
				   	 tableView.jTable.setModel( new Model(controller.queryBooksOnLoan()).getTableModel());
					}
				   
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
	 private class bookReturnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
		 int row = 	returnbookJTable.getSelectedRow();
		 String booktitleString  =(String) returnbookJTable.getValueAt(row, 0);
		 String borrowerFirstNameString  = (String) returnbookJTable.getValueAt(row, 1);
		 String borrowerLastNameString  = (String) returnbookJTable.getValueAt(row, 2);
		 
		 controller.bookReturn(booktitleString, borrowerFirstNameString, borrowerLastNameString);	 
		 returnMessJLabel.setText(borrowerFirstNameString +" "+borrowerLastNameString+" returned "+booktitleString);
		 TableModel tableModel =queryBooksInLibrary();
		  
		 booklisTable.setModel(tableModel);
		   returnbookJTable.setModel(new Model( controller.queryBooksOnLoan()).getTableModel());
		   if(tableView.isShowing()) {
		   	  if(rb1.isSelected()) 
		   	 tableView.jTable.setModel(tableModel);	
		   	  
		   	  if(rb2.isSelected()) 
		   	 tableView.jTable.setModel( new Model(controller.queryBooksOnLoan()).getTableModel());
			}
		   
		  
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub	
			this.dispose();
			if(tableView !=null)
			tableView.dispose();			
	}

}
//end class