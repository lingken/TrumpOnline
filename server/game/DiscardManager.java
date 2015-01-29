package com.lux.trump.server.game;

import org.w3c.dom.Element;

import com.lux.trump.shared.GameAction;

public class DiscardManager extends GameManager {

	@Override
	void dealWithAction(GameAction action) {
		if ("discard".equals(action.type)) {
			String[] contents = action.content.split(" ");
			String username = contents[0];
			if (username.equals(currentUser)) {
				String[] cards = contents[1].split(";");
				for (UserInfo user: this.service.users) {
					if (! user.username.equals(username)) continue;
					for (String card: cards) {
						user.deck.removeOne(card);
						this.service.attributes.findDeck(deck).add(card);
					}
				}
			}
		}
		
		String ret = action.content + " " + deck + " " + visible;
		this.service.addGlobalAction("discard", ret);
		
		timeout = false;
		this.notify();
	}
	
	private boolean timeout;
	private String deck;
	private String visible;
	private String currentUser;
	
	@Override
	public void run() {
		String type = element.getAttribute("type");
		deck = element.getAttribute("deck");
		visible = element.getAttribute("visible").equals("true") ? "true" : "false"; 
		int cardNum = new Integer(element.getAttribute("card_num"));
		
		if ("role".equals(type)) {
			String role = element.getAttribute("role");
			for (UserInfo user: this.service.users) {
				if (role.equals(user.username)) {
					this.service.addGlobalAction("discard_turn", user.username + " " + Integer.toString(cardNum));
					currentUser = user.username;
					
					timeout = true;
					try {
						this.wait(this.service.attributes.discardSecond);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (timeout) {
						randomDiscard(user, cardNum);
					}
				}
			}
		}
		
		this.service.nextStatus();
	}
	
	private void randomDiscard(UserInfo user, int cardNum) {
		String cardList = new String();
		
		for (int i=0; i<cardNum; i++) {
			if (! cardList.equals("")) cardList += ";";
			cardList += user.deck.dealOne();
		}
		
		this.service.addGlobalAction("discard", user.username + " " + cardList + " " + deck + " " + visible);
		
		String[] cards = cardList.split(";");
		for (String card: cards) {
			this.service.attributes.findDeck(deck).add(card);
		}
	}
	
	private GameService service;
	private Element element;
	
	public DiscardManager(GameService service, Element element) {
		this.service = service;
		this.element = element;
	}

}
