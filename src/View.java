/**
 *Program:View.java
 *Purpose:
 *Author:Gang
 *Date:Jul. 27, 2019
 *
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class View extends JFrame {

	JTabbedPane t1;
	JPanel p1,p2,p3;
	JButton jb1;
	JLabel l1,l2,l3;
	JTextField text1,text2,text3;
	JRadioButton rb1,rb2,rb3,rb4,rb5;
	JComboBox<String> jc1, jc2;
	JCheckBox ch1,ch2,ch3;
	
	
	
	public View () throws SQLException {
		super("Gui");//will go into title bar
		
		//BOILERPLATE CODE: you see these lines in just about every swing app
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//object becomes garbage when closed
		this.setSize(450, 400);//width and height of the JFrame in PIXELS
		this.setLocationRelativeTo(null);//centres our JFrame on the screen	
	
		t1=new JTabbedPane();
		
		 p1=new JPanel();
	    p2=new JPanel();
	    p3=new JPanel();
	   
	    p1.setLayout(new GridLayout(3,2));

	    l1=new JLabel("Name");
	    l2=new JLabel("Date of Birth (dd.mm.yyyy)");
	    l3=new JLabel("Identification Number");

	    text1=new JTextField(10);
	    text2=new JTextField(10);
	    text3=new JTextField(10);

	    p1.add(l1);
	    p1.add(text1);
	    p1.add(l2);
	    p1.add(text2);
	    p1.add(l3);
	    p1.add(text3);
	   
	    ch1=new JCheckBox("Computers");
	    ch2=new JCheckBox("Electronics");
	    ch3=new JCheckBox("Marketing");

	    
	    ResultSet authorRslt = new Controller().queryAuthor();
	    Model model = new Model();
	    model.setMyRslt(authorRslt);
	    DefaultComboBoxModel<String> ComboBoxmodel = model.getComboBoxModel();
	    
	    rb1=new JRadioButton("All books in the library");
	    rb2=new JRadioButton("All books on loan and the borrower");
	    rb3=new JRadioButton("Books by subject");
	    rb4=new JRadioButton("Books by author");
	    rb4.addItemListener((e)->{
	   	 jc2.setVisible(true);
	   	 
	    });
	    rb5=new JRadioButton("All borrowers");
	    jc1 = new JComboBox<>();
	    jc2 =new JComboBox<>(ComboBoxmodel);
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
        jb1.addActionListener((e)->{
	   	
	   	String nameString = jc2.getSelectedItem().toString();
	   	String []nameStrings = nameString.split(",");
	   	String firstName = nameStrings[0];
	   	String lastName = nameStrings[1];
	   	
	    });
	   
		 
		 p2.add(rb5);
	    p2.add(new JLabel(""));
       p2.add(jb1);
       
	    p3.add(ch1);
	    p3.add(ch2);
	    p3.add(ch3);

	    t1.addTab("Personal Information",p1);
	    t1.addTab("Education Qualification", p2);
	    t1.addTab("Area of intrest",p3);

	    add(t1);

	    this.setVisible(true);
	}
	
//	public static ResultSet makeConnection() {
//	Connection myConn = null;
//		Statement myStmt = null;
//		 ResultSet myRslt = null; 
//			try
//			{
//			//step 1: create the Connection object by calling getConnection() method
//			// of DriverManager class
//			myConn = DriverManager.getConnection(
//						         "jdbc:mysql://localhost:3306/INFO3136_Books?useSSL=false&allowPublicKeyRetrieval=true", 
//						                       "root","hangang");		
//			//step 2: create statement object
//			myStmt = myConn.createStatement();
//			
//			//step 3: pass in a query and execute it and catch the returned ResultSet
//			myRslt = myStmt.executeQuery("SELECT last_name,first_name FROM Author");
//			
//			}//end try
//			catch(SQLException ex)
//			{
//				System.out.println("SQL Exception, message is: " + ex.getMessage());
//			}
//			catch(Exception ex)
//			{
//				System.out.println("Some other Exception, message is: " + ex.getMessage());
//			}
//			
//			return myRslt;
//	}
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