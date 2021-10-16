package f21as.coursework.coffeshop.gui;

import java.awt.event.ActionEvent;





import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;


import f21as.coursework.coffeshop.core.CoffeShopPresenter;
import f21as.coursework.coffeshop.data.Order;
import f21as.coursework.coffeshop.data.OrderList;
import f21as.coursework.coffeshop.exceptions.CustomerNotFoundException;
import f21as.coursework.coffeshop.interfaces.Observer;

public class OrdersFrame extends JPanel implements Observer{

	private static final long serialVersionUID = 3785549210161444057L;
	
	private JTable managementTable;
	private JScrollPane jScrollPane;
	private JButton refreshButton;	
	private JButton deleteButton;
	private JButton detailButton;	
	
	private int Win_buttonFieldPosX = 387, Unix_buttonFieldPosX = 397, Win_buttonWidth = 85, Unix_buttonWidth = 110;
	
	private CoffeShopPresenter engine;
	
	
	//the Frame is initialized with the Presenter
	public OrdersFrame(CoffeShopPresenter engine){
		
		this.engine = engine;
		initComponents();
		this.engine.registerObserver(this);
	}
	
	
	// this method initializes all the SWING components 
	private void initComponents() {
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(null, "List of ORDERS", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, null),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		setLayout(new GroupLayout());
		
		int itemHeight = 25;
		
		add(getJScrollPane(), new Constraints(new Leading(3, 375, 12, 12), new Leading(0, 160, 12, 12)));

		int buttonFieldPosX=0;
		int buttonWidth=0;
		String value = OSValidator.getOS();
		
		if(value.equals("win"))
		{
			buttonFieldPosX = Win_buttonFieldPosX;
			buttonWidth = Win_buttonWidth;	
		}
		else
		{
		
			buttonFieldPosX = Unix_buttonFieldPosX;
			buttonWidth = Unix_buttonWidth;
			
		}
		
		
		add(getRefreshButton(), new Constraints(new Leading(buttonFieldPosX, buttonWidth, 12, 12), new Leading(100, itemHeight, 12, 12)));
		add(getDeleteButton(), new Constraints(new Leading(buttonFieldPosX, buttonWidth, 12, 12), new Leading(135, itemHeight, 12, 12)));
		add(getDetailButton(), 	new Constraints(new Leading(buttonFieldPosX, buttonWidth, 12, 12), new Leading(35, itemHeight, 12, 12)));
		
		setSize(337, 200);
	}
	
