package com.lux.trump.server.game;

import java.util.ArrayList;

public class XCardInfo {
	public String currentXCard;
	public ArrayList<String> levels = new ArrayList<String>();
	
	public void appendLevel(String level) {
		levels.add(level);
		this.currentXCard = level;
	}
}
