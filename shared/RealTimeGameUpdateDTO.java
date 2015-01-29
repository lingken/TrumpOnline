package com.lux.trump.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class RealTimeGameUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 190821498097854499L;
	
	public ArrayList<GameAction> gameActions = new ArrayList<GameAction>();
	
	public RealTimeGameUpdateDTO() {
		
	}
	
	public RealTimeGameUpdateDTO(ArrayList<GameAction> list) {
		gameActions = list;
	}
	
	public void appenGameAction(GameAction action) {
		gameActions.add(action);
	}
}
