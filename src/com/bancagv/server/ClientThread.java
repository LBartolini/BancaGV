package com.bancagv.server;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class ClientThread extends Thread {
	
	private Socket client = null;
	
	private BufferedReader in;
	private PrintWriter out;
	
	private String name = "";
	
	public ClientThread(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			this.in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
			this.out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
		
			while(true) {
				if(this.in.ready()) {
					String data = this.in.readLine();
					System.out.println(data);
				}
			}
		} catch (IOException e) { 
			e.printStackTrace();
			}
	}

}
