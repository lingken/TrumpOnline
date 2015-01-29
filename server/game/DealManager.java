package com.lux.trump.server.game;

import java.util.TimerTask;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lux.trump.shared.GameAction;

public class DealManager extends GameManager {
	
	@Override
	public void run() {
		type = element.getAttribute("type");
		int cardNumber = Integer.parseInt(element.getAttribute("card_num"));
		String visible = element.getAttribute("visible");
		if (!visible.equals("true")) visible = "false";
		String audit = element.getAttribute("audit");
		if (!audit.equals("true")) audit = "false";
		if (audit.equals("true")) {
			auditElement = (Element) element.getElementsByTagName("audit").item(0);
		}
		
	//	System.out.println(audit);
		
		Deck mainDeck = this.service.attributes.findDeck("main");
		
		if (type.equals("average"))
		{
			for (UserInfo user: this.service.users) {
				user.dealFinish = false;
			}
			for (UserInfo user: this.service.users)
			{
				String[] cardList = new String[cardNumber];
				for (int i=0; i<cardNumber; i++)
				{
					String card = mainDeck.dealOne();
					cardList[i] = card;
				}
				
				user.deck.append(cardList);
				String ret = new String();
				for (String card: cardList) {
					if (! "".equals(ret)) ret += ";";
					ret += card;
				}
				this.service.addGlobalAction("deal", user.username + " " + ret + " " + visible);
			}
		} 
		else if (type.equals("role"))
		{
			String role = element.getAttribute("role");
			for (UserInfo user: this.service.users) {
				if (! user.username.equals(role)) continue;
				
				user.dealFinish = false;
				
				String[] cardList = new String[cardNumber];
				for (int i=0; i<cardNumber; i++) {
					String card = mainDeck.dealOne();
					cardList[i] = card;
				}
				
				user.deck.append(cardList);
				String ret = new String();
				for (String card: cardList) {
					if (! "".equals(ret)) ret += ";";
					ret += card;
				}
				this.service.addGlobalAction("deal", user.username + " " + ret + " " + visible);
			}
		}
	}
	
	private GameService service;
	private Element element;
	private Element auditElement = null;
	
	private int cardNumber = 0;
	private String winner;
	private String color;
	
	public String type;
	
	public DealManager(GameService service, Element element) {
		this.service = service;
		this.element = element;
	}

	@Override
	void dealWithAction(GameAction action) {
		if (action.type.equals("audit_in_deal")) {
			if (auditElement != null) {
				String[] contents = action.content.split(" ");
				int newCardNum = new Integer(contents[1]);
				if (newCardNum <= cardNumber) return;
				winner = contents[0];
				cardNumber = newCardNum;
				color = contents[2];
			}
		}
		else if (action.type.equals("deal_finish")) {
			String username = action.content;
			for (UserInfo user: this.service.users) {
				if (user.username.equals(username)) {
					user.dealFinish = true;
				}
			}
			if (checkAllFinish()) {
				finish();
				this.service.nextStatus();
			}
		}
	}
	
	private boolean checkAllFinish() {
		for (UserInfo user: this.service.users) {
			if (! user.dealFinish) {
				return false;
			}
		}
		return true;
	}
	
	private void finish() {
		if (auditElement == null) return;
		Element ending = (Element) auditElement.getElementsByTagName("ending").item(0);
		NodeList actionList = ending.getElementsByTagName("action");
		
		for (int i=0; i<actionList.getLength(); i++) {
			Element action = (Element) actionList.item(i);
			String type = action.getAttribute("type");
			if (type.equals("set_primary_color")) {
				this.service.attributes.primary_color = color;
				this.service.addGlobalAction("set_primary_color", color);
			}
			else if (type.equals("set_role")) {
				boolean firstRound = action.getAttribute("first_round_only").equals("true");
				if (! firstRound || (firstRound && this.service.attributes.firstRound)) {
					String value = action.getAttribute("value");
					for (UserInfo user: this.service.users) {
						if (user.username.equals(winner)) {
							user.role = value;
							this.service.addGlobalAction("set_role", user.username + " " + value);
						}
						else if (user.role.equals(value)) {
							user.role = this.service.attributes.defaultRole;
							this.service.addGlobalAction("set_role", user.username + " " + user.role);
						}
					}
				}
			}
		}
	}
}
