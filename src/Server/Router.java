package Server;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class Router {
	public static int port = 2406;
	public static String ip = "";

	public static ServerSocket server;

	public static ArrayList<Socket> list_sockets = new ArrayList<Socket>();
	public static ArrayList<Integer> list_client_states = new ArrayList<Integer>();
	public static ArrayList<DataPackage> list_data = new ArrayList<DataPackage>();

	public static int cout_match = 0;
	// //////////////////////////////////////////////////////////////////////////////////////
	// run //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ RUN HERE
	private static Runnable accept = new Runnable() {
		@SuppressWarnings({ "unchecked", "unused" })
		@Override
		public void run() {
			new Thread(send).start(); // ///////send\\\\\\\\\\
			new Thread(receive).start(); // //////receiv\\\\\\\\\\
			         
			while (true) {
				try {
					Socket socket = server.accept();
					
					
					 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

					 String des_ip = (String) ois.readObject();
					 String source_ip = (String) ois.readObject();
					 String mac_string_des = (String) ois.readObject();
					 String mac_string_src = (String) ois.readObject();
					// String n = (String) ois.readObject();
					 String data_packet;
					 String data_table;
					////////////////////////////////////////////////////////////////////////////////////////////////////////
					boolean accepted = true;

					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					
					NetworkInterface networkInterface = NetworkInterface.getByInetAddress(Inet4Address.getLocalHost());
					if (accepted) {
						oos.writeObject("ยินดีต้อนรับ..");
						

						 data_packet = des_ip +"           "+ source_ip + "          " + mac_string_des
								+ "          " + mac_string_src;
						////////////////////////////////////////////////////////////////////
						list_clients_model.addElement(data_packet);
						Thread.sleep(1000);
						list_clients_model.addElement(data_packet);
						Thread.sleep(1000);
						list_clients_model.addElement(data_packet);
						Thread.sleep(1000);
						list_clients_model.addElement(data_packet);
						
						///////////////////////////////////////////////
						
						 data_table ="                  "+ des_ip + "                                            "
						+source_ip +"/" + networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
		
						////////////////// MODEL 2 \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
						if(list_clients_model2.getSize() == 0 ){
							list_clients_model2.addElement(data_table);
							new Thread(wait).start();			///////////// Timer
						}
						else {
							 cout_match = 0;
						for(int i=0;i< list_clients_model2.getSize();i++){
							 		if(data_table.equalsIgnoreCase(list_clients_model2.getElementAt(i)) )
							 			{
							 			cout_match++;
							 			}
							}
						if(cout_match==0){
							
							list_clients_model2.addElement(data_table);

						}
							
					//	else 
					//		System.out.println(cout_match);
	
						}
						//cout_match = 0;
						list_client_states.add(0);
						list_data.add(new DataPackage());
						list_sockets.add(socket);
					} else {
						oos.writeObject("Your name is already taken!");
					}
				} catch (Exception ex) {
				}
				
			}
		}
	};

	
	
	private static Runnable wait = new Runnable() {
		@Override
		public void run() {
	try {
	    Thread.sleep(10000);                 //1000 milliseconds is one second.  
	    list_clients_model.clear();
	    list_clients_model2.clear();
	} catch(InterruptedException ex) {
	    Thread.currentThread().interrupt();
	}
		}
	};
	private static Runnable send = new Runnable() {
		@Override
		public void run() {
			ObjectOutputStream oos;

			while (true) {
				for (int i = 0; i < list_sockets.size(); i++) {
					try {
						oos = new ObjectOutputStream(list_sockets.get(i)
								.getOutputStream());
						int client_state = list_client_states.get(i);
						oos.writeObject(client_state);

						oos = new ObjectOutputStream(list_sockets.get(i)
								.getOutputStream());
						oos.writeObject(list_data);
					} catch (Exception ex) {
					}
				}
			}
		}
	};

	private static Runnable receive = new Runnable() {
		@Override
		public void run() {
			ObjectInputStream ois;

			while (true) {
				for (int i = 0; i < list_sockets.size(); i++) {
					try {
						ois = new ObjectInputStream(list_sockets.get(i)
								.getInputStream());


						ois = new ObjectInputStream(list_sockets.get(i)
								.getInputStream());
						DataPackage dp = (DataPackage) ois.readObject();

						list_data.set(i, dp);
				
					} catch (Exception ex) {}// Client Disconnected (Client Didn't
											// Notify Server About
											// Disconnecting)
			
					
				}
				
			}
		}
	};



	public static JFrame frame;

	public static JPanel content;

	public static JPanel panel_ip;
	public static JPanel panel_l, panel_r;

	public static JButton btn_disconnect;

	public static JList<String> list_clients, list_clients2;
	public static DefaultListModel<String> list_clients_model,list_clients_model2;
	
	public static JPanel panel_2,panel_2r;
	public static JLabel lblNewLabel, lblNewLabel_1, lblNewLabel_2, lblNewLabel_3;
	public static JLabel lblNewLabel_r, lblNewLabel_1r, lblNewLabel_2r, lblNewLabel_3r;
	public static JScrollPane scroll_packet, scroll_routing;
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}

		try {
			ip = "Default Gateway : " + InetAddress.getLocalHost().getHostAddress();// + ":" + port;

			server = new ServerSocket(port, 0, InetAddress.getLocalHost());

			new Thread(accept).start();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(),
					"Alert", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}


		list_clients_model = new DefaultListModel<String>();
		list_clients_model2 = new DefaultListModel<String>();
		

		list_clients = new JList<String>(list_clients_model);
		list_clients2 = new JList<String>(list_clients_model2);

		frame = new JFrame();
		frame.setTitle("Router SIM - " + ip);
