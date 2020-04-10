package com.bancagv.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class FileHandler {

	private Semaphore mutex;
	private ClientThread now;
	private String file_path;
	
	private File file;
	private String file_name;
	private List<ClientThread> customers_online = new ArrayList<ClientThread>();
	
	private Scanner reader;
	private BufferedWriter writer;
	
	public FileHandler(String file_path) {
		this.file_path = file_path;
		this.file = new File(file_path);
		this.file_name = this.file.getName().replaceFirst(".txt", "");
		this.mutex = new Semaphore(1);
	}
	
	public void removeOffline(ClientThread ct) {
		for(int i = 0; i < this.customers_online.size(); i++) {
			if(this.customers_online.get(i) == ct){
				this.customers_online.remove(i);
				break;
			}
		}
	}
	
	public void open(ClientThread ct) {
		try {
			this.mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean exist = false;
		for(ClientThread cust : this.customers_online) {
			if(cust == ct){
				exist = true;
				break;
			}
		}
		if(!(exist)) {
			this.customers_online.add(ct);
		}
		this.now = ct;
	}
	
	public void close(ClientThread ct) {
		if(now == ct) {
			now = null;
			this.mutex.release();
		}
	}
	
	public String getName() {
		return this.file_name;
	}
	
	public Scanner getReader() {
		try {
			this.reader = new Scanner(this.file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.reader;
	}
	
	public BufferedWriter getWriter() {
		try {
			this.writer = new BufferedWriter(new FileWriter(this.file_path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.writer;
	}
	
}
