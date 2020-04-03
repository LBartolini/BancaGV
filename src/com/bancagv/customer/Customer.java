package com.bancagv.customer;

import java.net.*;
import java.io.*;

public class Customer {
	
	private Socket sock = null;
	private int port;
	private InetAddress ip;
	
	private BufferedReader in;
	private PrintWriter out;
	
	public Customer(int port, String ip) throws IOException {
		this.port = port;
		this.ip = InetAddress.getByName(ip);
	}
	
	public void connect() throws IOException{ // change to private
		sock = new Socket(this.ip, this.port);
		
		this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		this.out = new PrintWriter(sock.getOutputStream());
		
		while(true) {
			this.out.println("bella");
		}
	}
	
	public static void main(String[] args) throws IOException {
		Customer c = new Customer(5000, "127.0.0.1");
		c.connect();
	}

}
