package com.bancagv.server;

import java.net.*;
import java.io.*;

public class ClientThread extends Thread {
	
	private Socket client = null;
	
	private BufferedReader in;
	private PrintWriter out;
	
	public ClientThread(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			this.out = new PrintWriter(client.getOutputStream());
		
		
			while(true) {
				if(this.in.ready()) {
					String data = this.in.readLine();
					System.out.println(data);
				}
			}
		} catch (IOException e) { e.printStackTrace();}
	}

}
