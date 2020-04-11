package com.bancagv.customer;

import java.io.BufferedReader;
import java.io.IOException;
import com.bancagv.utils.*;

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
		while(true) {
			try {
				if(this.in.ready()) {
					String new_balance="";
					try {
						new_balance = this.in.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.homepage.updateBalance(new_balance);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
