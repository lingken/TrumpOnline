package com.lux.trump.server.game;

import java.util.ArrayList;

import com.lux.trump.shared.GameAction;

public class UserInfo {
	public Deck deck = new Deck();
	public String role;
	public String username;
	public String xCard;
	public ActionQueue actionQueue;
	public boolean dealFinish = true;
	
	public class ActionQueue {
		private ArrayList<GameAction> actions = new ArrayList<GameAction>();
		
		public synchronized void appendAction(GameAction action) {
			actions.add(action);
		}
		
		public synchronized ArrayList<GameAction> fetchActions() {
			ArrayList<GameAction> ret = (ArrayList<GameAction>) actions.clone();
			actions.clear();
			return ret;
		}
	}
	
	public UserInfo(String username) {
		this.username = username;
	}
	
	// TODO
	public void wager(int wager) {
		
	}
}