////////////////////////////////////////////////////////////////////////
		frame.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {

				System.exit(0);
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowOpened(WindowEvent e) {
			}
		});
		
		
		 panel_2 = new JPanel();
		panel_2.setLayout(new FlowLayout(4, 70,2 ));
		
		 lblNewLabel = new JLabel("Des.IP");
		panel_2.add(lblNewLabel);
		
		 lblNewLabel_1 = new JLabel("Src. IP");
		panel_2.add(lblNewLabel_1);
		
		 lblNewLabel_2 = new JLabel("Des. MAC");
		panel_2.add(lblNewLabel_2);
		
		 lblNewLabel_3 = new JLabel("Src. MAC");
		panel_2.add(lblNewLabel_3);
		
		

		panel_l = new JPanel();

		panel_l.add(panel_2);//,BorderLayout.NORTH);
		
		scroll_packet = new JScrollPane(list_clients);
		scroll_packet.setPreferredSize(new Dimension(421,500));
		
		panel_l.add(scroll_packet);//, BorderLayout.CENTER);

		panel_l.setBorder(new TitledBorder(null, "Packet", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));

		
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////rrrr
		
			panel_2r = new JPanel();

			panel_2r.setLayout(new FlowLayout(2, 160,2 ));
			
			 lblNewLabel_r = new JLabel("Des.IP");
			panel_2r.add(lblNewLabel_r);
			
			 lblNewLabel_1r = new JLabel("Src. IP");
			panel_2r.add(lblNewLabel_1r);
			
			
		panel_ip = new JPanel();
		panel_ip.add(new JLabel(ip));
		
		panel_r = new JPanel();
		panel_r.add(panel_2r);
		scroll_routing = new JScrollPane(list_clients2);
		scroll_routing.setPreferredSize(new Dimension(400,300));
		panel_r.add(scroll_routing);//, BorderLayout.CENTER);
		
		panel_r.add(panel_ip);// , BorderLayout.SOUTH);
		panel_r.setBorder(new TitledBorder(null, "Routing Table",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));

		

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		content = new JPanel();
		content.setLayout(new GridLayout(1, 5, 2, 5));

		content.add(panel_l);// , BorderLayout.WEST);
		content.add(panel_r);// , BorderLayout.EAST);


		content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		frame.setContentPane(content);
		frame.pack();
		frame.setSize(900, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
