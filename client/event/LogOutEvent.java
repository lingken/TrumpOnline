package com.lux.trump.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LogOutEvent extends GwtEvent<LogOutEventHandler> {
	public static Type<LogOutEventHandler> TYPE = new Type<LogOutEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LogOutEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogOutEventHandler handler) {
		handler.onLogOut(this);
	}

}
