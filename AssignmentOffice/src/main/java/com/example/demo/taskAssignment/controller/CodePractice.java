package com.example.demo.taskAssignment.controller;

import java.util.Arrays;
import java.util.List;

public class CodePractice {

	public static void main(String[] arg) {
		List<String> list= Arrays.asList("Ruchi","Amit","Gulshan","Shankar","Suraj");
		list.stream().forEach(name->{
			
			if(name.startsWith("S")) {
				return ;
			}
			System.out.println(name);
		});
	
	}
	
	
}




