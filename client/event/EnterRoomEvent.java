package com.lux.trump.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EnterRoomEvent extends GwtEvent<EnterRoomEventHandler> {
	public static Type<EnterRoomEventHandler> TYPE = new Type<EnterRoomEventHandler>();
	private int roomID;
	
	public EnterRoomEvent(int roomID) {
		this.roomID = roomID;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<EnterRoomEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EnterRoomEventHandler handler) {
		handler.onEnterRoom(this);
	}
	
	public int getID() {return roomID;}

}
