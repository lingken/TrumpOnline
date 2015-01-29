package com.lux.trump.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginSuccessEvent extends GwtEvent<LoginSuccessEventHandler> {
	public static Type<LoginSuccessEventHandler> TYPE = new Type<LoginSuccessEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LoginSuccessEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginSuccessEventHandler handler) {
		handler.onLoginSuccess(this);
	}

}
