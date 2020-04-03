package com.bancagv.server;

import java.io.IOException;
import java.net.*;

public class Server {
	
	private ServerSocket server = null;
	private int port;
	
	public Server(int port) throws IOException {
		this.port = port;
		this.server = new ServerSocket(this.port);
	}
	
	public void startListening() throws IOException {
		System.out.println("Listening...");
		while(true) {
			Socket client = this.server.accept();
			
			System.out.println("Connected with " + client.getInetAddress() + ":" + client.getPort());
			
			ClientThread ct = new ClientThread(client);
			ct.start();
		}
	}

	public static void main(String[] args) throws IOException {
		Server s = new Server(5000);
		s.startListening();
	}
	
}
