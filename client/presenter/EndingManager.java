package com.lux.trump.client.presenter;

import com.google.gwt.dev.WebServerPanel.RestartAction;
import com.google.gwt.xml.client.Element;
import com.lux.trump.shared.GameAction;

public class EndingManager extends GameManager {

	private RoomPresenter roomPresenter = null;
	private Element procedureElement;
	public EndingManager(RoomPresenter presenter, Element element) {
		super(presenter, element);
		roomPresenter = presenter;
		procedureElement = element;
	}

	@Override
	public void dealWithAction(GameAction action) {
		roomPresenter.restart();

	}

}
