package com.bancagv.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import com.bancagv.utils.Utils;

public class Server {
	
	private List<FileHandler> bankaccounts, users;
	private Semaphore mutex_users;
	private Semaphore mutex_bankaccounts;
	
	private ServerSocket server = null;
	private int port;
	
	public Server(int port) throws IOException {
		this.port = port;
		this.bankaccounts = new ArrayList<FileHandler>();
		this.mutex_bankaccounts = new Semaphore(1);
		this.users = new ArrayList<FileHandler>();
		this.mutex_users = new Semaphore(1);
		this.server = new ServerSocket(this.port);
		this.initDB();
	}
	
	public void startListening() throws IOException {
		System.out.println("Listening...");
		while(true) {
			Socket client = this.server.accept();
			
			System.out.println("Connected with [" + client.getInetAddress() + ":" + client.getPort() + "]");
			
			ClientThread ct = new ClientThread(client, this);
			ct.setDaemon(true);
			ct.start();
		}
	}
	
	public void updateValue(String code) throws InterruptedException {
		FileHandler fh = this.getBA(code);
		for(ClientThread ct: fh.getCustomersOnline()) {
				ct.updateValues();
		}
		fh.finishedUpdate();
	}
	
	public FileHandler getUser(String name) {
		for(FileHandler file: this.users) {
			if(file.getName().compareTo(name) == 0) {
				return file;
			}
		}
		return null;
	}
	
	public FileHandler getBA(String code) {
		for(FileHandler file: this.bankaccounts) {
			if(file.getName().compareTo(code) == 0) {
				return file;
			}
		}
		return null;
	}
	
	private void initDB() {
		try {
			this.mutex_bankaccounts.acquire();
			this.mutex_users.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		this.mutex_bankaccounts.release();
		this.mutex_users.release();
	}
	
	public boolean createUser(String name, String passw) throws IOException {
		try {
			this.mutex_users.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean ret = false;
		
		for(FileHandler user: this.users) {
			if(user.getName().compareTo(name)==0) {
				// already exist a user with that name
				ret = false;
				break;
			}else {
				ret = true;
			}
		}
		File new_user = new File("database/users/"+name+".txt");
		new_user.createNewFile();
		BufferedWriter wrt = new BufferedWriter(new FileWriter(new_user));
		wrt.write(name+" "+passw+" null"+"\n");
		wrt.close();
		this.users.add(new FileHandler("database/users/"+name+".txt"));
		this.mutex_users.release();
		
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
		Server s = new Server(6000);
		s.startListening();
	}
	
}
