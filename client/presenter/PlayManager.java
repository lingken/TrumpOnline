package com.lux.trump.client.presenter;

import java.util.ArrayList;

import java_cup.internal_error;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Element;
import com.lux.trump.shared.GameAction;
import com.lux.trump.shared.RealTimeGameUpdateDTO;

public class PlayManager extends GameManager {
	private RoomPresenter roomPresenter = null;
	private Element procedureElement = null;
	String content[];
	boolean isSelectingCard = false;
	boolean isAbleToDiscard;
	ArrayList<String> userCards = null;
	ArrayList<String> selectedCards = null;
	ArrayList<String> lastRoundPlayCards = new ArrayList<String>();
	ArrayList<String> baseCards = new ArrayList<String>();
	int hasPlayedNumber = 0;
	private enum ActionType{
		play_turn, play_discard, play, score;
		private static ActionType getActionType(String type){
			return valueOf(type);
		}
	}
	public PlayManager(RoomPresenter presenter, Element element) {
		super(presenter, element);
		roomPresenter = presenter;
		procedureElement = element;
		// parse to see if it is able to discard
		// maybe problems here
		Element playElement = (Element) procedureElement.getElementsByTagName("play").item(0);
		String discard = playElement.getAttribute("discard");
		userCards = roomPresenter.userCardsRecord[roomPresenter.getIndexByUsername(roomPresenter.username)];
		if (discard.equals("true")){
			isAbleToDiscard = true;
		}
		else{
			isAbleToDiscard = false;
		}
		
		roomPresenter.display.getCardPanel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				selectedCards = roomPresenter.display.getSelectedCard();
				if (checkCardsPlayable()){
					roomPresenter.display.setPlayButtonVisible(true);
				}
				else{
					roomPresenter.display.setPlayButtonVisible(false);
				}
				
			}
		});
		
		roomPresenter.display.getPlayButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String playActionContent = roomPresenter.username;
				playActionContent += " ";
				//selectedCards = roomPresenter.display.getSelectedCard();
				for (int i = 0; i < selectedCards.size(); i ++){
					playActionContent += selectedCards.get(i) + ";";
				}
				playActionContent = playActionContent.substring(0, playActionContent.length() - 2);
				GameAction playAction = new GameAction("play", playActionContent, 0);
				RealTimeGameUpdateDTO updateDTO = createUpdateDTO(playAction);
				roomPresenter.rpcService.updateRealTime(roomPresenter.roomID, updateDTO, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// deal with base card
						if (isAbleToDiscard){
							hasPlayedNumber = 1;
//							baseCards = (ArrayList<String>) selectedCards.clone(); // no use
							
						}
						else{
							hasPlayedNumber ++;
							if (hasPlayedNumber == 4){
								hasPlayedNumber = 0;
								baseCards.clear();
							}
//							if (hasPlayedNumber == 1){//  no use
//								baseCards = (ArrayList<String>) selectedCards.clone();
//							}
						}
						
						roomPresenter.display.setPlayButtonVisible(false);
						roomPresenter.display.setCancelButton(false);
						roomPresenter.display.setPublicDisplayCards(selectedCards);
						for (int i = 0; i < selectedCards.size(); i ++){
							for (int j = 0; j < userCards.size(); j ++){
								if (selectedCards.get(i).equals(userCards.get(j))){
									userCards.remove(j);
									break;
								}
							}
						}
						lastRoundPlayCards = selectedCards;
						roomPresenter.display.setUserCards(roomPresenter.userCardsRecord);
						roomPresenter.display.setPublicDisplayCards(lastRoundPlayCards);
					}
				});
				
			}
		});
		
		roomPresenter.display.getCancelButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				GameAction discardAction = new GameAction("play_discard", roomPresenter.username, roomPresenter.roomID);
				roomPresenter.rpcService.updateRealTime(roomPresenter.roomID, createUpdateDTO(discardAction), new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						// deal with base card
						if (isAbleToDiscard){
							hasPlayedNumber ++;
							if (hasPlayedNumber == 3){
								hasPlayedNumber = 0;
								lastRoundPlayCards.clear();
							}
						}
						roomPresenter.display.setPlayButtonVisible(false);
						roomPresenter.display.setCancelButton(false);
						lastRoundPlayCards.clear();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}

	protected boolean checkCardsPlayable() {
		//there is no basetype yet
		return roomPresenter.checkTypeLegal.check((String[])baseCards.toArray(), (String[])userCards.toArray(), (String[])selectedCards.toArray());
	}

	@Override
	public void dealWithAction(GameAction action) {
		content = action.content.split(" ");
		switch (ActionType.getActionType(action.type)) {
		case play_turn:
			if(content[0].equals(roomPresenter.username)){
				isSelectingCard = true;
				roomPresenter.display.setCancelButton(true);
			}
			else{
				isSelectingCard = false;
			}
			break;
		case play_discard:
			// show that someone has discarded
			// maybe conceal his Timer on the UI ~~
			lastRoundPlayCards.clear();
			if (!content[0].equals(roomPresenter.username)) {
				if (isAbleToDiscard) {
					hasPlayedNumber++;
					if (hasPlayedNumber == 3){
						hasPlayedNumber = 0;
						baseCards.clear();
					}
				}
			}
			break;
		case play:
			if (!content[0].equals(roomPresenter.username)){
				lastRoundPlayCards.clear();
				String lastRoundCards[] = content[1].split(";");
				for (int i = 0; i < lastRoundCards.length; i ++){
					lastRoundPlayCards.add(lastRoundCards[i]);
				}
				roomPresenter.display.setPublicDisplayCards(lastRoundPlayCards);
				if(isAbleToDiscard){
					hasPlayedNumber = 1;
					baseCards = (ArrayList<String>) lastRoundPlayCards.clone();
				}
				else{
					hasPlayedNumber ++;
					if (hasPlayedNumber == 4){
						hasPlayedNumber = 0;
						baseCards.clear();
					}
					if (hasPlayedNumber == 1){
						baseCards = (ArrayList<String>) lastRoundPlayCards.clone();
					}
				}
			}
			break;
		case score:
			roomPresenter.display.setScore(Integer.valueOf(content[0]));
			break;
		default:
			break;
		}
	}
	
	private RealTimeGameUpdateDTO createUpdateDTO(GameAction gameAction){
		ArrayList<GameAction> gameActionList = new ArrayList<GameAction>();
		gameActionList.add(gameAction);
		RealTimeGameUpdateDTO updateDTO = new RealTimeGameUpdateDTO(gameActionList);
		return updateDTO;
	}

}
