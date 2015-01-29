package com.lux.trump.server.game;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lux.trump.shared.GameAction;

public class AuditManager extends GameManager {
	
	private GameService service;
	private Element status;
	
	private String winner;
	private int key = 0;
	
	public AuditManager(GameService service, Element status) {
		this.service = service;
		this.status = status;
	}

	@Override
	void dealWithAction(GameAction action) {
		if (action.type.equals("audit")) {
			String[] contents = action.content.split(" ");
			int key = new Integer(contents[1]);
			if (key > this.key) {
				this.key = key;
				winner = contents[0];
				
				this.service.addGlobalAction(action);
			}
			timeout = false;
			this.notify();
		}
	}
	
	private boolean timeout;
	
	@Override
	public void run() {
		Element element = status; 
		
		String turn = element.getAttribute("turn");
		String start = element.getAttribute("start");
		String mode = element.getAttribute("mode");

		int userNumber = this.service.attributes.userNumber;
		
		if (turn.equals("true")) {
			int startNumber = 0;
			
			if (start.equals("random"))
			{
				Random random = new Random();
				startNumber = random.nextInt(userNumber);
			}
			
			for (int j=0; j<userNumber; j++) {
				int i = (startNumber + j) % userNumber;
				this.service.addGlobalAction("audit_turn", this.service.users.get(i).username);
				
				timeout = true;
				try {
					this.wait(this.service.attributes.auditSecond * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (timeout) {
					this.service.addGlobalAction("audit", this.service.users.get(i).username + " -1");
				}
			}
		}
		
		NodeList list = element.getElementsByTagName("ending");
		if (list.getLength() > 0) {
			Element ending = (Element) list.item(0);
			
			NodeList actionList = ending.getElementsByTagName("action");
			for (int i=0; i<actionList.getLength(); i++) {
				Element action = (Element) actionList.item(i);
				String type = action.getAttribute("type");
				
				if (type.equals("set_wager")) {
					this.service.attributes.wager = key;
					this.service.addGlobalAction("set_wager", Integer.toString(key));
				}
				else if (type.equals("set_role")) {
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
		
		this.service.nextStatus();
	}

}
