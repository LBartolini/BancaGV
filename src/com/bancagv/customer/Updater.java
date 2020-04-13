package com.bancagv.customer;

import java.io.BufferedReader;
import java.io.IOException;
import com.bancagv.utils.*;
import com.bancagv.customer.graphic.Disconnected;
import com.bancagv.customer.graphic.HomePage;

public class Updater extends Thread{
	
	private Customer customer;
	private HomePage homepage;
	private BufferedReader in;
	
	public Updater(Customer c, HomePage hp, BufferedReader in) {
		this.customer = c;
		this.homepage = hp;
		this.in = in;
	}
	
	@Override
	public void run() {
		boolean active = true;
		while(active) {
			try {
				if(this.in.ready()) {
					String data="";
					data = this.in.readLine();
					if(data.compareTo("disconnect")==0) {
						this.homepage.close("disconnected");
						active = false;
					}else {
					this.homepage.updateBalance(data);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
