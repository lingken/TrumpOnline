package com.lux.trump.client.presenter;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.lux.trump.client.TrumpServiceAsync;
import com.lux.trump.client.presenter.AuditManager.AuditOption;
import com.lux.trump.logic.CardComparator;
import com.lux.trump.logic.CheckTypeLegal;
import com.lux.trump.shared.ChatDTO;
import com.lux.trump.shared.GameAction;
import com.lux.trump.shared.Message;
import com.lux.trump.shared.RealTimeGameUpdateDTO;
import com.lux.trump.shared.StatusMachine;
import com.lux.trump.shared.UserGameInfoDTO;

public class RoomPresenter implements Presenter {
	
	final Display display;
	final TrumpServiceAsync rpcService;
	private final HandlerManager eventBus;
	
	int roomID;
	private String xmlRule;
	private Element rootElement;
	private ArrayList<UserGameInfoDTO> users;
	private ArrayList<Integer> userIndexMapArray; // from order in UserGameInfoDTO to display order
	private final int UPDATE_FREQUENCY = 200;
	private final int CHAT_FREQUENCY = 500;
	
	String username = "";
	String userrole = "";
	private String gametype = "";
	private boolean isAbleToAudit;
	ArrayList<String> userCardsRecord[] = null;
	
	private GameManager manager;
	private StatusMachine statusMachine;
	public String primaryColor = "";
	public String primaryNumber = "";
	
	public enum Status {
		INIT, READY, PLAYING;
	}
	
	private enum ActionType {
		deal, audit, set_role, player_set, lose, win, restart;
		private static ActionType getActionType(String type){ 
			return valueOf(type);
		}
	}
	
	private Status status;
	public CheckTypeLegal checkTypeLegal;
	public CardComparator cardComparator;
	
	public RoomPresenter(TrumpServiceAsync rpcService, HandlerManager eventBus, Display view, int roomID, String username) {
		this.display = view;
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.roomID = roomID;
		this.username = username;
		this.status = Status.INIT;
	}
	
	public interface Display {
		Widget asWidget();
		void setRoomNumber(int roomID);
		void setUsers(ArrayList<UserGameInfoDTO> users);
		HasClickHandlers getReadyButton();
		void appendCharBoard(String content);
		void setStatus(Status status);
		
		HasClickHandlers getCardPanel();
		ArrayList<String> getSelectedCard();
		HasClickHandlers getPlayButton();
		void setPlayButtonVisible(boolean isVisible);
		HasClickHandlers getAuditPanel();
		
		//audit
		void setAuditDiamondButtonVisible(boolean isVisible);
		void setAuditSpadeButtonVisible(boolean isVisible);
		void setAuditHeartButtonVisible(boolean isVisible);
		void setAuditClubButtonVisible(boolean isVisible);
		void setAuditNoneButtonVisible(boolean isVisible);
		void setAuditWagerPanelVisible(boolean isVisible);
		HasClickHandlers getAuditDiamondButton();
		HasClickHandlers getAuditSpadeButton();
		HasClickHandlers getAuditHeartButton();
		HasClickHandlers getAuditClubButton();
		HasClickHandlers getAuditNoneButton();
		HasClickHandlers getAuditWagerPanel();
		
		void setUserCards(ArrayList<String>[] userCards);
		void setPublicDisplayCards(ArrayList<String> publicDisplayCards);
		void setUserRole(int index, String rolename);
		void setWin(boolean isWin);
		void createAuditItems(ArrayList<AuditOption> auditOptionList);
		void setAuditItems(int i, boolean isVisible);
		HasClickHandlers getSelectedAuditButton(int buttonNumber);
		int getSelectedAuditOption();
		void setWager(String string);
		HasClickHandlers getDiscardButton();
		void setDiscardButton(boolean isVisible);
		void setUserXcard(int userIndex, String userXcard);
		void setWagerDisplay(int originalSequenceOfUserInDTO, String string); // there may be problem of the sequence
		void setScore(Integer score);
		HasClickHandlers getCancelButton();
		void setCancelButton(boolean isVisible);
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		display.setRoomNumber(roomID);
		fetchInitialRoomInfo();
		createStatusMachine();
		
