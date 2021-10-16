package f21as.coursework.coffeshop.gui;



import java.awt.Dimension;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;

import f21as.coursework.coffeshop.core.CoffeShopPresenter;
import f21as.coursework.coffeshop.core.CoffeShopPresenter;
import f21as.coursework.coffeshop.core.Constants;
import f21as.coursework.coffeshop.core.FileManager;
//import f21as.coursework.coffeshop.core.RefreshAnnotationThread;
import f21as.coursework.coffeshop.core.Utils;
import f21as.coursework.coffeshop.data.OrderList;
import f21as.coursework.coffeshop.exceptions.CustomerNotFoundException;
import f21as.coursework.coffeshop.exceptions.DiscountFileNotFoundException;
import f21as.coursework.coffeshop.exceptions.FailedInitializationException;
import f21as.coursework.coffeshop.exceptions.FailedReportException;
import f21as.coursework.coffeshop.exceptions.FrequencyException;
import f21as.coursework.coffeshop.exceptions.MenuFileNotFoundException;
import f21as.coursework.coffeshop.exceptions.OrderFileNotFoundException;
import f21as.coursework.coffeshop.exceptions.OrdersToFileException;
import f21as.coursework.coffeshop.runnable.Chef;
import f21as.coursework.coffeshop.runnable.OnLineOrderer;
import f21as.coursework.coffeshop.runnable.Waiter;
//import testgui.MainGUI;
//import testgui.ServersFrame;


public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L;	
	
	/*menu items*/
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuHelp;
	private JMenuItem menuItemExit;
	private JMenuItem menuItemHelp;
	private JMenuItem menuItemCredit;	
		
//	private JLabel HWLogo;
	private CustomerFrame customerFrame;
	private ReportFrame reportFrame;
	private OrdersFrame ordersFrame;
	private ServersFrame serversframe;
	
	//private JLabel server1label;
	
	
	
		
	private static final String PREFERRED_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	
	
