package f21as.coursework.coffeshop.gui;


import java.awt.Font;
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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;


import f21as.coursework.coffeshop.core.CoffeShopPresenter;
import f21as.coursework.coffeshop.core.Utils;
import f21as.coursework.coffeshop.data.Item;
import f21as.coursework.coffeshop.data.Order;
import f21as.coursework.coffeshop.data.OrderList;
import f21as.coursework.coffeshop.exceptions.CustomerNotFoundException;
import f21as.coursework.coffeshop.interfaces.Observer;
import f21as.coursework.coffeshop.runnable.Barista;
import f21as.coursework.coffeshop.runnable.Chef;
import f21as.coursework.coffeshop.runnable.Waiter;


// servers frame implements the observer and is responsible for displaying all the orders allocated to the staff
public class ServersFrame extends JPanel implements Observer{
	
			// private data members
			private JLabel server1label;
			private JLabel server2label;
			private JLabel server3label;
			private JLabel server4label;
			private JLabel cheflabel;
			private JLabel baristalabel;
			private JLabel setsimlabel;
			private JLabel incsimlabel;
			private JLabel decsimlabel;
			private static final long serialVersionUID = 3785549210161444057L;
			private JTextArea queue1;
			private JTextArea queue2;
			private JTextArea queue3;
			private JTextArea queue4;
			private JTextArea chefqueue;
			private JTextArea baristaqueue; 
			
			
			
			// defining the positions for placing each component in the GUI
			private int Win_authFieldPosX = 25, Win_authFieldWidth= 175,  itemHeight = 10;
			private int Win_buttonFieldPosX = 215, Win_buttonWidth = 175;

			// individual threads for servers present in the coffeeshop
			private Waiter server1Thread;
			private Waiter server2Thread;
			private Waiter server3Thread;
			private Waiter server4Thread;
			private Chef chefThread;
			private Barista baristaThread;
			
			
			// default speed of JSlider
			private double defaultSpeed=0.5;
			
			private CoffeShopPresenter engine;
			
			// constructor
			public ServersFrame(CoffeShopPresenter engine) {
				this.engine = engine;
				initComponents();
				this.engine.registerObserver(this);
				
				// initialize thereads for each server and set default speed to 0.5
				server1Thread = new Waiter(engine.server1Name, this.engine, this.defaultSpeed);
				server2Thread = new Waiter(engine.server2Name, this.engine, this.defaultSpeed);
				server3Thread = new Waiter(engine.server3Name, this.engine, this.defaultSpeed);
				server4Thread = new Waiter(engine.server4Name, this.engine, this.defaultSpeed);
				chefThread = new Chef(engine.chefName, this.engine, this.defaultSpeed);
				baristaThread = new Barista(engine.baristaName, this.engine, this.defaultSpeed);
				
				// start 3 threads (Server1, Chef and Barista) by default on running the GUI
				Thread thread1 = new Thread(server1Thread);
				thread1.start();
				
				Thread threadC = new Thread(chefThread);
				threadC.start();
				
				Thread threadB = new Thread(baristaThread);
				threadB.start();
			}
			
