package com.bancagv.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	static public List<String> split(String input, char ch){
		List<String> result = new ArrayList<String>();
		String temp = "";
		
		for(int i = 0; i < input.length(); i++) {
			if(input.charAt(i) == ch && !(temp.isEmpty())) {
				result.add(temp);
				temp = "";
			}else {
				temp += input.charAt(i);
			}
		}
		if(!(temp.isEmpty())) {
			result.add(temp);
			temp = "";
		}
		
		return result;
	}
}
