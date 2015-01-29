package com.lux.trump.client.presenter;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.lux.trump.client.TrumpServiceAsync;
import com.lux.trump.client.event.EnterRoomEvent;
import com.lux.trump.shared.RoomDTO;
import com.lux.trump.shared.UserGameInfoDTO;

public class GamePresenter implements Presenter {
	
	private final TrumpServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	private String game;
	private ArrayList<RoomDTO> roomList;
	private int selectedRoom = -1;
	
	public GamePresenter(TrumpServiceAsync rpcService, HandlerManager eventBus, Display display, String game) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = display;
		this.game = game;
	}
	
	public interface Display {
		Widget asWidget();
		void setGameName(String game);
		void setRoomList(ArrayList<RoomDTO> roomList);
		@SuppressWarnings("rawtypes")
		HasSelectionHandlers getList();
		void setSelectedRoomInfo(String info);
		HasClickHandlers enterButton();
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		display.setGameName(game);
		fetchRoomList();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void bind() {
		display.getList().addSelectionHandler(new SelectionHandler() {

			@Override
			public void onSelection(SelectionEvent event) {
				selectedRoom = new Integer(((TreeItem)(event.getSelectedItem())).getText()).intValue();
				for (RoomDTO room : roomList) {
					if (room.id == selectedRoom) {
						String info = "Users:\n";
						for (UserGameInfoDTO user: room.users) {
							info += "\t" + user.username + "\n";
						}
						display.setSelectedRoomInfo(info);
					}
				}
			}
			
		});
		
		display.enterButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedRoom >= 0)
					eventBus.fireEvent(new EnterRoomEvent(selectedRoom));
			}
			
		});
	}
	
	private void fetchRoomList() {
		rpcService.roomList(game, new AsyncCallback<ArrayList<RoomDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<RoomDTO> result) {
				roomList = result;
				display.setRoomList(result);
			}
			
		});
	}

}
