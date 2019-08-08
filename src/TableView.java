/**
 *Program:WorldLanguageViewer.java
 *Purpose:  Allow a user to search for contries in which a certain language is spoken by a minimum percentage of the population.  
 *         this a view class
 *Author:Gang
 *Date:Aug. 2, 2019
 *
 */
import javax.swing.*;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.*;
public class TableView  extends JFrame{

	//for sizing purposes to size the JFrame
	private static final double FRAME_WIDTH_FACTOR = 0.45;//25% of screen width
	  private static final double FRAME_HEIGHT_FACTOR = 0.5;//50% of screen height
	 
	
	JTable jTable ;
	public TableView (TableModel model) {
		
		super("View GUI");
		//boilerplate		
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);			
			this.setLayout(new BorderLayout());	
			//Variation: using the current dimensions of your screen resolution
			this.setSize(					
					(int)(this.getToolkit().getScreenSize().width *FRAME_WIDTH_FACTOR),
		  			(int)(this.getToolkit().getScreenSize().height *FRAME_HEIGHT_FACTOR)
					);
			
			this.setLocationRelativeTo(null);
		
			jTable =new JTable(model);
			JScrollPane jScrollPane = new JScrollPane(jTable);
		 //  jScrollPane.setSize(900, 600);
		   this.add(jScrollPane,BorderLayout.CENTER);	  
			this.setVisible(true);
	}
	
	
	
	
	

}
//end class