			// method to place the components in the GUI 
			 private void initComponents() {
	             setBorder(BorderFactory.createCompoundBorder(
	                           BorderFactory.createTitledBorder(null, "Servers Panel", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, null),
	                          BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	             setLayout(new GroupLayout());
	             
	             
	             int listFieldPosX=0;
	             int listFieldWidth=0;
	             int buttonFieldPosX=0;
	     		 int buttonWidth=0;
	     		
	     		// returns the operating system on which eclipse is running 
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
	     		}
	             
	     		// add label components into the GUI
	     		add(getserver1label(), new Constraints(new Leading(15, 67, 12, 12), new Leading(8, itemHeight, 12, 12)));
	     		add(getserver2label(), new Constraints(new Leading(240, 67, 12, 12), new Leading(8, itemHeight, 12, 12)));
	    		add(getserver3label(), new Constraints(new Leading(15, 67, 12, 12), new Leading(180, itemHeight, 12, 12)));
	    		add(getserver4label(), new Constraints(new Leading(240, 67, 12, 12), new Leading(180, itemHeight, 12, 12)));
	    		add(getcheflabel(), new Constraints(new Leading(15, 67, 12, 12), new Leading(352, itemHeight, 12, 12))); 
		        add(getbaristalabel(), new Constraints(new Leading(240, 67, 12, 12), new Leading(352, itemHeight, 12, 12))); 
		        add(getsetsimlabel(), new Constraints(new Leading(180, 80, 12, 12), new Leading(500, itemHeight, 12, 12)));
	    		add(getdecsimlabel(), new Constraints(new Leading(100, 67, 12, 12), new Leading(510, itemHeight, 12, 12)));
	    		add(getincsimlabel(), new Constraints(new Leading(320, 67, 12, 12), new Leading(510, itemHeight, 12, 12)));
	           
	    		//add server1 window
	    		JScrollPane server1 = new JScrollPane (getserver1Field());
	      		server1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	            server1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	            		            
	            add(server1, new Constraints(new Leading(15, 175, 12, 12), new Leading(18,125, 12, 12)));
	    		 
	            //add server2 window
	    		JScrollPane server2 = new JScrollPane (getserver2Field());
		     	server2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		        server2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    	
		        add(server2, new Constraints(new Leading(240, 175, 12, 12), new Leading(18, 125, 12, 12)));
		    	
		        //add server3 window
		        JScrollPane server3 = new JScrollPane (getserver3Field());
		        server3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		        server3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    	  
		        add(server3, new Constraints(new Leading(15, 175, 12, 12), new Leading(190, 125, 12, 12)));
		    	
		        //add server4 window
		       	JScrollPane server4 = new JScrollPane (getserver4Field());
		   		server4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	         	server4.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    	  
	           	add(server4, new Constraints(new Leading(240, 175, 12, 12), new Leading(190, 125, 12, 12)));
	           	
	            //add chef window
		        JScrollPane chef = new JScrollPane (getchefField());
		        chef.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		        chef.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    	  
		        add(chef, new Constraints(new Leading(15, 175, 12, 12), new Leading(362, 125, 12, 12)));
		    	
		        //add barista window
		        JScrollPane barista = new JScrollPane (getbaristaField());
		        barista.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		        barista.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    	  
		        add(barista, new Constraints(new Leading(240, 175, 12, 12), new Leading(362, 125, 12, 12)));
		         
		        // add checkboxes
		        JCheckBox checkBox2 = new JCheckBox();   
		            
		        add(checkBox2, new Constraints(new Leading(315, 75, 12, 12), new Leading(0, 18, 12, 12)));
		            
		        checkBox2.addActionListener(new ActionListener() {
		    	     public void actionPerformed(ActionEvent e) {
		    	      if(checkBox2.isSelected())
		    	      {
		    	    	  System.out.println("Checkbox is selected");
		    	    	  // if the checkbox above server2 is selected, then start its respective thread
		    	    	  if(server2Thread.isActive())
		    	    	  {
		    	    		  Thread thread2 = new Thread(server2Thread);
			  				  thread2.start();
		    	    	  }
		    	    	  else
		    	    		  server2Thread.enable();  
		    	    		  
		    	    	  queue2.setEditable(false);  
		    	      }
		    	      
		    	      else
		    	      {
		    	    	  System.out.println("Checkbox is NOT selected");
		    	    	  server2Thread.disable();
		    	    	  queue2.setEditable(false); 
		    	    	  
		    	      }
		    	              	
		    	     }
		    	 });	
		            
		            
		            JCheckBox checkBox3 = new JCheckBox();   
		            
		            add(checkBox3, new Constraints(new Leading(85, 75, 12, 12), new Leading(172, 18, 12, 12)));
		            
		            checkBox3.addActionListener(new ActionListener() {
		    	         public void actionPerformed(ActionEvent e) {	                 
		    	        	 if(checkBox3.isSelected())
				    	      {
		    	        		  // if the checkbox above server2 is selected, then start its respective thread
				    	    	  System.out.println("Checkbox is selected");
				    	    	  if(server3Thread.isActive())
				    	    	  {
				    	    		  Thread thread3 = new Thread(server3Thread);
					  				  thread3.start();
				    	    	  }
				    	    	  else
				    	    		  server3Thread.enable();  
				    	    		  
				    	    	  queue3.setEditable(false);  
				    	      }
				    	      
				    	      else
				    	      {
				    	    	  System.out.println("Checkbox is NOT selected");
				    	    	  server3Thread.disable();
				    	    	  queue3.setEditable(false); 
				    	    	  
				    	      }        	
		    	         }
		    	      });
		            
		            
		            JCheckBox checkBox4 = new JCheckBox();   
		            
		            add(checkBox4, new Constraints(new Leading(315, 75, 12, 12), new Leading(172, 18, 12, 12)));
		            
		            checkBox4.addActionListener(new ActionListener() {
		    	         public void actionPerformed(ActionEvent e) {	                 
		    	        	 if(checkBox4.isSelected())		    	        
				    	      {
		    	        		  // if the checkbox above server2 is selected, then start its respective thread
				    	    	  System.out.println("Checkbox is selected");
				    	    	  if(server4Thread.isActive())
				    	    	  {
				    	    		  Thread thread4 = new Thread(server4Thread);
					  				  thread4.start();
				    	    	  }
				    	    	  else
				    	    		  server4Thread.enable();  
				    	    		  
				    	    	  queue4.setEditable(false);  
				    	      }
				    	      
				    	      else
				    	      {
				    	    	  System.out.println("Checkbox is NOT selected");
				    	    	  server4Thread.disable();
				    	    	  queue4.setEditable(false); 
				    	    	  
				    	      }        	
		    	         }
		    	      });
		            
//		            JCheckBox checkBox5 = new JCheckBox();   
//		            
//		            add(checkBox5, new Constraints(new Leading(85, 75, 12, 12), new Leading(344, 18, 12, 12)));
//		            
//		            checkBox5.addActionListener(new ActionListener() {
//		    	         public void actionPerformed(ActionEvent e) {	                 
//		    	        	chefqueue.setEditable(false);        	
//		    	         }
//		    	      });
//		            
//		            JCheckBox checkBox6 = new JCheckBox();   
//		            
//		            add(checkBox6, new Constraints(new Leading(315, 75, 12, 12), new Leading(344, 18, 12, 12)));
//		    		
//		            checkBox6.addActionListener(new ActionListener() {
//		    	         public void actionPerformed(ActionEvent e) {	                 
//		    	        	baristaqueue.setEditable(false);        	
//		    	         }
//		    	      });
		            
		            // adding the slider onto the GUI
		            int speed = (int) (this.defaultSpeed*10);
		            JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 10, speed);      
		            slider.setMinorTickSpacing(1);  
		            slider.setMajorTickSpacing(9);

		            slider.setPaintTicks(true);  
		            slider.setPaintLabels(true);  
		            add(slider, new Constraints(new Leading(135, 180, 12, 12), new Leading(510, 42, 12, 12)));
		            slider.addChangeListener(null);
		            slider.addChangeListener(new ChangeListener() {
		    	         public void actionPerformed(ActionEvent e) {	                 
		    	        	        	
		    	         }
		    	         
		    	         
		    	        // Catch the change in speed and display on the console
						@Override
						public void stateChanged(ChangeEvent e) {
							// TODO Auto-generated method stub
							JSlider source = (JSlider) e.getSource();

					        double speed = (double)source.getValue();
					        speed = speed * 0.1;
					        
					        server1Thread.changeSpeed(speed);
							server2Thread.changeSpeed(speed);
							server3Thread.changeSpeed(speed);
							server4Thread.changeSpeed(speed);
							chefThread.changeSpeed(speed);
							baristaThread.changeSpeed(speed);
					        System.out.println("CHANGE THE SLIDE ----> "+speed);
						}

						
		    	      });
		            
		            
	   	
			}
   	
			 
			 private JLabel getserver1label() {
					if (server1label == null) {
						server1label = new JLabel();
						server1label.setText("Server 1");
						
					}
					return server1label;
				}
				
