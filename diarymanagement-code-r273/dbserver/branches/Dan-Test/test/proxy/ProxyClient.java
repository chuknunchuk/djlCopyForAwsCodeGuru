//charles jap, ckj866, 12:13 tues 21/07/09

import java.io.*;
import java.net.*; //for proxy objects

public class ProxyClient{

	
	String hostName = "";
	String proxyName = "";
	String proxyPort = "8080";
	int port = 8080;
	DataInputStream in = null;
	DataOutputStream out = null;


	/******************************************************
	
	Send and Get methods: for exchanging data to server
	
	/*****************************************************/	
	
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
		//hostName = "localhost";	
		
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
	
	
		//set proxy settings for jvm
		try{
			System.setProperty("http.proxyHost", proxyName);
			System.setProperty("http.proxyPort", proxyPort);
			
		}catch(Exception ex){
			System.out.println("here");
			System.out.println(ex);
		}		
		
		
		//TODO: user pass proxy authentication
		
		System.out.println("hostname: " + hostName);
		System.out.println("port: " + port);
		
		
        
		try {
			
			InetAddress ina = InetAddress.getByName(hostName);
          	System.out.println(ina.toString());
			
            Socket s = new Socket(ina, port);
			
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
			
			sendData("TEST");
			System.out.println(getData());
			
			s.close();
        } catch (Exception e) {
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
