package com.lux.trump.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class GotoGameEvent extends GwtEvent<GotoGameEventHandler> {
	public static Type<GotoGameEventHandler> TYPE = new Type<GotoGameEventHandler>();
	private String game;
	
	public GotoGameEvent(String game) {
		this.game = game;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GotoGameEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GotoGameEventHandler handler) {
		handler.onGotoGameEvent(this);
	}
	
	public String getName() { return game;}

}
