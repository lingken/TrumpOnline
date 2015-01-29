package com.lux.trump.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4138069381160415913L;
	
	public ChatDTO() {
		
	}
	
	public ArrayList<Message> messages = new ArrayList<Message>();
	
	public void appendMessage(String user, String content) {
		messages.add(new Message(user, content));
	}
}
