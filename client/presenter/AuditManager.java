package com.lux.trump.client.presenter;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.lux.trump.client.presenter.DealManager.ActionType;
import com.lux.trump.shared.GameAction;
import com.lux.trump.shared.RealTimeGameUpdateDTO;

public class AuditManager extends GameManager {
	private RoomPresenter roomPresenter = null;
	private Element procedureElement = null;
	private String content[];
	private String lastAduditUser = "";
	private String lastAuditKey = "";
	
	public class AuditOption implements Serializable{
		public String keyString = "";
		public String displayString = "";
		AuditOption(){
			keyString = "";
			displayString = "";
		}
		AuditOption(String key, String display){
			keyString = key;
			displayString = display;
		}
	}
	
	ArrayList<AuditOption> auditOptionList = new ArrayList<AuditOption>();
	
	private enum ActionType{
		audit_turn, audit, set_wager, set_role;
		private static ActionType getActionType(String type){
			return valueOf(type);
		}
	}
	
	public AuditManager(RoomPresenter presenter, Element element) {
		super(presenter, element);
		roomPresenter = presenter;
		procedureElement = element;
		Node auditNode = element.getElementsByTagName("audit").item(0);
		NodeList optionList = ((Element)auditNode).getElementsByTagName("option");
		for (int i = 0; i < optionList.getLength(); i ++){
			Element option = (Element)optionList.item(i);
			if (!option.getAttribute("key").isEmpty()){
				auditOptionList.add(new AuditOption(option.getAttribute("key"), option.getAttribute("display")));
			}
		}
		roomPresenter.display.createAuditItems(auditOptionList);
		for (int i = 0; i < auditOptionList.size(); i ++){
			roomPresenter.display.getSelectedAuditButton(i).addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					int selectOptionNumber = roomPresenter.display.getSelectedAuditOption();
					String actionContent = roomPresenter.username + " " + auditOptionList.get(selectOptionNumber).keyString;
					GameAction auditGameAction = new GameAction("audit", actionContent, 0);
					realTimeAuditUpdate(auditGameAction);
					
				}
			});
		}
//		roomPresenter.display.getAuditWagerPanel().addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				
//				
//			}
//		});
	}

	@Override
	public void dealWithAction(GameAction action) {
		content = action.content.split(" ");
		switch (ActionType.valueOf(action.type)) {
		case audit_turn:
			if (content[0].equals(roomPresenter.username)){
				roomPresenter.display.setAuditWagerPanelVisible(true);
			}
			break;
		case audit:
			lastAduditUser = content[0];
			lastAuditKey = content[1];
			checkAudit();
			break;
		case set_wager:
			// what dose that mean??
			roomPresenter.display.setWager(content[0]);
			break;
		case set_role:
			if (content[0].equals(roomPresenter.username)){
				roomPresenter.userrole = content[1];
				roomPresenter.display.setUserRole(roomPresenter.getIndexByUsername(roomPresenter.username), content[1]);
			}
			break;
		default:
			break;
		}
	}

	private void checkAudit() {
		boolean biggerThanLastAudit = true;
		for (int i = 0; i < auditOptionList.size(); i ++){
			if (auditOptionList.get(i).keyString.equals(lastAuditKey)){
				biggerThanLastAudit = false;
			}
			roomPresenter.display.setAuditItems(i, biggerThanLastAudit);
		}
		
	}
	
	private void realTimeAuditUpdate(GameAction gameAction){
		ArrayList<GameAction> gameActionList = new ArrayList<GameAction>();
		gameActionList.add(gameAction);
		RealTimeGameUpdateDTO updateDTO = new RealTimeGameUpdateDTO(gameActionList);
		roomPresenter.rpcService.updateRealTime(roomPresenter.roomID, updateDTO, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
