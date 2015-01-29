package com.lux.trump.client.presenter;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder.Case;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Element;
import com.lux.trump.shared.GameAction;
import com.lux.trump.shared.RealTimeGameUpdateDTO;
 
public class DealManager extends GameManager {
	RoomPresenter roomPresenter = null;
	Element procedureElement = null;
	int indexOfCardToBeAdd = -1;
	String [] cards;
	ArrayList<String> publicDisplayCards = new ArrayList<String>();
	ArrayList<String> userCardsRecord[] = null;
	String [] content;
	String lastAuditPlayer = "";
	int lastAuditCardNum = 0;
	String lastAuditCardColor = "";//S, H, D, C
	int primaryCardNumber[] = new int [5]; // 0:king 1:H 2:S 3:D 4:C
	private enum ActionType{
		audit_in_deal, deal, set_primary_color, set_role;
		private static ActionType getActionType(String type){
			return valueOf(type);
		}
	}
	public DealManager(RoomPresenter presenter, Element element) {
		super(presenter, element);
		roomPresenter = presenter;
		procedureElement = element;
		userCardsRecord = roomPresenter.userCardsRecord;
		auditListen();
	}

	@Override
	public void dealWithAction(GameAction action) {
		content = action.content.split(" ");
		switch(ActionType.getActionType(action.type)){
		case set_primary_color:
			roomPresenter.primaryColor = content[0];
			break;		
		case set_role:
			if (content[0].equals(roomPresenter.username)){
				roomPresenter.userrole = content[1];
				roomPresenter.display.setUserRole(roomPresenter.getIndexByUsername(roomPresenter.username), content[1]);
			}
			break;
		case deal:
			cards = content[1].split(";");
			indexOfCardToBeAdd = 0;
			Timer addCardTimer = new Timer() {
				
				@Override
				public void run() {
					if (indexOfCardToBeAdd >= cards.length){
						//deal finish
						GameAction deal_finishGameAction = new GameAction("deal_finish", roomPresenter.username, 0);
						ArrayList<GameAction> updateDTOList = new ArrayList<GameAction>();
						updateDTOList.add(deal_finishGameAction);
						RealTimeGameUpdateDTO updateDTO = new RealTimeGameUpdateDTO(updateDTOList);
						roomPresenter.rpcService.updateRealTime(roomPresenter.roomID, updateDTO, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								
							}
						});
						this.cancel();
						return;
					}
					else{
						//deal
						//Problem: the sequence of the players(by using getIndexByUsername)
						userCardsRecord[roomPresenter.getIndexByUsername(roomPresenter.username)].add(cards[indexOfCardToBeAdd]);
						publicDisplayCards.add(cards[indexOfCardToBeAdd]);
						roomPresenter.display.setUserCards(userCardsRecord);
						if (content[2].equals("true")){
							roomPresenter.display.setPublicDisplayCards(publicDisplayCards);
						}
						indexOfCardToBeAdd ++;
					}
				}
			};
			addCardTimer.scheduleRepeating(1000);
			checkAudit();
			break;
		case audit_in_deal:
			lastAuditPlayer = content[0];
			lastAuditCardColor = content[2];
			lastAuditCardNum = Integer.valueOf(content[1]);
			checkAudit();
			break;
		default:
			break;
		}
	}
	
	private void checkAudit(){
		//set, counter, no
		//result-based
		//every two cards will set it?
		//color in gameaction is S, D, H, C
		if (lastAuditCardNum >= 2){
			for (int i = 0; i < 5; i ++){
				setAuditButtonVisible(false, i);
			}
			return;
		}
//		int king = 0;
//		int spadePrimary = 0;
//		int heartPrimary = 0;
//		int clubPrimary = 0;
//		int diamondPrimary = 0;
		
		for (int i = 0; i < userCardsRecord.length; i ++){
			String card = userCardsRecord[roomPresenter.getIndexByUsername(roomPresenter.username)].get(i);
			char color = card.charAt(0);
			if (color == 'B' || color == 'L'){
				primaryCardNumber[0] ++;
				continue;
			}
			String number = card.substring(1);
			if (number.equals(roomPresenter.primaryNumber)){
				switch(color){
				case 'H': primaryCardNumber[1] ++; break;
				case 'S': primaryCardNumber[2] ++; break;
				case 'D': primaryCardNumber[3] ++; break;
				case 'C': primaryCardNumber[4] ++; break;
				default: break;
				}
			}
		}
		if(lastAuditCardNum == 1){
			for (int i = 0; i < 5; i ++){
				if (primaryCardNumber[i] >= 2){
					setAuditButtonVisible(true, i);
				}
			}
			return;
		}
		if (lastAuditCardNum == 0){
			for (int i = 0; i < 5; i ++){
				if (primaryCardNumber[i] >= 1){
					setAuditButtonVisible(true, i);
				}
			}
			return;
		}
	}
	
	private void setAuditButtonVisible(boolean isVisible, int primaryType){
		switch (primaryType) {
		case 0: roomPresenter.display.setAuditNoneButtonVisible(isVisible); break;
		case 1: roomPresenter.display.setAuditHeartButtonVisible(isVisible); break;
		case 2: roomPresenter.display.setAuditSpadeButtonVisible(isVisible); break;
		case 3: roomPresenter.display.setAuditDiamondButtonVisible(isVisible); break;
		case 4:	roomPresenter.display.setAuditClubButtonVisible(isVisible); break;
		default: break;
		}
	}
	
	private void auditListen(){
		roomPresenter.display.getAuditClubButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				realTimeAuditUpdate(4);
			}
		});
		roomPresenter.display.getAuditDiamondButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				realTimeAuditUpdate(3);
			}
		});
		roomPresenter.display.getAuditSpadeButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				realTimeAuditUpdate(2);
			}
		});
		roomPresenter.display.getAuditHeartButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				realTimeAuditUpdate(1);
			}
		});
		roomPresenter.display.getAuditNoneButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				realTimeAuditUpdate(0);
			}
		});
		
	}
	private GameAction generateAuditAction(int colorIndex){
		String actionContent = roomPresenter.username;
		switch (colorIndex) {
		case '0': actionContent += (" " + primaryCardNumber[colorIndex] + " " + "B"); break;
		case '1': actionContent += (" " + primaryCardNumber[colorIndex] + " " + "H"); break;
		case '2': actionContent += (" " + primaryCardNumber[colorIndex] + " " + "S"); break;
		case '3': actionContent += (" " + primaryCardNumber[colorIndex] + " " + "D"); break;
		case '4': actionContent += (" " + primaryCardNumber[colorIndex] + " " + "C"); break;
		default: break;
		}
		GameAction audit_in_dealGameAction = new GameAction("audit_in_deal", actionContent, 0);
		return audit_in_dealGameAction;
	}
	private void realTimeAuditUpdate(int colorIndex){
		ArrayList<GameAction> updateArrayList = new ArrayList<GameAction>();
		updateArrayList.add(generateAuditAction(colorIndex));
		RealTimeGameUpdateDTO updateDTO = new RealTimeGameUpdateDTO(updateArrayList);
		roomPresenter.rpcService.updateRealTime(roomPresenter.roomID, updateDTO, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Void result) {
				// conceal the audit button
				for (int i = 0; i < primaryCardNumber.length; i ++){
					setAuditButtonVisible(false, i);
				}
			}
		});
	}
}
