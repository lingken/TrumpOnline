package com.lux.trump.client.presenter;

import com.google.gwt.xml.client.Element;
import com.lux.trump.shared.GameAction;

public abstract class GameManager {
	public RoomPresenter presenter;
	public Element element;
	
	public GameManager(RoomPresenter presenter, Element element) {
		this.presenter = presenter;
		this.element = element;
	}
	
	public abstract void dealWithAction(GameAction action);
}
