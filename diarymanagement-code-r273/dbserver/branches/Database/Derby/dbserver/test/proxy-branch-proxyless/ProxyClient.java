//charles jap, ckj866, 12:13 tues 21/07/09

import java.io.*;
import java.net.*; //for proxy objects

public class ProxyClient{

	String hostName = "";
	int port = 8080;
	DataInputStream in = null;
	DataOutputStream out = null;
	
	
	public void sendData(String s) {
		try{
			out.writeBytes(s+"\n");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String getData() {
		byte[] s=new byte[1024];
		int cnt = 0;
		try{
			while(true){
				byte b = in.readByte();
				if(b== '\n')
					break;
				s[cnt] = b;
				cnt++;
				if(cnt >=1024)
					break;
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		String str = new String(s, 0, cnt);
		return str;
	}	
	
	
	/******************************************************
	
	Constructors
	
	/*****************************************************/
	

	public ProxyClient() {
		
		hostName = "java.sun.com";	
		
	}


	public ProxyClient(String h, String p, String pn) {
		
		System.out.println("test");
		
		
		hostName = h;
		//proxyName = pn;
		port = Integer.parseInt(p);
		
		
	}
	
	
	/******************************************************
	
	methods and functions
	
	
	//connect should connect through proxy - still need to
	//find out where you can put in the hostname
	
	
	/*****************************************************/
	
	
	public void Connect(){
	
	
		//http://java.sun.com/j2se/1.5.0/docs/api/java/net/Socket.html
		//^ has the socket methods listed
		
		/*Examples:
		
		* Socket s = new Socket(Proxy.NO_PROXY); will create a plain socket ignoring any other proxy configuration.
		* Socket s = new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("socks.mydom.com", 1080))); 
			will create a socket connecting through the specified SOCKS proxy server.
		
		*/
		
		System.out.println("hostname: " + hostName);
		System.out.println("port: " + port);
		
		
        
		try {
			
			InetAddress ina = InetAddress.getByName(hostName); // <- this is trouble line
          	
            Socket s = new Socket(ina, port);
			
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
			
			sendData("TEST");
			System.out.println(getData());
			
			s.close();
        } catch (Exception e) {
            System.out.println("Couldn't get internet address from the host name");
            System.out.println(e.getMessage());
            System.exit(0);
        }
		
	}
	
	
	/******************************************************
	
	Main
	
	/*****************************************************/
	
	
	public static void main(String[] args) {
	
		ProxyClient aProxyClient;
		
		aProxyClient = new ProxyClient();
		
		aProxyClient.Connect();
	}
}
