package com.lux.trump.server.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestGetXML {
	public static String fetchXML(){
		//test for Lincoln code
		String content = "";
		String s;
		FileInputStream is = null;
		InputStreamReader ir;
		BufferedReader in;
		try {
			is=new FileInputStream("E:/KeplerWorkSpace/test.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ir=new InputStreamReader(is);
		in=new BufferedReader(ir);
		try {
			while((s=in.readLine())!=null){
				content += s;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(content);
		return content;
	}
}
