package Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;


public class Remote_Host
{
	public static Socket socket;
	
	public static int port = 2406;
	public static String ip = "";
	
	
	
	public static void main(String[] args)
	{
		try
		{
			String local;
			
			try
			{
				local = InetAddress.getLocalHost().getHostAddress();// + ":" + port;
			}
			catch (UnknownHostException ex)
			{
				local = "Network Error";
			}
			
			
			
			
			
			
			
			//////////////////////////////////source_ip///////////////////////////////////////////////////////////////////////////////////
			String source_ip =  (String) JOptionPane.showInputDialog(null, "My IP : ", "Info", JOptionPane.INFORMATION_MESSAGE, null, null, local);
			
			//////////////////////// default gateway//////////////////////////////////////////////////////////////////////
			ip = (String) JOptionPane.showInputDialog(null, "Default Gateway : ", "Info", JOptionPane.INFORMATION_MESSAGE, null, null, local);
			//	ip = ip.substring(0);
			socket = new Socket(ip, port);
			
			
			////////////////////////////////////////des_ip////////////////////////////////////////////////////////////////////////////
			String des_ip = "";
			des_ip = (String) JOptionPane.showInputDialog(null, "Ping to : ", "Info", JOptionPane.INFORMATION_MESSAGE, null, null, local);
			
			
			
			////////////////////////////////////////mac_string_des//////////////////////////////////////////////////////////////////////////////////
	/*		NetworkInterface network_des = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()); 
			byte[] mac_des = network_des.getHardwareAddress();
			StringBuilder mac_string_des = new StringBuilder();
			for (int i = 0; i < mac_des.length; i++) {
				mac_string_des.append(String.format("%02X%s", mac_des[i], (i < mac_des.length - 1) ? "-" : ""));		
			}
	*/	
			/////////////////////////////////////////////mac_src////////////////////////////////////////////////////////////////////////
			 NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
				byte[] mac_src = network.getHardwareAddress();
				StringBuilder mac_string_src = new StringBuilder();
				for (int i = 0; i < mac_src.length; i++) {
					mac_string_src.append(String.format("%02X%s", mac_src[i], (i < mac_src.length - 1) ? "-" : ""));		
				}

			/////////////////////////////////////////////////SUB NET MASK//////////////////////////////////////////////////////////////////////
				NetworkInterface networkInterface = NetworkInterface.getByInetAddress(Inet4Address.getLocalHost());
				int subnet = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
			
			//String n = "";
			//n =  (String) JOptionPane.showInputDialog(null, "Ping times : ", "Info", JOptionPane.INFORMATION_MESSAGE, null, null, "3");
			
			
			//for(int i=0; i<Integer.parseInt(n) ;i++ ){
				
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(des_ip);
			oos.writeObject(source_ip);
			oos.writeObject("------------------------");
			oos.writeObject(mac_string_src.toString());
			oos.writeObject(subnet);
			//oos.writeObject(n);
			
			
		//	ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		//	String response = (String) ois.readObject();
		//  JOptionPane.showMessageDialog(null, response, "Message", JOptionPane.INFORMATION_MESSAGE);
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
