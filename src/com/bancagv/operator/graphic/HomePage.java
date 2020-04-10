package com.bancagv.operator.graphic;

import java.awt.*;

import javax.swing.*;

import com.bancagv.operator.Operator;

public class HomePage {
	private Operator operator;

	private JFrame frame;
	
	private JPanel mainPanel;
	private JPanel namePanel;
	private JPanel codePanel;
	private JPanel actionPanel;
	private JPanel createAccount;
	private JPanel removeAccount;
	
	private TextField name;
	private TextField code;
	private JButton addAccount;
	private JButton delAccount;
	
	public HomePage(Operator operator) {
		this.frame = new JFrame();
		this.mainPanel = new JPanel();
		this.namePanel = new JPanel();
		this.codePanel = new JPanel();
		this.actionPanel = new JPanel();
		this.createAccount = new JPanel();
		this.removeAccount = new JPanel();
		
		this.frameSetup();
		this.frame.add(mainPanel);
		
		this.mainPanelSetup();
		this.mainPanel.add(namePanel);
		this.mainPanel.add(codePanel);
		this.mainPanel.add(actionPanel);
		
		this.actionPanel.add(createAccount);
		this.actionPanel.add(removeAccount);
		
		this.name = new TextField();
		this.code = new TextField();
		this.addAccount = new JButton("Aggiungi");
		this.delAccount = new JButton("Rimuovi");
		
		this.textFieldSetup();
		this.buttonSetup();
		this.namePanel.add(name);
		this.codePanel.add(code);
		this.createAccount.add(addAccount);
		this.removeAccount.add(delAccount);
	}
	
	public void frameSetup() {
		this.frame.setBounds(300, 300, 300, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}
	
	public void mainPanelSetup() {
		this.mainPanel.setLayout(new GridLayout(3, 1));
		
	}
	
	public void textFieldSetup() {
		this.name.setPreferredSize(new Dimension(100, 30));
		this.code.setPreferredSize(new Dimension(100, 30));
	}
	
	public void buttonSetup() {
		this.addAccount.setPreferredSize(new Dimension(100, 30));
		this.delAccount.setPreferredSize(new Dimension(100, 30));
	}
}
