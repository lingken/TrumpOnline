package com.lux.trump.server.game;

import java.util.ArrayList;

import org.w3c.dom.Element;

import com.lux.trump.shared.GameAction;

public class WagerManager extends GameManager {

	@Override
	void dealWithAction(GameAction action) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void run() {
		String type = element.getAttribute("type");
		
		if ("wager".equals(type)) {
			ArrayList<UserInfo> winners = new ArrayList<UserInfo>();
			ArrayList<UserInfo> losers = new ArrayList<UserInfo>();
			
			String ret = new String();
			for (UserInfo user: this.service.users) {
				if (! "".equals(ret)) {
					ret += ";";
				}
				if (user.role.equals(this.service.attributes.firstFinishRole)) {
					winners.add(user);
					ret += "W";
				}
				else {
					losers.add(user);
					ret += "L";
				}
			}
			
			int winWager = 0, loseWager = 0;
			if (winners.size() < losers.size()) {
				winWager = this.service.attributes.wager * 2;
				loseWager = this.service.attributes.wager;
			}
			else {
				winWager = this.service.attributes.wager;
				loseWager = this.service.attributes.wager * 2;
			}
			
			ret.replaceAll("W", Integer.toString(winWager));
			ret.replaceAll("L", Integer.toString(-loseWager));
			
			this.service.addGlobalAction("wager", ret);
			
			for (UserInfo user: winners) {
				user.wager(winWager);
			}
			for (UserInfo user: losers) {
				user.wager(- loseWager);
			}
		}
		
		this.service.nextStatus();
	}
	
	private GameService service;
	private Element element;
	
	public WagerManager(GameService service, Element element) {
		this.service = service;
		this.element = element;
	}

}
