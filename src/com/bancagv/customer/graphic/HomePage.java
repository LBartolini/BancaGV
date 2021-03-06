package com.bancagv.customer.graphic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.swing.*;

import com.bancagv.customer.Customer;

public class HomePage {
	
	private JFrame frame;
	
	private JPanel form;
	private JPanel formDates;
	private JPanel formCash;
	private JPanel formActionBankAccount;
	private JPanel formCashAction;
	
	private Label name;
	private Label nCC;
	private Label bankAccount;
	private TextField actionBankAccount;
	private JButton take;
	private JButton deposit;
	
	private Customer customer;
	
	public HomePage(Customer customer) {
		this.customer = customer;
		this.frame = new JFrame();
		this.frameSetup();
		
		this.form = new JPanel();
		this.formDates = new JPanel();
		this.formCash = new JPanel();
		this.formActionBankAccount = new JPanel();
		this.formCashAction = new JPanel();
		this.panelSetup();
		
		this.form.add(formDates);
		this.form.add(formCash);
		this.form.add(formCashAction);
		this.form.add(formActionBankAccount);
		this.frame.add(form);
		
		this.name = new Label(this.customer.getName(), SwingConstants.CENTER);
		this.nCC = new Label(this.customer.getBankCode());
		this.bankAccount = new Label(this.customer.getBalance() + " Euro");
		this.customer.startUpdating(this);
		this.actionBankAccount = new TextField();
		this.take = new JButton("Preleva");
		this.deposit = new JButton("Versa");
		
		this.formCashSetup();
		this.actionBankAccountSetup();
		this.buttonSetup();
		this.formDates.add(name);
		this.formDates.add(nCC);
		
		this.formCash.add(bankAccount);
		this.formCashAction.add(actionBankAccount);
		
		this.formActionBankAccount.add(take);
		this.formActionBankAccount.add(deposit);
	}
	
	public void frameSetup() {
		this.frame.setBounds(300, 300, 300, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
		this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    		@Override
		    		public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    				close();
		    				}
		    	});
	}
	
	public void close() {
		customer.close();
		this.frame.dispose();
	}
	
	public void close(String x) {
		customer.close();
		new Disconnected();
		this.frame.dispose();
	}
	
	public void panelSetup() {
		this.form.setLayout(new GridLayout(4, 1));
	}
	
	public void formCashSetup() {
		//this.formCash.setLayout(new GridLayout(1, 2));
	}
	
	public void actionBankAccountSetup() {
		this.actionBankAccount.setPreferredSize(new Dimension(100, 30));
	}
	
	public void buttonSetup() {
		this.take.setPreferredSize(new Dimension(100, 30));
		this.take.setBackground(Color.red);
		this.take.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				take();
			}
		});
		
		this.deposit.setPreferredSize(new Dimension(100, 30));
		this.deposit.setBackground(Color.green);
		this.deposit.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				deposit();
			}
		});
	}
	
	private void take() {
		try {
			this.customer.withdraw(Double.parseDouble(this.actionBankAccount.getText()));
		}catch(Exception e){
			// wrong chars in text field
		}
		this.actionBankAccount.setText("");
	}
	
	private void deposit() {
		try {
			this.customer.pour(Double.parseDouble(this.actionBankAccount.getText()));
		}catch(Exception e){
			// wrong chars in text field
		}
		this.actionBankAccount.setText("");
	}
	
	public void updateBalance(String new_balance) {
		this.bankAccount.setText(new_balance+" Euro");
	}
}
