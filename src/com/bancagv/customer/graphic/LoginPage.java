package com.bancagv.customer.graphic;

import java.awt.*;

import javax.swing.*;

import com.bancagv.customer.Customer;
public class LoginPage {
	private JFrame frame;
	private JPanel form;
	private JPanel formName;
	private JPanel formPassword;
	private JPanel formNameField;
	private JPanel formPasswordField;
	private JPanel formLogin;
	private JPanel formSignIn;
	private Label name;
	private Label password;
	private TextField nameField;
	private TextField passwordField;
	private JButton login;
	private JButton signIn;
	Customer customer;
	
	public LoginPage(Customer customer) {
		this.customer = customer;
		this.frame = new JFrame();
		
		this.frameSetup();
		
		this.form = new JPanel();
		
		this.formName = new JPanel();
		this.formPassword = new JPanel();
		this.formNameField = new JPanel();
		this.formPasswordField = new JPanel();
		this.formLogin = new JPanel();
		this.formSignIn = new JPanel();
		//this.formName.setLayout(new Layout());
		
		
		this.panelSetup();
		form.add(formName);
		form.add(formNameField);
		form.add(formPassword);
		
		form.add(formPasswordField);
		form.add(formLogin);
		form.add(formSignIn);
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
		
		this.formName.add(name);
		this.formPassword.add(password);
		this.formNameField.add(nameField);
		this.formPasswordField.add(passwordField);
		this.formLogin.add(login);
		this.formSignIn.add(signIn);
		
	}
	
	public void frameSetup() {
		this.frame.setBounds(300, 300, 300, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}
	
	public void panelSetup() {
		this.form.setLayout(new GridLayout(3, 2));
		this.form.setBackground(Color.cyan);
	}
	
	public void labelSetup() {
		//this.name.setAlignment(Label.CENTER);
		this.name.setSize(20, 10);
		//this.name.setLocation(0, 50);
		
		//this.password.setAlignment(Label.CENTER);
		this.password.setSize(20, 10);
		//this.password.setLocation(0, 50);
	}
	
	public void textFieldSetup() {
		this.nameField.setPreferredSize(new Dimension(100, 30));
		this.passwordField.setPreferredSize(new Dimension(100, 30));
	}
	
	public void buttonSetup() {
		
	}
}


