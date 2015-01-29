package com.lux.trump.shared;

import java.io.Serializable;

public class Message implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4225153476004869493L;
		public Message() {
			
		}
		
		public Message(String sender, String content) {
			this.sender = sender;
			this.content = content;
			
			this.read = false;
		}
		
		public String sender;
		public String content;
		public String type;
		public boolean read;
	}