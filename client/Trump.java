package com.lux.trump.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

public class Trump implements EntryPoint {

	@Override
	public void onModuleLoad() {
		TrumpServiceAsync rpcService = GWT.create(TrumpService.class);
		HandlerManager eventBus = new HandlerManager(null);
		AppController appViewer = new AppController(rpcService, eventBus);
		appViewer.go(RootPanel.get());
	}
	
}