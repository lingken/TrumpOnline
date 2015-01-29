package com.lux.trump.client.presenter;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.lux.trump.client.TrumpServiceAsync;
import com.lux.trump.client.event.GotoGameEvent;
import com.lux.trump.client.event.LogOutEvent;

public class HomePresenter implements Presenter {
	private ArrayList<String> gameList;
	
	public interface Display {
		Widget asWidget();
		void setUsername(String username);
		HasClickHandlers getLogOutButton();
		HasClickHandlers getList();
		int getClickedRow(ClickEvent event);
		void setGameList(ArrayList<String> gameList);
	}
	
	private final TrumpServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	
	public HomePresenter(TrumpServiceAsync rpcService, HandlerManager eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		String username = Cookies.getCookie("user");
		this.display.setUsername(username);
		fetchGameList();
	}

	private void bind() {
		this.display.getLogOutButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogOutEvent());
			}
			
		});
		
		this.display.getList().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int row = display.getClickedRow(event);
				eventBus.fireEvent(new GotoGameEvent(gameList.get(row)));
			}
			
		});
	}
	
	private void fetchGameList() {
		rpcService.gameList(new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				gameList = result;
				display.setGameList(result);
			}
			
		});
	}
}
