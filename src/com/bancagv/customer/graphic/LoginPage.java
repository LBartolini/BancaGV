package com.bancagv.customer.graphic;

import java.awt.*;

import javax.swing.*;
public class LoginPage {
	private JFrame frame;
	private JPanel form;
	private Label name;
	private Label password;
	private TextField nameField;
	private TextField passwordField;
	private JButton login;
	private JButton signIn;
	
	public LoginPage() {
		this.frame = new JFrame();
		
		this.frameSetup();
		
		this.form = new JPanel();
		
		this.panelSetup();
		
		this.frame.add(form);
		
		this.name = new Label("Nome Utente");
		this.password = new Label("Password");
		this.labelSetup();
		
		this.nameField = new TextField();
		this.passwordField = new TextField();
		this.textFieldSetup();
		
		this.login = new JButton();
		this.signIn = new JButton();
		this.buttonSetup();
		
		this.form.add(name);
		this.form.add(password);
		this.form.add(nameField);
		this.form.add(passwordField);
		this.form.add(login);
		this.form.add(signIn);
		
	}
	
	public void frameSetup() {
		this.frame.setBounds(300, 300, 300, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}
	
	public void panelSetup() {
		//this.form.setLayout(new FlowLayout());
		this.form.setBackground(Color.cyan);
	}
	
	public void labelSetup() {
		//this.name.setAlignment(Label.CENTER);
		this.name.setSize(20, 10);
		this.name.setLocation(0, 50);
		//this.password.setAlignment(Label.CENTER);
		this.password.setSize(20, 10);
		this.password.setLocation(0, 50);
	}
	
	public void textFieldSetup() {
		this.nameField.setBounds(20, 20, 20, 20);
		this.passwordField.setBounds(20, 20, 20, 20);
	}
	
	public void buttonSetup() {
		
	}
}


