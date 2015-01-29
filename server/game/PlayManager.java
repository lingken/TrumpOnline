package com.lux.trump.server.game;

import java.util.ArrayList;

import org.w3c.dom.Element;

import com.lux.trump.shared.GameAction;

public class PlayManager extends GameManager {

	@Override
	void dealWithAction(GameAction action) {
		if (action.type.equals("play")) {
			String[] contents = action.content.split(" ");
			if (contents[0].equals(this.service.users.get(currentPlayer).username)) {
				String[] cards = contents[1].split(";");
				
				// should be placed after each round!
				//if (scoreRole(this.service.users.get(currentPlayer), this.currentPlayer)) score(cards);
				
				for (String card: cards) {
					this.service.users.get(currentPlayer).deck.removeOne(card);
				}
			}
			
			this.service.addGlobalAction(action);
		}
		else if (action.type.equals("play_discard")) {
			this.service.addGlobalAction(action);
		}
		
		
		timeout = false;
		notify();
	}
	
	private void score(String[] cards) {
		for (String card: cards) {
			for (ScoreItem item: this.service.attributes.scores) {
				if (card.endsWith(item.term)) {
					this.service.attributes.score += item.score;
				}
			}
		}
	}
	
	private void score(String[] cards, int multi) {
		for (String card: cards) {
			for (ScoreItem item: this.service.attributes.scores) {
				if (card.endsWith(item.term)) {
					this.service.attributes.score += item.score * multi;
				}
			}
		}
	}
	
	private boolean scoreRole(UserInfo user, int id) {
		if (! user.role.equals(scoreRole)) {
			return false;
		}
		
		if (this.service.attributes.diagonal) {
			id = (id + 2) % 4;
			UserInfo another = this.service.users.get(id);
			if (! another.role.equals(scoreRole)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void run() {
		String start = element.getAttribute("start");
		
		currentPlayer = 0;
		
		if ("role".equals(start)) {
			String role = element.getAttribute("role");
			
			for (UserInfo user: this.service.users) {
				if (user.role.equals(role)) {
					currentPlayer = this.service.users.indexOf(user);
					break;
				}
			}
		}
		
		discard = element.getAttribute("discard").equals("true") ? true : false;
		ending = element.getAttribute("ending");
		score = element.getAttribute("score").equals("true") ? true: false;
		if (score) {
			scoreRole = element.getAttribute("score_role");
		}
		finalRound = element.getAttribute("final_round").equals("true");
		
		while (true) {
			this.service.addGlobalAction("play_turn", this.service.users.get(currentPlayer).username);
			
			
			timeout = true;
			try {
				this.wait(this.service.attributes.playSecond);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (timeout) {
				randomPlay();
			}
			
			boolean finish = false;
			if (ending.equals("one_finish")) {
				if (this.service.users.get(currentPlayer).deck.empty()) {
					finish = true;
					this.service.attributes.firstFinishRole = this.service.users.get(currentPlayer).role;
				}
			}
			else if (ending.equals("all_finish")) {
				finish = true;
				for (UserInfo user: this.service.users) {
					if (! user.deck.empty()) {
						finish = false;
						break;
					}
				}
			}
			
			if (finish) {
				// TODO judge the winner
				if (this.finalRound) {
					Element finalElement = (Element) element.getElementsByTagName("final").item(0);
					String deck = finalElement.getAttribute("deck");
					
					String[] cards = (String[]) this.service.attributes.findDeck(deck).cards.toArray();
					
					// TODO judge the card types
					int multi = 1;
					
					this.score(cards, multi);
				}
				
				break;
			}
			
			// TODO judge the next player
		}
		
		this.service.nextStatus();
	}
	
	// TODO
	private void randomPlay() {
		
	}
	
	private boolean timeout;
	
	private GameService service;
	private Element element;
	
	private boolean discard;
	private String ending;
	private boolean score;
	private String scoreRole;
	private boolean finalRound;
	
	private int currentPlayer;
	
	public PlayManager(GameService service, Element element) {
		this.service = service;
		this.element = element;
	}

}
