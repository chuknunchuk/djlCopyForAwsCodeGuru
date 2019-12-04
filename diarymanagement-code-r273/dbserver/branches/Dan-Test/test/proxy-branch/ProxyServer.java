import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.*;

public class ProxyServer {

	int port = 8080;
	/*
	DataInputStream in = null;
	DataOutputStream out = null;
	*/
	ServerSocket	serv = null;
	
	public ProxyServer(){
	}
	
	public void createConnection() {
		try {
			serv = new ServerSocket(port);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	public void waitForClient(){
		for(;;) {
			System.out.println("waiting");
			Socket s = null;
			try {
				s = serv.accept();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				break;
			}
			
			HandleThread ht = new HandleThread(s);
			Thread t = new Thread(ht);
			t.start();
		}
	}
	
	public static void main(String[] args){
		ProxyServer server = null;
		
		server = new ProxyServer();
		
		server.createConnection();
		server.waitForClient();
	}
	
}	



class HandleThread implements Runnable {
	Socket con = null;
	DataInputStream in = null;
	DataOutputStream out = null;
	
	public HandleThread(Socket s){
		con = s;
	}
	
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
	
	public void run(){
	
		try{
			in = new DataInputStream(con.getInputStream());
			out = new DataOutputStream(con.getOutputStream());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.close();
			} catch (Exception err) {
				System.out.println(err.getMessage());
			}
			return;
		}
		
		for(;;){
			String message = getData();
			System.out.println("\nMessage: "+message);
			
			if(message.equals("TEST")) {
				System.out.println("awesome");
			} else {
				System.out.println("unknown: "+message);
				break;
			}
		}
		
	}
}