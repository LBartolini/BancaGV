package com.bancagv.customer;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.*;
import com.bancagv.utils.*;

public class Customer {
	
	private Socket sock = null;
	private int port;
	private InetAddress ip;
	
	private BufferedReader in;
	private PrintWriter out;
	
	private boolean connected;
	private String name;
	
	
	public Customer(int port, String ip) throws IOException {
		this.port = port;
		this.ip = InetAddress.getByName(ip);
	}
	
	public void connect(){ // change to private
		try {
			sock = new Socket(this.ip, this.port);
			this.connected = true;
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
			this.out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			this.connected = false;
		}	
		try {
			this.auth("lorebart", "1234", 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean auth(String name, String passw, int mode) throws IOException { // mode : 0 = signin, else(default 1) = login
		boolean ret = false;
		if(this.connected) {
			// fixed error thanks to MIT
			// http://web.mit.edu/6.031/www/sp19/classes/23-sockets-networking/#using_network_sockets_in_java
			if(mode == 0) { // registration
				this.out.println("signin|"+name+"|"+passw+"|");
			}else { // login	
				this.out.println("login|"+name+"|"+passw+"|");	
			}
			this.out.flush();
			while(true) {
				if(this.in.ready()) { // get the result from the server
					List<String> result = Utils.split(this.in.readLine(), '|');
					if(result.get(0).compareTo("auth") == 0) {
						ret = true;
						this.name = name; 
					}
					break;
				}
			}
		}
		
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
		Customer c = new Customer(5000, "127.0.0.1");
		c.connect();
	}

}
