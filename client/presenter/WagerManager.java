package com.lux.trump.client.presenter;

import com.google.gwt.xml.client.Element;
import com.lux.trump.shared.GameAction;

public class WagerManager extends GameManager {
	private RoomPresenter roomPresenter = null;
	private Element procedureElement = null;
	private String content[];
	private enum ActionType{
		wager, set_role, setXcard, set_user_xcard;
		private static ActionType getActionType(String type){
			return valueOf(type);
		}
	}
	public WagerManager(RoomPresenter presenter, Element element) {
		super(presenter, element);
		roomPresenter = presenter;
		procedureElement = element;	
	}

	@Override
	public void dealWithAction(GameAction action) {
		content = action.content.split(" ");
		switch (ActionType.getActionType(action.type)){
		case wager:
			// the sequence is key!
			String wager[] = content[0].split(";");
			for (int i = 0; i < wager.length; i ++){
				roomPresenter.display.setWagerDisplay(roomPresenter.getIndexByOriginalOrder(i), wager[i]);
			}
			break;
		case set_role:
			if (content[0].equals(roomPresenter.username)){
				roomPresenter.userrole = content[1];
			}
			roomPresenter.display.setUserRole(roomPresenter.getIndexByUsername(content[0]), content[1]);
			break;
		case setXcard:
			//what if the Xcard is B or L ??
			String xCard = content[0];
			roomPresenter.primaryColor = "" + xCard.charAt(0);
			if (xCard.length() > 1){
				roomPresenter.primaryNumber = xCard.substring(1);
			}
			break;
		case set_user_xcard:
			roomPresenter.display.setUserXcard(roomPresenter.getIndexByUsername(content[0]), content[1]);
			break;
		default:
			break;
		}

	}
}