	public JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			
			jScrollPane.setViewportView(getManagementTable());
		}
				
		return jScrollPane;
	}


	// initialize the OrdersTableModel object displaying the current queue of orders
	private void initializeTable(List<List<String>> protectedFolders)
	{
		
		TableModel ordersModel = new OrdersTableModel(protectedFolders);
		managementTable = new JTable(ordersModel);
		managementTable.setRowSelectionAllowed(true);
		managementTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			
			System.out.println("Value changed: "+managementTable.getSelectedRow());	
			int currentSelectedRow = managementTable.getSelectedRow();

			String enabled = (String)managementTable.getModel().getValueAt(currentSelectedRow, 2);
			
//			detailButton.setText("Detail");
			
			}
			});
		
	}
	

	
	// reset the orders table, it is called when refreshing
	public void resetManagementTable(){		
		managementTable = null;
	}
	
	
	// the method takes as a parameter the list of Orders and return a formatted matrix in which 
	// each row represent a single order and the columns its attributes 
	private List<List<String>> createOrdersList(OrderList orders)
	{
		
//		System.out.println("Creating table with orders: "+orders.getWaiting_Orders_Num());
		List<List<String>> protectedFolders = new ArrayList<List<String>>();
		List<String> orderInfo = null;

		if(!orders.isEmpty())
		{
			//iterates the orders
			Iterator iter = orders.getList().keySet().iterator();
			while(iter.hasNext())
			{
				
				String customerID = (String) iter.next();
				
				ArrayList<Order> ordersArr;
				try {
					ordersArr = orders.getCustomerOrdersArrays(customerID);
				} catch (CustomerNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ordersArr = new ArrayList<Order>();
				}
				
				
				//per each order collects the attributes to be displayed
				Iterator<Order> iterOrders =  ordersArr.iterator();
				while(iterOrders.hasNext())
				{
					orderInfo = new ArrayList<String>();
					Order order = iterOrders.next();
//					System.out.println(order.toString());
					orderInfo.add(order.getOrderID());
					orderInfo.add((String) order.getItemID());
//					DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
					DateFormat dateFormat = new SimpleDateFormat("HH:mm, dd-MM-yy");  

	                String strDate = dateFormat.format(order.getCreateDate().getTime());
					orderInfo.add(strDate);
//					orderInfo.add(""); this is supposed to display the waiting time for a particular order
					orderInfo.add(Integer.toString(order.getDiscount())+"%");
					String assignedTo = order.getAssignment();
					orderInfo.add(assignedTo);
//					System.out.println(assignedTo);
					boolean online = order.isOnline();
					if(online)
						orderInfo.add("Online");
					else
						orderInfo.add("");
					protectedFolders.add(orderInfo);
				}			
			}
       	 }

		System.out.println("----------------------------------");
		System.out.println(protectedFolders);
		System.out.println("----------------------------------");
		return protectedFolders;
	}
	
	
	//the method initialize a new table with the updated list of orders, it is called only once at initialization of the GUI components
	public JTable getManagementTable() {
//		if (managementTable == null) {
		/*TODO: get list of protected folders*/
		List<List<String>> protectedFolders = new ArrayList<List<String>>();
		List<String> folderInfo = null;
       	
		OrderList orders = engine.getordertList();	
		protectedFolders = this.createOrdersList(orders);
		initializeTable(protectedFolders);
		
		System.out.println("GetManagementTable --> There are "+managementTable.getRowCount()+ " lines in the table");
		TableColumnModel columnModel = managementTable.getColumnModel();  
		columnModel.setColumnSelectionAllowed(false);
//		}
		System.out.println("Hash value: "+managementTable.hashCode());	
		return managementTable;
	}
	
	// this method is called any time the OrderList is modified, when called it updates the table displaying the list of orders 
	public void refreshOrdersList()
	{
	 if(managementTable!=null)
	 {
		 OrdersTableModel tableModel = (OrdersTableModel) managementTable.getModel();
	   	 List<List<String>> waitingOrders = new ArrayList<List<String>>();
//	   	 synchronized(engine.getordertList())
//	   	 {
	   		this.resetManagementTable();
		   	 OrderList orders = engine.getordertList();
		   	 waitingOrders = this.createOrdersList(orders);
		   	 tableModel.replaceFolders(waitingOrders);
		   	 managementTable = new JTable(tableModel);
		   	 managementTable.repaint();
		   	 JViewport viewport = new JViewport(); 
		   	 viewport.add(managementTable);
		   	 jScrollPane.setViewport(viewport);
		   	 this.validate();
//	   	 }
	   	 
	 }
	 
   	
	}

	

	
	private JButton getRefreshButton() {
		if (refreshButton == null) {
			refreshButton = new JButton();
			refreshButton.setText("Refresh");

			refreshButton.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	 
		        	 refreshOrdersList();
//		        	 System.out.println("getRefreshButton --> There are "+managementTable.getRowCount()+ " lines in the table");
//		        	 System.out.println("getRefreshButton --> There are "+engine.getordertList().getWaiting_Orders_Num()+ " orders in the queue");
		        	 
		        	 
		         }
			});
		}
		return refreshButton;
	}
	
//	public void pressRefresh()
//	{
//		System.out.println("Refreshing annotations list...");
//		refreshButton.doClick();
//		
//	}
	
	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton();
			deleteButton.setText("Remove");
			
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				
					
			
				}
			});
		}deleteButton.setEnabled(false);
		return deleteButton;
	}


	
	
	private JButton getDetailButton() {
		if (detailButton == null) {
			detailButton = new JButton();
			detailButton.setText("Details");
			
			detailButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
			
				}
			});
		}detailButton.setEnabled(false);
		return detailButton;
	}
	
	
	
	public void updateItemState(){
		refreshButton.setEnabled(true);
		deleteButton.setEnabled(true);
	}

	
	//it is called because the class implements Observer, once it is called it refresh the table showing the list of orders
	@Override
	public void update() {
		// TODO Auto-generated method stub
		System.out.println("The Observer has just received a notification!");
		refreshOrdersList();
	}
}