		Timer updateTimer = new Timer() {

			@Override
			public void run() {
				updateStatus();
			}
			
		};
		
		updateTimer.scheduleRepeating(UPDATE_FREQUENCY);
		
		Timer chatTimer = new Timer() {

			@Override
			public void run() {
				updateChatRoom();
			}
			
		};
		
		chatTimer.scheduleRepeating(CHAT_FREQUENCY);
	}
	
	private void nextStatus() {
		Element status = statusMachine.nextStatus();
		
		if ("deal".equals(status.getTagName())) {
			manager = new DealManager(this, status);
		}
		else if ("audit".equals(status.getTagName())) {
			manager = new AuditManager(this, status);
		}
		else if ("ending".equals(status.getTagName())) {
			manager = new EndingManager(this, status);
		}
		else if ("discard".equals(status.getTagName())) {
			manager = new DiscardManager(this, status);
		}
		else if ("play".equals(status.getTagName())) {
			manager = new PlayManager(this, status);
		}
		else if ("wager".equals(status.getTagName())) {
			manager = new WagerManager(this, status);
		}
	}
	
	private void createStatusMachine() {
		statusMachine = new StatusMachine();
		Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
		NodeList procedureList = bodyElement.getElementsByTagName("procedure");
		
		for (int i=0; i<procedureList.getLength(); i++) {
			Element procedureElement = (Element) procedureList.item(i);
			
			if (procedureElement.getElementsByTagName("deal").getLength() > 0) {
				statusMachine.appendStatus(procedureElement);
			}
			else if (procedureElement.getElementsByTagName("audit").getLength() > 0) {
				statusMachine.appendStatus(procedureElement);
			}
			else if (procedureElement.getElementsByTagName("ending").getLength() > 0) {
				statusMachine.appendStatus(procedureElement);
			}
			else if (procedureElement.getElementsByTagName("play").getLength() > 0) {
				statusMachine.appendStatus(procedureElement);
			}
			else if (procedureElement.getElementsByTagName("discard").getLength() > 0) {
				statusMachine.appendStatus(procedureElement);
			}
			else if (procedureElement.getElementsByTagName("wager").getLength() > 0) {
				statusMachine.appendStatus(procedureElement);
			}
		}
	}
	
