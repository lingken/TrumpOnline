package com.lux.trump.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.lux.trump.client.event.EnterRoomEvent;
import com.lux.trump.client.event.EnterRoomEventHandler;
import com.lux.trump.client.event.GotoGameEvent;
import com.lux.trump.client.event.GotoGameEventHandler;
import com.lux.trump.client.event.LogOutEvent;
import com.lux.trump.client.event.LogOutEventHandler;
import com.lux.trump.client.event.LoginSuccessEvent;
import com.lux.trump.client.event.LoginSuccessEventHandler;
import com.lux.trump.client.presenter.GamePresenter;
import com.lux.trump.client.presenter.HomePresenter;
import com.lux.trump.client.presenter.LoginPresenter;
import com.lux.trump.client.presenter.Presenter;
import com.lux.trump.client.presenter.RoomPresenter;
import com.lux.trump.client.view.GameView;
import com.lux.trump.client.view.HomeView;
import com.lux.trump.client.view.LoginView;
import com.lux.trump.client.view.RoomView;

public class AppController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private final TrumpServiceAsync rpsService;
	private HasWidgets container;
	
	public AppController(TrumpServiceAsync rpcService, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpsService = rpcService;
		bind();
	}
	
	private void bind() {
		History.addValueChangeHandler(this);
		
		eventBus.addHandler(LoginSuccessEvent.TYPE, new LoginSuccessEventHandler() {
			@Override
			public void onLoginSuccess(LoginSuccessEvent event) {
				doLoginSuccess();
			}
		});
		
		eventBus.addHandler(LogOutEvent.TYPE, new LogOutEventHandler() {

			@Override
			public void onLogOut(LogOutEvent event) {
				doLogOut();
			}
			
		});
		
		eventBus.addHandler(GotoGameEvent.TYPE, new GotoGameEventHandler() {

			@Override
			public void onGotoGameEvent(GotoGameEvent event) {
				doGotoGame(event.getName());	
			}
			
		});
		
		eventBus.addHandler(EnterRoomEvent.TYPE,  new EnterRoomEventHandler() {

			@Override
			public void onEnterRoom(EnterRoomEvent event) {
				doEnterRoom(event.getID());
			}
			
		});
	}

	@Override
	public void go(HasWidgets container) {
		this.container = container;
		
		if ("".equals(History.getToken())) {
			if (Cookies.getCookie("user") == null) {
				History.newItem("login");
			}
			else {
				History.newItem("home");
			}
		}
		else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		
		if (token != null) {
			Presenter presenter = null;
			
			if (token.equals("home")) {
				presenter = new HomePresenter(this.rpsService, this.eventBus, new HomeView());
			}
			else if (token.equals("login")) {
				presenter = new LoginPresenter(this.rpsService, this.eventBus, new LoginView());
			}
			else if (token.startsWith("game")) {
				String[] tokens = token.split(":");
				if (tokens.length > 1)
					presenter = new GamePresenter(this.rpsService, this.eventBus, new GameView(), tokens[1]);
			}
			else if (token.startsWith("room")) {
				String[] tokens = token.split(":");
				if (tokens.length > 1)
					presenter = new RoomPresenter(this.rpsService, this.eventBus, new RoomView(), new Integer(tokens[1]));
			}
			
			if (presenter != null) {
				presenter.go(container);
			}
		}
	}
	
	private void doLoginSuccess() {
		History.newItem("home");
	}
	
	private void doLogOut() {
		Cookies.removeCookie("user");
		History.newItem("login");
	}
	
	private void doGotoGame(String game) {
		String bookmark = "game:" + game;
		History.newItem(bookmark);
	}
	
	private void doEnterRoom(int id) {
		String bookmark = "room:" + Integer.toString(id);
		History.newItem(bookmark);
	}

}
