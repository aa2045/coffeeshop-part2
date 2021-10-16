package f21as.coursework.coffeshop.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class OrdersTableModel extends AbstractTableModel{
	 
	private static final long serialVersionUID = -868024399356700859L;
//	String[] columnNames = {"Order", "Item", "date", "discount", "assigned"};
	String[] columnNames = {"Order", "Item", "date", "discount", "assigned", "Online"};
	
	private static List<List<String>> folders = new ArrayList<List<String>>();

	
	//the constructor of the class, takes as input the matrix of orders and attributes
	public OrdersTableModel(List<List<String>> allOrders){		  
		folders = allOrders;		  
	}
	  
	public String getColumnName(int c){
		return columnNames[c];
	}
	  
	public int getRowCount(){
		return folders.size();
	}

	public int getColumnCount(){
		return columnNames.length;
	}

	public Object getValueAt(int r, int c){

		List<String> folderInfo =  new ArrayList<String>();
		for(int i=0; i < folders.size(); i++){
			if (i==r){
				folderInfo = folders.get(i);					
			}
		}
		
		return folderInfo.get(c);
	}

	public List<List<String>> getFolders()
	{
		return this.folders;
	}

	// replace a new matrix
	public void replaceFolders(List<List<String>> encryptedFolders)
	{
		folders = encryptedFolders;		
	}
}