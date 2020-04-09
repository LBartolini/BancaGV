package com.bancagv.server;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import com.bancagv.utils.*;

public class ClientThread extends Thread {
	
	private Socket client = null;
	
	private Server server;
	
	private BufferedReader in;
	private PrintWriter out;
	
	private String name = "";
	
	public ClientThread(Socket client, Server server) {
		this.client = client;
		this.server = server;
		System.out.println(client.getPort()+ " "+ client.getLocalPort()+ " "+ client.getInetAddress()+ " " +client.getRemoteSocketAddress());
	}
	
	@Override
	public void run() {
		try {
			this.in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
			this.out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
		
			while(true) {
				if(this.in.ready()) {
					List<String> split_data = Utils.split(this.in.readLine(), '|'); // mode name passw
					
					if(split_data.get(0).compareTo("login") == 0) {
						//login
						FileHandler user = this.server.getUser(split_data.get(1));
						if(user != null) {
							//user exist
							user.open(this);
							if(this.auth(user.getReader(), split_data.get(2))) {
								// user logged in
								this.out.println("auth");
							}else {
								// wrong password
								this.out.println("wrong");
							}
							this.out.flush();
							user.close(this);
						}
					}
				}
			}
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	private boolean auth(Scanner reader, String passw) {
		boolean ret = false;
		List<String> user_attributes = new ArrayList<String>();
		user_attributes.add(reader.next()); // name
		user_attributes.add(reader.next()); // passw
		user_attributes.add(reader.next()); // CC number
		
		if(passw.compareTo(user_attributes.get(1)) == 0) {
			ret = true;
		}
		
		reader.close();
		return ret;
	}

}