	private void bind() {
		if (display.getReadyButton() == null) {
			return;
		}
		display.getReadyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				status = Status.READY;
				//display.setStatus(Status.getStatusString(status));
				display.setStatus(status);
			}
			
		});
		
		if (display.getCardPanel() == null){
			return;
		}
		display.getCardPanel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				ArrayList<String> selectedCard = display.getSelectedCard();
				display.setPlayButtonVisible((checkCardPlayable(selectedCard)));
			}
		});
		
		if (display.getPlayButton() == null){
			return;
		}
		display.getPlayButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				//create my DTO and transmit it to the server
			}
		});
	}

	protected boolean checkCardPlayable(ArrayList<String> selectedCard) {
		// TODO Auto-generated method stub
		return false;
	}

	private void fetchInitialRoomInfo() {
		rpcService.xmlRule(roomID, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				xmlRule = result;
				parseRule(xmlRule);
			}
			
		});
		
		fetchUsers();

	}
	
	private void fetchGameActions(){
		rpcService.realtime(roomID, username, new AsyncCallback<RealTimeGameUpdateDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(RealTimeGameUpdateDTO gameActionsDTO) {
				for (GameAction action: gameActionsDTO.gameActions) {
					manager.dealWithAction(action);
				}
			}
		});
	}
	
	/*

	+	 type = "set_role" content = "username rolename" 
	+	 type = "deal" content = "username card" 
	+	 type = "audit" content = "username boolean" booleanΪtrueΪ���Խ��ƣ� ��֮�����ԡ�
	+	 type = "player_set" content = "username" 
	+	 type = "lose" content = "username"	
	+	 type = "win" content = "username" 
	+	 type = "restart" content = "" 
	 	 */
	private void act(GameAction gameAction) {
		// TODO Auto-generated method stub
		String contents[] = gameAction.content.split(" ");
		switch(ActionType.getActionType(gameAction.type)){
		case deal:{
			userCardsRecord[getIndexByUsername(contents[0])].add(contents[1]);
			display.setUserCards(userCardsRecord);
			checkAudit();
			break;
		}
		case audit:{
			if(contents[1].equals(username)){
				if (contents[2].equals("true")){
					isAbleToAudit = true;
				} else if(contents[2].equals("false")){
					isAbleToAudit = false;
				}
			}
			break;
		}
		case set_role:{
			display.setUserRole(getIndexByUsername(contents[0]), contents[1]);
			if (contents[0].equals(username)){
				userrole = contents[1];
			}
			break;
		}
		case player_set:{
			if (contents[1].equals(username)){
				display.setPlayButtonVisible(true);
			}
			else{
				display.setPlayButtonVisible(false);
			}
			break;
		}
		case lose:{
			if (contents[1].equals(username)){
				display.setWin(false);
			}
			break;
		}
		case win:{
			if (contents[1].equals(username)){
				display.setWin(true);
			}
			break;
		}
		case restart:{
			break;
		}
		default:break;
		}
	}
	
	private void checkAudit() {
		// TODO Auto-generated method stub
		
	}

	void restart() {
		userrole = "";
		display.setUserRole(getIndexByUsername(username), userrole);
		primaryColor = "";
		primaryNumber = "";
		for (int i = 0; i < userCardsRecord.length; i ++){
			userCardsRecord[i].clear();
		}
		display.setUserCards(userCardsRecord);
		display.setPublicDisplayCards(new ArrayList<String>());
		status = Status.PLAYING;
		display.setStatus(status);
	}
	
	
	int getIndexByUsername(String Username) {
		// TODO Auto-generated method stub
		for (int i = 0; i < users.size(); i ++){
			if (Username.equals(users.get(i).username)){
				return i;
			}
		}
		return -1;
	}

	private void parseRule(String xmlRule) {
		Document document = XMLParser.parse(xmlRule);
		rootElement = document.getDocumentElement();
	}

	private void fetchUsers() {
		rpcService.usersInRoom(roomID, new AsyncCallback<ArrayList<UserGameInfoDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<UserGameInfoDTO> result) {
				users = result;
				ArrayList<UserGameInfoDTO> sortUserList = new ArrayList<UserGameInfoDTO>();
				int k = 0;
				for (; k < users.size(); k ++){
					if(users.get(k).username.equals(username)){
						break;
					}
				}
				for (int j = k; j <users.size(); j ++){
					sortUserList.add(users.get(j));
					userIndexMapArray.add(j);
				}
				for (int j = 0; j < k; j ++){
					sortUserList.add(users.get(j));
					userIndexMapArray.add(j);
				}
				users = sortUserList;
				username = result.get(0).username;
				userCardsRecord = new ArrayList[result.size()];
				for (int i = 0; i < userCardsRecord.length; i ++){
					userCardsRecord[i] = new ArrayList<String>();
				}
				display.setUsers(result);
			}
			
		});
	}
	
	private void updateStatus() {
		if (status == Status.INIT) {
			fetchUsers();
		}
		else if (status == Status.READY) {
			fetchUsers();
			boolean allReady = true;
			for (UserGameInfoDTO user: users) {
				if (user.ready == false) {
					allReady = false;
					break;
				}
			}
			if (allReady) {
				status = Status.PLAYING;
				display.setStatus(status);
			}
		}
		else if (status == Status.PLAYING) {
			fetchGameActions();
		}
	}
	
	private void updateChatRoom() {
		rpcService.chatsInRoom(roomID, new AsyncCallback<ChatDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ChatDTO result) {
				String content = new String();
				for (Message message: result.messages) {
					content += message.sender + ":" + message.content + "\n";
				}
				if (! "".equals(content)) {
					display.appendCharBoard(content);
				}
			}
			
		});
	}

	public int getIndexByOriginalOrder(int originalIndex) {
		for (int i = 0; i < userIndexMapArray.size(); i ++){
			if (userIndexMapArray.get(i) == originalIndex){
				return i;
			}
		}
		return -1;
	}
}
