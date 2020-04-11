package com.bancagv.customer.graphic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import com.bancagv.customer.Customer;
import com.bancagv.utils.Utils;
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
	private Customer customer;
	
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
		
		this.login = new JButton("Login");
		this.signIn = new JButton("Sign in");
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
		this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
    		@Override
    		public void windowClosing(java.awt.event.WindowEvent windowEvent) {
    			customer.close();
    			System.exit(0);		    		}
    	});
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
		this.login.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				try {
					checkAccount(1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // login = 1, register = 0
			}
		});
		this.signIn.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				try {
					checkAccount(0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // login = 1, register = 0
			}
		});
	}
	
	public void checkAccount(int mode) throws IOException {
		Boolean check=false;
		try {
			check = this.customer.auth(this.nameField.getText(), this.passwordField.getText(), mode);
		} catch (IOException e) {
			Utils.print("ERRORE!!!");
		}
		if(check) {
			this.frame.dispose();
			this.customer.getData();
			new HomePage(this.customer);
		}
		else {
			System.out.println("Errato!");
		}
	}
}