//	private static int WIN_Width = 493;
//	private static int UNIX_Width = 530;
	
	private JTextArea queue;
	private static int WIN_Width = 530;
	private static int UNIX_Width = 530;
	private int Win_authFieldPosX = 525, Win_authFieldWidth= 400,itemHeight =10;
	private int Win_buttonFieldPosX = 215, Win_buttonWidth = 175;
	
	private CoffeShopPresenter engine;
	
	
	private OrdersFrame of;	
	
	private HashMap entities = new HashMap();
	
	
	public CoffeShopPresenter getEngine()
	{
		return this.engine;
	}
	
	public static String localDir()
	{
		File localdir = null;
		try 
		{
			localdir = new File(MainGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			localdir = localdir.getParentFile();
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("LocalDirectory: "+localdir.getAbsolutePath());
		return localdir.getAbsolutePath();
	}
	
	public MainGUI() throws FailedInitializationException, MenuFileNotFoundException, OrderFileNotFoundException, DiscountFileNotFoundException, CustomerNotFoundException  {
		
			
			
		    this.engine = CoffeShopPresenter.istanciateEngine();
	
			addWindowListener(new WindowAdapter(){
	        public void windowClosing(WindowEvent e)
	        {
	          dispose();
	          System.exit(0); //calling the method is a must
	        }
	     });
		 
		initComponents();
	}
	
	
	private void initComponents() {
		setLayout(new GroupLayout());
		
		OrdersFrame ordFrame = getOrdersFrame(engine);
		ReportFrame repFrame = getReportFrame(engine);
		ServersFrame servFrame = getServersFrame(engine);
		
		int listFieldPosX=0;
        int listFieldWidth=0;
        int buttonFieldPosX=0;
		int buttonWidth=0;
		

		String value = OSValidator.getOS();
		int width = 0;
		if(value.equals("win"))
		{
			width = 500;
			listFieldPosX = Win_authFieldPosX;
			listFieldWidth = Win_authFieldWidth;
			buttonFieldPosX = Win_buttonFieldPosX;
			buttonWidth = Win_buttonWidth;
			
			}
		else
		{
			listFieldPosX = Win_authFieldPosX;
        	listFieldWidth = Win_authFieldWidth;
        	buttonFieldPosX = Win_buttonFieldPosX;
			buttonWidth = Win_buttonWidth;
			width = MainGUI.UNIX_Width;	
		}
		
		add(getCustomerFrame(engine), new Constraints(new Leading(12, width, 12, 12), new Leading(15, 170, 12, 12)));
		
		add(repFrame, new Constraints(new Leading(12, width, 12, 12), new Leading(200, 95, 12, 12)));
		add(ordFrame, new Constraints(new Leading(12, width, 12, 12), new Leading(300, 200, 12, 12)));
		add(servFrame, new Constraints(new Leading(550, 445, 12, 12), new Leading(12, 580, 12, 12)));
		
		//JScrollPane scroll = new JScrollPane (getItemField());
		//scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      	//scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      	
     	//add(scroll, new Constraints(new Leading(listFieldPosX, listFieldWidth, 12, 12), new Leading(30, 460, 12, 12)));
		
		
	
        
        
		setJMenuBar(getJMenuBar0());


		setSize(1000, 600);

		this.setResizable(true);

	}

	protected void exit(int i) {
		System.exit(i);
	}

	/**************************************************************************************
	 * create menu items
	 **************************************************************************************/
//	 private JLabel getserver1label() {
//			if (server1label == null) {
//				server1label = new JLabel();
//				server1label.setText("Server 1");
//				server1label.setVisible(true);
//			}
//			return server1label;
//		}
	
	
	
	private JMenuBar getJMenuBar0() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMenuFile());
			menuBar.add(getMenuHelp());
		}
		return menuBar;
	}
	
	private JTextArea getItemField() {
        if (queue == null) {
               queue = new JTextArea();
               
               queue.setFont(new Font("Times New Roman", Font.BOLD, 12));
               queue.setEditable(false);
               queue.setText("");

        }
        return queue;
  }

	private JMenu getMenuFile() {
		if (menuFile == null) {
			menuFile = new JMenu();
			menuFile.setText("File");
			menuFile.setOpaque(false);
			menuFile.add(getMenuItemExit());
		}
		return menuFile;
	}
	
	private JMenuItem getMenuItemExit() {
		if (menuItemExit == null) {
			menuItemExit = new JMenuItem();
			menuItemExit.setText("Exit");
			menuItemExit.setName("exit");
			menuItemExit.setOpaque(false);
			MainListener.enableMenu(menuItemExit);
		
			menuItemExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						engine.saveOrders();
						try {
							saveReport("finalReport.txt");
						} catch (FailedReportException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (OrdersToFileException e1) {
						// TODO Auto-generated catch block
						//needs to be handled
						e1.printStackTrace();
					}
					dispose();
				}
			});
		}
		return menuItemExit;
	}
	
	private JMenu getMenuHelp() {
		if (menuHelp == null) {
			menuHelp = new JMenu();
			menuHelp.setText("?");
			menuHelp.setOpaque(false);
			menuHelp.add(getMenuItemHelp());
			menuHelp.add(getMenuItemCredit());
		}
		return menuHelp;
	}

	private JMenuItem getMenuItemHelp() {
		if (menuItemHelp == null) {
			menuItemHelp = new JMenuItem();
			menuItemHelp.setText("Help");
			menuItemHelp.setName("help");
			menuItemHelp.setOpaque(false);
			MainListener.enableMenu(menuItemHelp);
		}
		return menuItemHelp;
	}
	
	private JMenuItem getMenuItemCredit() {
		if (menuItemCredit == null) {
			menuItemCredit = new JMenuItem();
			menuItemCredit.setText("Credits");
			menuItemCredit.setName("credits");
			menuItemCredit.setOpaque(false);
			MainListener.enableMenu(menuItemCredit);
		}
		return menuItemCredit;
	}
	
	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			UIManager.setLookAndFeel(lnfClassname);
		} 
		catch (Exception e){
			System.err.println("The class " + PREFERRED_LOOK_AND_FEEL
					+ " cannot be installed on this platform:" + e.getMessage());
			String lnfClassname = UIManager.getSystemLookAndFeelClassName();
//			String lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			System.err.println("Installing default 'LOOK_AND_FEEL' class: "+lnfClassname);
			try {
				System.err.println("Trying...");
				UIManager.setLookAndFeel(lnfClassname);
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	
	private CustomerFrame getCustomerFrame(CoffeShopPresenter engine) {
		if (customerFrame == null) {
			customerFrame = new CustomerFrame(engine);
			
		}
		return customerFrame;
	}
	
	private ReportFrame getReportFrame(CoffeShopPresenter engine) {
		if (reportFrame == null) {
			reportFrame = new ReportFrame(engine);
			
		}
		return reportFrame;
	}
	
	private OrdersFrame getOrdersFrame(CoffeShopPresenter engine) {
		if (ordersFrame == null) {
			ordersFrame = new OrdersFrame(engine);
		}
		return ordersFrame;
	}
	
	private ServersFrame getServersFrame(CoffeShopPresenter engine) {
		if (serversframe == null) {
			serversframe = new ServersFrame(engine);	
			
		}
		return serversframe;
	}
	
	
	
	private void saveReport(String fileName) throws FailedReportException
	{
		try 
		{
			FileManager.overWriteFile(fileName, engine.generateReport());
		} catch (FrequencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FailedReportException("Failed to save the report "+fileName);
		}
	}
	
	@Override
	public void dispose() {
	    
		try {
			engine.saveOrders();
		} catch (OrdersToFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			saveReport("finalReport.txt");
		} catch (FailedReportException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		super.dispose();
	}
	
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainGUI frame;
				try 
				{
					frame = new MainGUI();
					
//					frame.setDefaultCloseOperation(MainGUI.EXIT_ON_CLOSE);
					frame.setDefaultCloseOperation(MainGUI.DISPOSE_ON_CLOSE);
					
					frame.setTitle("Heriot Watt CofeeShop Service - Trial Application");
					
					BufferedImage myPicture= null;
					InputStream url = this.getClass().getClassLoader().getResourceAsStream("f21as/coursework/coffeshop/pic/logo.JPG");
					try {
						myPicture = ImageIO.read(url);
					} 
					catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ImageIcon image = new ImageIcon(myPicture);				
					
					frame.setIconImage(image.getImage());				
					frame.getContentPane().setPreferredSize(frame.getSize());

					frame.pack();
								
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					SwingUtilities.updateComponentTreeUI(frame);
											 
//					Thread thread1 = new Thread(new Waiter("staff1", frame.getEngine()));
//					thread1.start();
//					
//					Thread thread2 = new Thread(new Waiter("staff2", frame.getEngine()));
//					thread2.start();
//			
//					Thread thread3 = new Thread(new Chef("The Chef", frame.getEngine()));
//					thread3.start();
//					
					Thread thread4 = new Thread(new OnLineOrderer(frame.getEngine()));
					thread4.start();

					
				} catch (FailedInitializationException | MenuFileNotFoundException | OrderFileNotFoundException | DiscountFileNotFoundException | CustomerNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				 

			}
		});
	}
	

}
