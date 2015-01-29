package com.lux.trump.client.view;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.lux.trump.client.presenter.*;
import com.lux.trump.client.presenter.RoomPresenter.Status;
import com.lux.trump.shared.UserGameInfoDTO;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextArea;

public class RoomView extends Composite implements RoomPresenter.Display {
	private Label roomID = new Label("");
	private FlexTable userTable = new FlexTable();
	private final TextArea textArea = new TextArea();
	
	public RoomView() {
		
		FlowPanel flowPanel = new FlowPanel();
		initWidget(flowPanel);
		
		
		flowPanel.add(roomID);
		
		
		flowPanel.add(userTable);
		textArea.setReadOnly(true);
		
		flowPanel.add(textArea);
		textArea.setSize("161px", "189px");
	}

	@Override
	public void setRoomNumber(int roomID) {
		this.roomID.setText(Integer.toString(roomID));
	}

	@Override
	public void setUsers(ArrayList<UserGameInfoDTO> users) {
		userTable.removeAllRows();
		
		userTable.setText(0, 0, "username");
		userTable.setText(0, 1, "title");
		userTable.setText(0, 2, "#win");
		userTable.setText(0, 3, "#played");
		
		int i = 1;
		for (UserGameInfoDTO user: users) {
			userTable.setText(i, 0, user.username);
			userTable.setText(i, 1, user.title);
			userTable.setText(i, 2, Integer.toString(user.winGame));
			userTable.setText(i, 3, Integer.toString(user.totalGame));
			i++;
		}
	}

	@Override
	public HasClickHandlers getReadyButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void appendCharBoard(String content) {
		textArea.setText(textArea.getText() + content);
	}

	@Override
	public void setStatus(Status status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getCardPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getSelectedCard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getPlayButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUserCards(ArrayList<String>[] userCards) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserRole(int index, String rolename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlayButtonVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getAuditPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAuditDiamondButtonVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAuditSpadeButtonVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAuditHeartButtonVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAuditClubButtonVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAuditWagerButtonVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWin(boolean isWin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAuditNoneButtonVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasClickHandlers getAuditDiamondButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getAuditSpadeButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getAuditHeartButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getAuditClubButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getAuditNoneButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getAuditWagerButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPublicDisplayCards(ArrayList<String> publicDisplayCards) {
		// TODO Auto-generated method stub
		
	}

}
