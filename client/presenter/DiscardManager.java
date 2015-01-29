package com.lux.trump.client.presenter;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Element;
import com.lux.trump.shared.GameAction;
import com.lux.trump.shared.RealTimeGameUpdateDTO;

public class DiscardManager extends GameManager {
	private RoomPresenter roomPresenter = null;
	private Element procedureElement = null;
	String content[];
	boolean isDiscarding = false;
	ArrayList<String> userCardsRecord = null; 
	private enum ActionType{
		discard_turn, discard;
		private static ActionType getActionType(String type){
			return valueOf(type);
		}
	}
	public DiscardManager(RoomPresenter presenter, Element element) {
		super(presenter, element);
		roomPresenter = presenter;
		procedureElement = element;
		userCardsRecord = roomPresenter.userCardsRecord[roomPresenter.getIndexByUsername(roomPresenter.username)];
		roomPresenter.display.getCardPanel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isDiscarding){
					checkDiscardCards();
				}
			}
		});
		roomPresenter.display.getDiscardButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ArrayList<String> cardsToBeDiscarded = roomPresenter.display.getSelectedCard();
				discardCard(cardsToBeDiscarded, roomPresenter.username);
				
				String actionContent = roomPresenter.username;
				actionContent += " ";
				for (int i = 0; i < cardsToBeDiscarded.size(); i ++){
					actionContent += cardsToBeDiscarded.get(i) + ";";
				}
				actionContent = actionContent.substring(0, actionContent.length() - 2); // omit the last semicolon		
				GameAction discardGameAction = new GameAction("discard", actionContent, 0);
				ArrayList<GameAction> gameActionList = new ArrayList<GameAction>();
				gameActionList.add(discardGameAction);
				RealTimeGameUpdateDTO updateDTO = new RealTimeGameUpdateDTO(gameActionList);
				roomPresenter.rpcService.updateRealTime(roomPresenter.roomID, updateDTO, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						roomPresenter.display.setDiscardButton(false);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
				
			}
		});
	}

	private void checkDiscardCards() {
		ArrayList<String> discardCardList = roomPresenter.display.getSelectedCard();
		if (discardCardList.size() == Integer.valueOf(content[1])){
			roomPresenter.display.setDiscardButton(true);
		}
		else{
			roomPresenter.display.setDiscardButton(false);
		}
		
	}

	@Override
	public void dealWithAction(GameAction action) {
		content = action.content.split(" ");
		switch(ActionType.getActionType(action.type)){
		case discard_turn:
			if(content[0].equals(roomPresenter.username)){
				isDiscarding = true;
			}
			else{
				isDiscarding = false;
			}
			break;
		case discard:
			String discardCardList[] = content[1].split(";");
			ArrayList<String> cardsToBeDiscarded = new ArrayList<String>();
			for (int i = 0; i < discardCardList.length; i ++){
				cardsToBeDiscarded.add(discardCardList[i]);
			}
			discardCard(cardsToBeDiscarded, content[0]);
			break;
		default:
			break;
		}

	}
	
	void discardCard(ArrayList<String> cardsToBeDiscarded, String username){
		ArrayList<String> thatUserCards = roomPresenter.userCardsRecord[roomPresenter.getIndexByUsername(username)]; 
		for (int i = 0; i < cardsToBeDiscarded.size(); i ++){
			for (int j = 0; j < thatUserCards.size(); j ++){
				if(thatUserCards.get(j).equals(cardsToBeDiscarded.get(i))){
					thatUserCards.remove(j);
					break;
				}
			}
		}
		roomPresenter.display.setUserCards(roomPresenter.userCardsRecord);	
	}

}
