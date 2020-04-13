package com.bancagv.customer.graphic;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Disconnected {
	private JFrame frame;
	private JPanel panel;
	private Label text1;
	private Label text2;
	
	public Disconnected() {
		this.frame = new JFrame();
		this.panel = new JPanel();
		this.text1 = new Label("Non hai più accesso al tuo Conto");
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
	}
	
	public void panelSetup() {
		this.panel.setLayout(new FlowLayout());
	}
	
	public void textSetup() {
		this.text1.setFont(new Font("", Font.BOLD, 16));
		this.text2.setFont(new Font("", Font.BOLD, 16));
	}
}
