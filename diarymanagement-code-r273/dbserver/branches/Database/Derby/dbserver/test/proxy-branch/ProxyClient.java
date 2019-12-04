//charles jap, ckj866, 12:13 tues 21/07/09

import java.io.*;
import java.net.*; //for proxy objects

public class ProxyClient{

	String hostName = "";
	String proxyName = "";
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
		
		//will definately have to look at user/pass
		//dont think its offered in java.net.proxy
		
		hostName = "java.sun.com";
		proxyName = "proxy.uow.edu.au";
		
	}


	public ProxyClient(String h, String p, String pn) {
		
		System.out.println("test");
		
		hostName = h;
		proxyName = pn;
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
		
	/*	
		Examples:
		
		* Socket s = new Socket(Proxy.NO_PROXY); will create a plain socket ignoring any other proxy configuration.
		* Socket s = new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("socks.mydom.com", 1080))); 
			will create a socket connecting through the specified SOCKS proxy server.
		
	*/
		
		System.out.println("hostname: " + hostName);
		System.out.println("proxy: " + proxyName);
		System.out.println("port: " + port);
	
		InetAddress ina = null;
		InetSocketAddress sockAddress = null;
        
	/*	
		try {
            insa = new InetSocketAddress(proxyName, port);			
            
            //Socket s = new Socket(ina, port);
			//Socket s = new Socket(new Proxy(Proxy.Type.HTTP, insa));
			Socket s = new Socket(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyName, port))); 
            
			//above constructor makes an unconnected socket, we still have to connect it
			
			try {
				ina = InetAddress.getByName(hostName); // <- this is trouble line
				s.connect(new InetSocketAddress(ina,port));
			} catch (Exception e) {
				System.out.println("test");
				System.out.println(e.getMessage());
				System.exit(0);
			}
			
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
        } catch (Exception e) {
            //System.out.println("Couldn't get internet address from the host name");
            System.out.println(e.getMessage());
            System.exit(0);
        }
	*/
		
		
		
	/*	
		try{
			System.setProperty("http.proxyHost", "proxy.uow.edu.au");
			System.setProperty("http.proxyPort", "8080");
			
		}catch(Exception ex){
			System.out.println("here");
			System.out.println(ex);
		}
	*/
		
		
		//^ line above sets system properties for the java machine
		
		Socket s = null;
		sockAddress = new InetSocketAddress(proxyName, port);			
		
		try {
			//Socket s = new Socket(ina, port);
			//Socket s = new Socket(new Proxy(Proxy.Type.HTTP, sockAddress)); <- gives: type DIRECT is not compatible with address proxy.uow.edu.au/130.130.37.12:8080
			
			//s = new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyName, port)));
			s = new Socket(new Proxy(Proxy.Type.SOCKS, sockAddress));
			
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
		} catch (Exception e) {
			System.out.println("problem making proxy or socket before connect: ");
			System.out.println(e.getMessage());
			//System.exit(0);
		}
		
		//above constructor makes an unconnected socket, we still have to connect it
		
	/*
		URLConnection connection = url.openConnection();
		String password = "username:password";
		String encodedPassword = base64Encode( password );
		connection.setRequestProperty( "Proxy-Authorization", encodedPassword );
	*/
		
		
		try {
			ina = InetAddress.getByName(hostName); // <- this is trouble line
			
			System.out.println("hostName: " + ina.toString());
			
			SocketAddress connectTo = new InetSocketAddress(ina,port);
			
			//s.connect(new InetSocketAddress(ina,port), 30);
			
			
			s.connect(connectTo, 30);
			
			sendData("TEST");
			System.out.println(getData());
			
			s.close();
		} catch (Exception e) {
			System.out.println("problem connecting");
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		System.out.println("haha:");
		
		try {
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		
		System.out.println("end");
		
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