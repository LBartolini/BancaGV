package com.bancagv.server;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.io.*;
import com.bancagv.utils.*;

public class ClientThread extends Thread {
	
	private Socket client = null;
	
	private Server server;
	private Semaphore mutex_out, mutex_in;
	
	private BufferedReader in;
	private PrintWriter out;
	
	private String bankcode= "null";
	
	public ClientThread(Socket client, Server server) {
		this.client = client;
		this.server = server;
		this.mutex_in = new Semaphore(1);
		this.mutex_out = new Semaphore(1);
	}
	
	@Override
	public void run() {
		try {
			this.in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
			this.out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
			boolean is_client_connected = true;
			while(is_client_connected) {
				if(this.in.ready()) {
					this.mutex_in.acquire();
					List<String> split_data = Utils.split(this.in.readLine(), '|'); // mode name passw
					this.mutex_in.release();
					
					if(split_data.get(0).compareTo("login") == 0) {
						//login
						this.login(split_data);
					}else if(split_data.get(0).compareTo("querycc") == 0) {
						//query data from bank account
						this.querycc(split_data);
					}else if(split_data.get(0).compareTo("close") == 0) {
						//close
						this.close(split_data);
						is_client_connected = false;
					}else if(split_data.get(0).compareTo("signin") == 0) {
						// new user
						this.signin(split_data);
					}else if(split_data.get(0).compareTo("pourmoney") == 0) {
						// pur money, versamento di soldi
						this.pour(split_data);
					}else if(split_data.get(0).compareTo("withdraw") == 0) {
						// withdraw, prelevare
						this.withdraw(split_data);
					}else if(split_data.get(0).compareTo("adduser") == 0) {
						// bind user to bank account
						this.adduser(split_data);
					}else if(split_data.get(0).compareTo("deluser") == 0) {
						// remove binding bank account-user
						this.deluser(split_data);
					}
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	private void adduser(List<String> input) throws Exception { // 0 = adduser, 1 = username, 2 = code
		String code = input.get(2);
		if(code.compareTo("null") == 0) {
			// create new BA
			code = this.server.createNewBA();
		}
		boolean ret = this.server.bindBA(input.get(1), code, this);
		
		this.mutex_out.acquire();
		if(ret) {
			// ok
			this.out.println("ok");
		}else {
			// not ok
			this.out.println("wrong");
		}
		this.mutex_out.release();
	}
	
	private void deluser(List<String> input) throws Exception {
		boolean ret = this.server.unbindBA(input.get(1), this);
		
		this.mutex_out.acquire();
		if(ret) {
			// ok
			this.out.println("ok");
		}else {
			// not ok
			this.out.println("wrong");
		}
		this.mutex_out.release();
	}
	
	private void pour(List<String> input) throws IOException, InterruptedException {
		FileHandler ba = this.server.getBA(this.bankcode);
		ba.open(this);
		Scanner rdr = ba.getReader();
		rdr.next(); // throw the bankcode
		double value = rdr.nextDouble();
		rdr.close();
		value += Double.parseDouble(input.get(1));
		BufferedWriter wrt = ba.getWriter();
		wrt.write(this.bankcode + " " + value);
		wrt.close();
		ba.close(this);
		this.server.updateValue(this.bankcode);
	}
	
	private void withdraw(List<String> input) throws IOException, InterruptedException {
		FileHandler ba = this.server.getBA(this.bankcode);
		ba.open(this);
		Scanner rdr = ba.getReader();
		rdr.next(); // throw the bankcode
		double value = rdr.nextDouble();
		rdr.close();
		if((value - Double.parseDouble(input.get(1))) >= 0) {
			value -= Double.parseDouble(input.get(1));
			BufferedWriter wrt = ba.getWriter();
			wrt.write(this.bankcode + " " + value);
			wrt.close();
		}
		ba.close(this);
		this.server.updateValue(this.bankcode);
	}
	
	private void querycc(List<String> input) throws InterruptedException {
		FileHandler ba = this.server.getBA(input.get(1)); // BA = bank account, conto corrente
		ba.open(this);
		Scanner tmp = ba.getReader();
		tmp.next();
		String balance = tmp.next();
		tmp.close();
		this.mutex_out.acquire();
		this.out.println(balance);
		this.mutex_out.release();
		ba.close(this);
	}
	
	public void updateValues() throws InterruptedException {
		FileHandler ba = this.server.getBA(this.bankcode); // BA = bank account, conto corrente
		ba.open(this);
		Scanner tmp = ba.getReader();
		tmp.next();
		String balance = tmp.next();
		tmp.close();
		this.mutex_out.acquire();
		this.out.println(balance);
		this.mutex_out.release();
		ba.close(this);
	}
	
	private void login(List<String> input) throws InterruptedException {
		FileHandler user = this.server.getUser(input.get(1));
		if(user != null) {
			//user exist
			user.open(this);
			this.mutex_out.acquire();
			String[] bankcode = new String[1];
			if(this.auth(user.getReader(), input.get(2), bankcode)) {
				// user logged in
				this.bankcode = bankcode[0];
				this.out.println("auth|"+this.bankcode+"|");
			}else {
				// wrong password
				this.out.println("wrong");
			}
			this.mutex_out.release();
			user.close(this);
		}
	}
	
	private void signin(List<String> input) throws IOException, InterruptedException {
		boolean ret = this.server.createUser(input.get(1), input.get(2));
		this.mutex_out.acquire();
		if(ret) {
			this.out.println("auth|null|");
		}else {
			this.out.println("wrong");
		}
		this.mutex_out.release();
	}
	
	private void close(List<String> input) throws InterruptedException {
		if(this.bankcode.compareTo("null") != 0) {
			this.server.getBA(this.bankcode).removeOffline(this);
		}
		Utils.print("Client ["+this.client.getInetAddress()+":"+this.client.getPort()+"] disconnected!");
	}
	
	private boolean auth(Scanner reader, String passw, String[] bankcode) {
		boolean ret = false;
		List<String> user_attributes = new ArrayList<String>();
		user_attributes.add(reader.next()); // name
		user_attributes.add(reader.next()); // passw
		bankcode[0] = reader.next(); // CC number
		reader.close();
		
		if(passw.compareTo(user_attributes.get(1)) == 0) {
			ret = true;
		}
		
		return ret;
	}
}
