package com.lux.trump.server.game;

import java.util.ArrayList;

public class GameAttribute {
	public final int auditSecond = 5;
	public final int discardSecond = 10;
	public final int playSecond = 15;
	
	public boolean firstRound;
	public boolean diagonal;
	public boolean roleCrossRounds;
	
	public String gameName;
	public int userNumber;
	
	public ArrayList<Role> roles = new ArrayList<Role>();
	public ArrayList<Deck> decks = new ArrayList<Deck>();
	public ArrayList<ScoreItem> scores = new ArrayList<ScoreItem>();
	
	public String defaultRole;
	
	public XCardInfo xcardInfo = new XCardInfo();
	
	public String primary_color;
	
	public int wager;
	
	public int score;
	
	public String firstFinishRole;
	
	public class Role {
		String roleName;
		int number;
		
		public Role(String roleName, int number) {
			this.roleName = roleName;
			this.number = number;
		}
	}
	
	public void appendRole(String roleName, int number) {
		roles.add(new Role(roleName, number));
	}
	
	public Deck findDeck(String name) {
		for (Deck deck : decks) {
			if (deck.name.equals(name))
				return deck;
		}
		return null;
	}
}
