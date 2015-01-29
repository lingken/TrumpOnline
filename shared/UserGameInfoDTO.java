package com.lux.trump.shared;

import java.io.Serializable;

public class UserGameInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8370501834271848888L;

	public UserGameInfoDTO() {
		
	}
	
	public UserGameInfoDTO(String username, String title, int winGame, int totalGame, boolean ready) {
		this.username = username;
		this.title = title;
		this.winGame = winGame;
		this.totalGame = totalGame;
		this.ready = ready;
	}
	
	public String username;
	public String gameName;
	public String title;
	public int winGame;
	public int totalGame;
	public boolean ready;
}
