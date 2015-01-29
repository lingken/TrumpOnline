package com.lux.trump.logic;


public class SingleCardType {
	boolean isxcard;
	boolean isprimary;
	
	String xcard;    // number
	String primary;  // color
	public SingleCardType(){
		isxcard = false;
		isprimary = false;
	}
	public SingleCardType(String xcard, String primary){
		this.xcard = xcard;
		this.primary = primary;
		this.isprimary = true;
		this.isxcard = true;
	}
	
	String type(String card){
		String str = null;
		if (card.equals("B") || card.equals("L"))
			str = card;
		else{
			str = card.substring(0, 1);
		}
		if (this.isxcard == true && this.isprimary == true){
			if (card.substring(1).equals(xcard))
				str = this.primary;
			if (card.equals("B") || card.equals("L"))
				str = this.primary;  
		}
		return str;
	}
	
	boolean typeequal(String s1, String s2){
	//	System.out.println(type(s1)+" "+type(s2));
		return type(s1).equals(type(s2));
	}
}
