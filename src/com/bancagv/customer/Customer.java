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
	private String name;


	public Customer(int port, String ip) throws IOException {
		LoginPage p = new LoginPage(this);
		this.port = port;
		this.ip = InetAddress.getByName(ip);
		this.connected = false;
	}

	public void connect(){ // change to private
		try {
			sock = new Socket(this.ip, this.port);
		} catch (IOException e1) {
			System.out.println("connection");
		}

		try {
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
			this.out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8));
		} catch (Exception e1) {
			System.out.println("buffer");
		}
		this.connected = true;
		//DA RIMUOVERE SOLO PER TEST
		try {
			this.auth("lorebart", "abc123", 1);
		} catch (Exception e) {
			System.out.println("auth");
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
			System.out.println(sock.getPort()+ " "+ sock.getLocalPort()+ " "+ sock.getInetAddress()+ " " +sock.getRemoteSocketAddress());
			this.out.flush();

			/*
			List<String> tmp = new ArrayList<String>();

			tmp.add(this.in.readLine());

			for(String s: tmp) {
				Utils.print(s);
			}
			*/
			List<String> result = Utils.split(this.in.readLine(), '|');
			System.out.println(result);
			if(result.get(0).compareTo("auth") == 0) {
				ret = true;
				this.name = name;
			}
		}

		return ret;
	}

	public static void main(String[] args) throws IOException {
		Customer c = new Customer(6000, "127.0.0.1");
		c.connect();
	}

}
