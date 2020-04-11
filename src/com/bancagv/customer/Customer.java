package com.bancagv.customer;

import java.net.*;
import com.bancagv.utils.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import com.bancagv.customer.graphic.*;

public class Customer {
	private Socket sock = null;
	private int port;
	private InetAddress ip;

	private BufferedReader in;
	private PrintWriter out;

	private boolean connected;
	private String name="";
	private String bankcode="";
	private double balance=0;


	public Customer(int port, String ip) throws IOException {
		new LoginPage(this);
		this.port = port;
		this.ip = InetAddress.getByName(ip);
		this.connected = false;
		this.connect();
	}
	
	private void connect(){ // change to private
		try {
			sock = new Socket(this.ip, this.port);
		} catch (IOException e1) {
			System.out.println("connection");
			return;
		}

		try {
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
			this.out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8), true);
		} catch (Exception e1) {
			System.out.println("buffer");
			return;
		}
		this.connected = true;
	}
	
	public void pour(double value) {
		this.out.println("pourmoney|"+value);
	}
	
	public void withdraw(double value) {
		this.out.println("withdraw|"+value);
	}
	
	public void close() {
		this.out.println("close");
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getBankCode() {
		return this.bankcode;
	}
	
	public void getData() throws IOException {
		if(this.name.compareTo("") != 0 && this.bankcode.compareTo("") != 0 && this.bankcode.compareTo("null") != 0) {
			// connected and bank account exist and is binded to the user
			this.out.println("querycc|"+this.bankcode+"|");
			
			List<String> temp = Utils.split(this.in.readLine(), '|'); // [0] = balance
			this.balance = Double.parseDouble(temp.get(0));
		}
	}
	
	public boolean isAccountConnected() {
		if(this.bankcode.compareTo("null") == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public void startUpdating(HomePage hp) {
		Updater u = new Updater(this, hp, this.in);
		u.start();
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
			// this.out.flush();
			List<String> result = Utils.split(this.in.readLine(), '|');
			if(result.get(0).compareTo("auth") == 0) {
				ret = true;
				this.name = name;
				this.bankcode = result.get(1);
			}
		}

		return ret;
	}

	public static void main(String[] args) throws IOException {
		Customer c = new Customer(6000, "127.0.0.1");
	}

}
