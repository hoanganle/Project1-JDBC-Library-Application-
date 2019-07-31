import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import javax.swing.DefaultComboBoxModel;

/**
 *Program:Model.java
 *Purpose:
 *Author:Gang
 *Date:Jul. 27, 2019
 *
 */

public class Model {
	private ResultSet myRslt;
	
	public Model() {
		
		myRslt=null;
		
	}

	public ResultSet getMyRslt() {
		return myRslt;
	}

	public void setMyRslt(ResultSet myRslt) {
		this.myRslt = myRslt;
	}
	
	public  DefaultComboBoxModel<String> getComboBoxModel() {
		
		return DbUtility.resultSetToDefaultComboBoxModel(myRslt);
	}
	
   public TableModel getTableModel() {
		
		return DbUtility.resultSetToTableModel(myRslt);
	}
	
}
//end class