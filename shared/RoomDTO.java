package com.lux.trump.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3104004442866573038L;
	
	public RoomDTO() {
		
	}
	
	public RoomDTO(int roomID) {
		this.id = roomID;
	}
	
	public int id;  
	public ArrayList<UserGameInfoDTO> users = new ArrayList<UserGameInfoDTO>();
	public GameInfo gameinfo;
	
	public void appendUser(UserGameInfoDTO username) {
		users.add(username);
	}

	public ChatDTO chatInRoom() {
		// TODO Auto-generated method stub
		ChatDTO chat = new ChatDTO();
		chat.appendMessage("lmx", "hello"); 
		return chat; 
	}

	public ArrayList<UserGameInfoDTO> getUserInRoom() {
		// TODO Auto-generated method stub
		return users;
	}
}
