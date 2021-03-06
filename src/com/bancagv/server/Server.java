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
import java.lang.management.ManagementFactory;

import com.bancagv.utils.Utils;

public class Server {
	
	private List<FileHandler> bankaccounts, users;
	private Semaphore mutex_users;
	private Semaphore mutex_bankaccounts;
	
	private ServerSocket server = null;
	private int port;
	
	private int CODE_LENGTH = 10;
	
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
		FileHandler ret=null;
		try {
			this.mutex_users.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(FileHandler file: this.users) {
			if(file.getName().compareTo(name) == 0) {
				ret = file;
			}
		}
		this.mutex_users.release();
		return ret;
	}
	
	public FileHandler getBA(String code) {
		FileHandler ret=null;
		try {
			this.mutex_bankaccounts.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(FileHandler file: this.bankaccounts) {
			if(file.getName().compareTo(code) == 0) {
				ret = file;
			}
		}
		this.mutex_bankaccounts.release();
		return ret;
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
	
	public String createNewBA() throws Exception {
		String code = "null";
		boolean found = false;
		while(!(found)) {
			code = Utils.getAlphaNumericString(this.CODE_LENGTH);

			if(this.getBA(code) == null) {
				found = true;
			}
		}
		this.mutex_bankaccounts.acquire();
		
		File new_ba = new File("database/bankaccounts/"+code+".txt");
		new_ba.createNewFile();
		BufferedWriter wrt = new BufferedWriter(new FileWriter(new_ba));
		wrt.write(code+" 0");
		wrt.close();
		this.bankaccounts.add(new FileHandler("database/bankaccounts/"+code+".txt"));
		
		this.mutex_bankaccounts.release();
		return code;
	}
	
	public boolean bindBA(String name, String code, ClientThread ct) throws IOException {
		boolean ret = false;
		FileHandler user;
		if(((user = this.getUser(name)) != null) && (this.getBA(code) != null)) {
			// user and ba exist
			user.open(ct);
			Scanner rdr = user.getReader();
			rdr.next(); // name that we already have
			String psw = rdr.next();
			rdr.close();
			BufferedWriter wrt = user.getWriter();
			wrt.write(name+" "+psw+" "+code);
			wrt.close();
			user.close(ct);
			ret = true;
		}
		
		return ret;
	}
	
	public boolean unbindBA(String name, ClientThread ct) throws Exception {
		boolean ret = false;
		FileHandler user;
		if((user = this.getUser(name)) != null) {
			// user and ba exist
			user.open(ct);
			Scanner rdr = user.getReader();
			rdr.next(); // name that we already have
			String psw = rdr.next();
			rdr.close();
			BufferedWriter wrt = user.getWriter();
			wrt.write(name+" "+psw+" null");
			wrt.close();
			// send disconnection message to client
			for(ClientThread client_thread: user.getCustomersOnline()) {
				if(client_thread.getUsername().compareTo(name) == 0) {
					client_thread.disconnectFromBA();
				}
			}
			user.finishedUpdate();
			user.close(ct);
			ret = true;
		}
		return ret;
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
