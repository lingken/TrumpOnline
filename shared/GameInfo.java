package com.lux.trump.shared;

import java.io.Serializable;

public class GameInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3296602136854992952L;
	public GameInfo(){
		
	}

	public GameInfo(String name) {
		this.name = name;
		// TODO Auto-generated constructor stub
	}

	public String name;
	public String xml;
}
