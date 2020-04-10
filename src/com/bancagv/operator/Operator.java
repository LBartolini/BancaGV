package com.bancagv.operator;

import com.bancagv.operator.graphic.HomePage;

public class Operator {
	
	public Operator() {
		new HomePage(this);
	}

	public static void main(String[] args) {
		Operator o = new Operator();

	}

}
