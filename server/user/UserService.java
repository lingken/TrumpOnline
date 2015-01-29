package com.lux.trump.server.user;

public class UserService {
	private static class InstanceHolder {
		private static final UserService instance = new UserService();
	}
	
	public static UserService getInstance() {
		return InstanceHolder.instance;
	}
	
	private UserService() {
		
	}
}