				 private JLabel getserver2label() {
						if (server2label == null) {
							server2label = new JLabel();
							server2label.setText("Server 2");
						}
						return server2label;
					}
				 private JLabel getserver3label() {
						if (server3label == null) {
							server3label = new JLabel();
							server3label.setText("Server 3");
						}
						return server3label;
					}
				 private JLabel getserver4label() {
						if (server4label == null) {
							server4label = new JLabel();
							server4label.setText("Server 4");
						}
						return server4label;
					}
				 private JLabel getcheflabel() {
						if (cheflabel == null) {
							cheflabel = new JLabel();
							cheflabel.setText("Chef");
						}
						return cheflabel;
					}
				 private JLabel getbaristalabel() {
						if (baristalabel == null) {
							baristalabel = new JLabel();
							baristalabel.setText("Barista");
						}
						return baristalabel;
					}
				 
				 private JLabel getsetsimlabel() {
						if (setsimlabel == null) {
							setsimlabel = new JLabel();
							setsimlabel.setText("Set Simulation");
						}
						return setsimlabel;
					}
				 
				 private JLabel getincsimlabel() {
						if (incsimlabel == null) {
							incsimlabel = new JLabel();
							incsimlabel.setText("Slow");
						}
						return incsimlabel;
					}
				 
				 private JLabel getdecsimlabel() {
						if (decsimlabel == null) {
							decsimlabel = new JLabel();
							decsimlabel.setText("Fast");
						}
						return decsimlabel;
					}
				 
				 
				 private JTextArea getserver1Field() {
			         if (queue1 == null) {
			                queue1 = new JTextArea();
			                
			                queue1.setFont(new Font("Times New Roman", Font.BOLD, 12));
			                queue1.setEditable(false);             
			         }
			         return queue1;
				 	}
				 
