package com.bancagv.server;

import java.net.*;
import java.io.*;

public class ClientThread extends Thread {
	
	private Socket client = null;
	
	private DataInputStream in;
	private DataOutputStream out;
	
	public ClientThread(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			this.in = new DataInputStream(client.getInputStream());
			this.out = new DataOutputStream(client.getOutputStream());
		
		
			while(true) {
			
				String data = this.in.readLine();
				System.out.println(data);

			}
		} catch (IOException e) { e.printStackTrace();}
	}

}
