package com.bancagv.customer.graphic;

import java.awt.*;

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
		
		this.name = new Label("nome", SwingConstants.CENTER);
		this.nCC = new Label("ABC123");
		this.bankAccount = new Label("50�");
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
		    			customer.close();
		    			System.exit(0);		    		}
		    	});
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
		this.deposit.setPreferredSize(new Dimension(100, 30));
		this.deposit.setBackground(Color.green);
		
	}
}
