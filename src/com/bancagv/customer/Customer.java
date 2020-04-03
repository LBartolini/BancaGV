package com.bancagv.customer;

import java.net.*;
import java.io.*;

public class Customer {
	
	private Socket sock = null;
	private int port;
	private InetAddress ip;
	
	private DataInputStream in;
	private DataOutputStream out;
	
	public Customer(int port, String ip) throws IOException {
		this.port = port;
		this.ip = InetAddress.getByName(ip);
	}
	
	public void connect() throws IOException{ // change to private
		sock = new Socket(this.ip, this.port);
		
		this.in = new DataInputStream(sock.getInputStream());
		this.out = new DataOutputStream(sock.getOutputStream());
	}
	
	public static void main(String[] args) throws IOException {
		Customer c = new Customer(5000, "127.0.0.1");
		c.connect();
	}

}
