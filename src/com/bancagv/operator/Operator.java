package com.bancagv.operator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.bancagv.operator.graphic.HomePage;
import com.bancagv.utils.Utils;

public class Operator {
	
	private Socket sock = null;
	private int port;
	private InetAddress ip;

	private BufferedReader in;
	private PrintWriter out;
	
	public Operator(int port, String ip) throws IOException {
		new HomePage(this);
		this.port = port;
		this.ip = InetAddress.getByName(ip);
		this.connect();
	}
	
	private void connect(){ // change to private
		try {
			sock = new Socket(this.ip, this.port);
		} catch (IOException e1) {
			System.out.println("connection");
			System.exit(0);
		}

		try {
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
			this.out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8), true);
		} catch (Exception e1) {
			System.out.println("buffer");
		}
	}
	
	public void close() {
		this.out.println("close");
	}
	
	public boolean add(String user, String code) {
		boolean ret = false;
		if(code.compareTo("") == 0) {
			// new bank account to create
			this.out.println("adduser|"+user+"|null");
		}else {
			// maybe bank account exist
			this.out.println("adduser|"+user+"|"+code);
		}
		
		try {
			if(this.in.readLine().compareTo("ok") == 0) {
				ret = true;
			}
		} catch (IOException e) {}
		
		return ret;
	}
	
	public boolean remove(String user) {
		boolean ret = false;
		
		this.out.println("deluser|"+user); // removes the link between user and bankaccount
		try {
			if(this.in.readLine().compareTo("ok") == 0) {
				ret = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}

	public static void main(String[] args) throws IOException {
		Operator o = new Operator(6000, "127.0.0.1");
	}

}