				 private JTextArea getserver2Field() {
			         if (queue2 == null) {
			                queue2 = new JTextArea();
			                
			                queue2.setFont(new Font("Times New Roman", Font.BOLD, 12));
			                queue2.setEditable(false);
			                queue2.setText("");

			         }
			         return queue2;
			   }
				 
				 private JTextArea getserver3Field() {
			         if (queue3 == null) {
			                queue3 = new JTextArea();
			                
			                queue3.setFont(new Font("Times New Roman", Font.BOLD, 12));
			                queue3.setEditable(false);
			                queue3.setText("");

			         }
			         return queue3;
			   }
				
				private JTextArea getserver4Field() {
			         if (queue4 == null) {
			               queue4 = new JTextArea();
			                
			                queue4.setFont(new Font("Times New Roman", Font.BOLD, 12));
			                queue4.setEditable(false);
			                queue4.setText("");
			                
			         }
			         return queue4;
			   }
				private JTextArea getchefField() {
			        if (chefqueue == null) {
			               chefqueue = new JTextArea();
			               
			               chefqueue.setFont(new Font("Times New Roman", Font.BOLD, 12));
			               chefqueue.setEditable(false);
			               chefqueue.setText("");

			        }
			        return chefqueue;
			  }
				private JTextArea getbaristaField() {
			        if (baristaqueue == null) {
			               baristaqueue = new JTextArea();
			               
			               baristaqueue.setFont(new Font("Times New Roman", Font.BOLD, 12));
			               baristaqueue.setEditable(false);
			               baristaqueue.setText("");

			        }
			        return baristaqueue;
			  }
		
				// method to print the order on the server panel
				private String printOrder(Order order)
				{
					Item item = engine.getMenu().getItem(order.getItemID());
					String output="";
					output = output+"Order: "+order.getOrderID()+"\n";
					output = output+"Item: "+order.getItemID()+"\n";
					output = output+"Name: "+item.getName()+"\n";
					output = output+"Date: "+order.getCreateDate()+"\n";
					output = output+"Price AED: "+item.getCost()+"\n";
					output = output+"Discount: "+order.getDiscount()+"\n";
					
					if(order.isOnline())
						output = output+"\nONLINE!!\n";
					
					return output;
				}
				
				@Override
				public void update() {
					// TODO Auto-generated method stub
					System.out.println("The Observer has just received a notification!");
					//refreshOrdersList();
					try 
					{
						// gets all the orders under process 
						ArrayList<Order> orders =  this.engine.getOrdersUnderProcess();
						Iterator<Order> iter = orders.iterator();
						boolean server1=false;
						boolean server2=false;
						boolean server3=false;
						boolean server4=false;
						boolean chef=false;
						boolean barista=false;
						// iteratre through all the orders
						while(iter.hasNext())
						{
							Order order = iter.next();
							// returns the staff member to whom the order is assigned to
							String assignTo = order.getAssignment();
							String text = this.printOrder(order);
							System.out.println(order.getOrderID()+" <---> "+assignTo);
							// display the respective order in the respective panel
							if(assignTo.equals(engine.server1Name))
							{this.queue1.setText(text);
							server1=true;}
								
							else if(assignTo.equals(engine.server2Name))
							{this.queue2.setText(text);
							server2=true;}
								
							else if(assignTo.equals(engine.server3Name))
							{this.queue3.setText(text);
							server3=true;}
								
							else if(assignTo.equals(engine.server4Name))
							{this.queue4.setText(text);
							server4=true;}
								
							else if(assignTo.equals(engine.chefName))
							{this.chefqueue.setText(text);
							chef=true;}
								
							else if(assignTo.equals(engine.baristaName ))
							{this.baristaqueue.setText(text);
							barista=true;}
								
						}
						
						if(!server1) this.queue1.setText("");
						if(!server2) this.queue2.setText("");
						if(!server3) this.queue3.setText("");
						if(!server4) this.queue4.setText("");
						if(!chef) this.chefqueue.setText("");
						if(!barista) this.baristaqueue.setText("");
						
					} catch (CustomerNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}		
				
				
				
				
				
				
				
				
}
	
	
	