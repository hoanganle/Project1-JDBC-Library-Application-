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
	
	public Model(ResultSet myRslt) {
		
		this.myRslt=myRslt;
		
	}

	public ResultSet getMyRslt() {
		return myRslt;
	}

	public void setMyRslt(ResultSet myRslt) {
		this.myRslt = myRslt;
	}
	
	public  DefaultComboBoxModel<String> getComboBoxModel(String str1,String str2) {
		
		return DbUtility.resultSetToDefaultComboBoxModel(myRslt,str1,str2);
	}
	
   public TableModel getTableModel() {
		
		return DbUtility.resultSetToTableModel(myRslt);
	}
	
}
//end class