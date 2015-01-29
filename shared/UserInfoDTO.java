package com.lux.trump.shared;

import java.io.Serializable;

public class UserInfoDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8024734389759824237L;

	public String username;
	
	public String birthday;
	public String gender;
	public String country;
	
	public String[] gameList;
	
	public String[] friends;
	
	public UserInfoDTO() {
		
	}
}
