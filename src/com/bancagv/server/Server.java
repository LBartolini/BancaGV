package com.bancagv.server;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	private List<FileHandler> bankaccounts, users;
	
	private ServerSocket server = null;
	private int port;
	
	public Server(int port) throws IOException {
		this.port = port;
		this.bankaccounts = new ArrayList<FileHandler>();
		this.users = new ArrayList<FileHandler>();
		this.server = new ServerSocket(this.port);
		this.initDB();
	}
	
	public void startListening() throws IOException {
		System.out.println("Listening...");
		while(true) {
			Socket client = this.server.accept();
			
			System.out.println("Connected with " + client.getInetAddress() + ":" + client.getPort());
			
			ClientThread ct = new ClientThread(client, this);
			ct.start();
		}
	}
	
	public FileHandler getUser(String name) {
		for(FileHandler file: this.users) {
			if(file.getName().compareTo(name) == 0) {
				return file;
			}
		}
		return null;
	}
	
	private void initDB() {
		FilenameFilter filter_txt = new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(".txt");
			}
		};

		File dir_db_users = new File("database/users/");
		File dir_db_bankaccounts = new File("database/bankaccounts/");
		String[] users_path, bankaccounts_path;
				
		users_path = dir_db_users.list(filter_txt);
		bankaccounts_path = dir_db_bankaccounts.list(filter_txt);
		
		for(String path : users_path) {
			this.users.add(new FileHandler("database/users/"+path));
		}
		for(String path : bankaccounts_path) {
			this.bankaccounts.add(new FileHandler("database/bankaccounts/"+path));
		}
	}
	
	public static void main(String[] args) throws IOException {
		Server s = new Server(5000);
		s.startListening();
	}
	
}
