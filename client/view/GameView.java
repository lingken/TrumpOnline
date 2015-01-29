package com.lux.trump.client.view;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.lux.trump.client.presenter.*;
import com.lux.trump.shared.RoomDTO;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Button;

public class GameView extends Composite implements GamePresenter.Display {
	private Label label = new Label("");
	private Tree tree = new Tree();
	private final TextArea textArea = new TextArea();
	private final Button button = new Button("New button");
	
	public GameView() {
		
		FlowPanel flowPanel = new FlowPanel();
		initWidget(flowPanel);
		
		
		flowPanel.add(label);
		
		
		tree.setAnimationEnabled(true);
		flowPanel.add(tree);
		
		flowPanel.add(textArea);
		textArea.setSize("152px", "185px");
		button.setText("Enter Room");
		
		flowPanel.add(button);
	}

	@Override
	public void setGameName(String game) {
		label.setText(game);
	}

	@Override
	public void setRoomList(ArrayList<RoomDTO> roomList) {
		for (RoomDTO room: roomList) {
			tree.addTextItem(Integer.toString(room.id));
		}
	}

	@Override
	public HasSelectionHandlers getList() {
		return tree;
	}

	@Override
	public void setSelectedRoomInfo(String info) {
		textArea.setText(info);
	}

	@Override
	public HasClickHandlers enterButton() {
		return button;
	}


}
