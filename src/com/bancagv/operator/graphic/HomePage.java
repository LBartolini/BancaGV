package com.bancagv.operator.graphic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.bancagv.operator.Operator;
import com.bancagv.utils.*;

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
	private Label text1;
	private TextField code;
	private Label text2;
	
	private JButton addAccount;
	private JButton delAccount;
	
	public HomePage(Operator operator) {
		this.operator = operator;
		
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
		this.panelSetup();
		this.mainPanel.add(namePanel);
		this.mainPanel.add(codePanel);
		this.mainPanel.add(actionPanel);
		
		this.actionPanel.add(createAccount);
		this.actionPanel.add(removeAccount);
		
		this.name = new TextField();
		this.text1 = new Label("Nome utente");
		this.code = new TextField();
		this.text2 = new Label("Codice Conto Corrente");
		this.addAccount = new JButton("Aggiungi");
		this.delAccount = new JButton("Rimuovi");
		
		this.textFieldSetup();
		this.buttonSetup();
		this.namePanel.add(text1);
		this.namePanel.add(name);
		this.codePanel.add(text2);
		this.codePanel.add(code);
		
		this.createAccount.add(addAccount);
		this.removeAccount.add(delAccount);
	}

	
	public void frameSetup() {
		this.frame.setBounds(300, 300, 300, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
		this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
    		@Override
    		public void windowClosing(java.awt.event.WindowEvent windowEvent) {
    			operator.close();
    			System.exit(0);	
    		}
    	});
	}
	
	public void mainPanelSetup() {
		this.mainPanel.setLayout(new GridLayout(3, 1));
		
	}
	
	public void panelSetup() {
		this.namePanel.setLayout(new GridLayout(2, 1));
		this.codePanel.setLayout(new GridLayout(2, 1));
	}
	
	public void textFieldSetup() {
		this.name.setPreferredSize(new Dimension(100, 30));
		this.code.setPreferredSize(new Dimension(100, 30));
	}
	
	public void buttonSetup() {
		this.addAccount.setPreferredSize(new Dimension(100, 30));
		this.addAccount.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				boolean ret = false;
				ret = operator.add(name.getText(), code.getText());
				if(ret) {
					Utils.print("OK");
				}else {
					Utils.print("NO");
				}
			}
		});
		this.delAccount.setPreferredSize(new Dimension(100, 30));
		this.delAccount.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				boolean ret = false;
				ret = operator.remove(name.getText());
				if(ret) {
					Utils.print("OK");
				}else {
					Utils.print("NO");
				}
			}
		});
	}
	
}
