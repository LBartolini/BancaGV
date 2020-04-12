package com.bancagv.customer.graphic;

import java.awt.*;
import javax.swing.*;

import com.bancagv.customer.Customer;

public class NewUserPage {
	
	private JFrame frame;
	private JPanel panel;
	private Label text1;
	private Label text2;
	
	private Customer customer;
	
	
	public NewUserPage(Customer customer) {
		this.customer = customer;
		
		this.frame = new JFrame();
		this.panel = new JPanel();
		this.text1 = new Label("Nessun conto corrente collegato");
		this.text2 = new Label("Contattare operatore");
		this.frameSetup();
		
		this.frame.add(panel);
		this.panelSetup();
		
		this.panel.add(text1);
		this.panel.add(text2);
		this.textSetup();
	}
	
	public void frameSetup() {
		this.frame.setBounds(300, 300, 300, 120);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
		this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
    		@Override
    		public void windowClosing(java.awt.event.WindowEvent windowEvent) {
    			customer.close();
    			System.exit(0);
    		}
    	});
	}
	
	public void panelSetup() {
		this.panel.setLayout(new FlowLayout());
	}
	
	public void textSetup() {
		this.text1.setFont(new Font("", Font.BOLD, 16));
		this.text2.setFont(new Font("", Font.BOLD, 16));
	}
}
