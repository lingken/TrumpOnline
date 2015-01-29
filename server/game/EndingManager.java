package com.lux.trump.server.game;

import java.util.HashMap;

import org.w3c.dom.Element;

import com.lux.trump.server.game.GameAttribute.Role;
import com.lux.trump.shared.GameAction;

public class EndingManager extends GameManager {

	@Override
	void dealWithAction(GameAction action) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void run() {
		String type = element.getAttribute("type");
		if (type.equals("check_role"))
		{
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			
			for (UserInfo user: this.service.users) {
				String role = user.role;
				
				if (! map.containsKey(role)) {
					map.put(role, 1);
				}
				else {
					int count = map.get(role);
					map.put(role, count + 1);
				}
			}
			
			boolean valid = true;
			for (Role role: this.service.attributes.roles) {
				if (! map.containsKey(role.roleName)) {
					valid = false;
					break;
				}
				else if (map.get(role.roleName) != role.number) {
					valid = false;
					break;
				}
			}
			
			if (! valid) {
				this.service.restart();
				this.service.addGlobalAction("restart", "");
			}
			else {
				this.service.nextStatus();
			}
		}
	}
	
	private GameService service;
	private Element element;
	
	public EndingManager(GameService service, Element element) {
		this.service = service;
		this.element = element;
	}

}
