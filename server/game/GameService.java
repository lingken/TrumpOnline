package com.lux.trump.server.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lux.trump.server.game.GameAttribute.Role;
import com.lux.trump.shared.GameAction;
import com.lux.trump.shared.RealTimeGameUpdateDTO;
import com.lux.trump.shared.StatusMachine;

public class GameService {
	
	private String gameName;
	private String[] usernames;
	private String[] userRole;
	private ArrayList<String>[] userDeck;
	private int userNumber;
	private Element rootElement;
//	private Boolean inherit = false;
	private Boolean diagonal = false;
	
	private StatusMachine statusMachine = new StatusMachine();
	public GameAttribute attributes = new GameAttribute();
	public ArrayList<UserInfo> users = new ArrayList<UserInfo>();
	private GameManager manager;


	private int playerNumber;
	private int gameWinnerNumber;
	
	
	public void parseXML() {
		String xmlPath = gameName+".xml";
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
		Document document = null;
		try
		{
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new File(xmlPath)); 
		}
		catch (ParserConfigurationException e) 
		{ 
	         e.printStackTrace();  
	    } 
		catch (SAXException e) 
		{ 
	         e.printStackTrace();   
		} 
		catch (IOException e) 
		{ 
	         e.printStackTrace(); 
	    } 
		rootElement = document.getDocumentElement();
	}
	
	public GameService(String gameName, String[] usernames) {
		attributes.firstRound = true;
		attributes.gameName = gameName;
		attributes.userNumber = usernames.length;
		
		for (String username: usernames) {
			UserInfo user = new UserInfo(username);
			users.add(user);
		}
		
		parseXML();

		setRole();
		setDeck();
		setMainDeck();
		setXCard();
		setScore();
		
		try {
			createStatusMachine();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setScore() {
		Element header = (Element) this.rootElement.getElementsByTagName("header").item(0);
		
		if (header.getElementsByTagName("score").getLength() == 0) return;
		Element score = (Element) header.getElementsByTagName("score").item(0);
		NodeList typeList = score.getElementsByTagName("type");
		
		for (int i=0; i<typeList.getLength(); i++) {
			Element type = (Element) typeList.item(i);
			int s = new Integer(type.getAttribute("score"));
			this.attributes.scores.add(new ScoreItem(type.getAttribute("term"), s));
		}
	}
	
	public void nextStatus() {
		this.addGlobalAction("new_procedure", "");
		
		if (! this.statusMachine.hasNext()) restart();
		
		Element status = this.statusMachine.nextStatus();
		
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
			
		manager.start();
	}
	
	public void go() {
		nextStatus();
	}

	public void setXCard() {
		NodeList nodeList = rootElement.getElementsByTagName("Xcard");
		if (nodeList.getLength() == 0) return;
		NodeList typeList = ((Element)nodeList.item(0)).getElementsByTagName("type");
		for (int i=0; i<nodeList.getLength(); i++) {
			attributes.xcardInfo.appendLevel(((Element)typeList.item(i)).getAttribute("term"));
		}
		
		for (UserInfo user: users) {
			user.xCard = attributes.xcardInfo.currentXCard;
		}
		
		addGlobalAction("setXCard", attributes.xcardInfo.currentXCard);
	}
	
	public void setDeck()
	{
		Element header = (Element) rootElement.getElementsByTagName("header").item(0);
		Element deckSet = (Element) rootElement.getElementsByTagName("deck_set").item(0);
		NodeList deckList = deckSet.getElementsByTagName("deck");
		for (int i=0; i<deckList.getLength(); i++) {
			Element deck = (Element) deckList.item(i);
			attributes.decks.add(new Deck(deck.getAttribute("name")));
		}
		
		attributes.decks.add(new Deck("main"));
	}
	
	public void setRole()
	{
		Element header = (Element) rootElement.getElementsByTagName("header").item(0);
		Element roleSet = (Element) header.getElementsByTagName("role_set").item(0);
		
		this.attributes.roleCrossRounds = roleSet.getAttribute("cross_rounds").equals("true");
		this.attributes.diagonal = roleSet.getAttribute("diagonal").equals("true");
		
		for (UserInfo user : users) {
			user.role = roleSet.getAttribute("default");
		}
		this.attributes.defaultRole = roleSet.getAttribute("default");
		NodeList typeList = roleSet.getElementsByTagName("role");
		for (int i=0; i<typeList.getLength(); i++) {
			Element role = (Element) typeList.item(i);
			attributes.appendRole(role.getAttribute("name"), Integer.parseInt(role.getAttribute("num")));
		}
	}
	
	public void createStatusMachine() throws InterruptedException
	{

	/*	for (int i = 0;i < deckName.length;i ++)
			System.out.println(deckName[i]);*/
		
		NodeList nodeList = rootElement.getElementsByTagName("procedure");	
		for (int i = 0;i < nodeList.getLength();i ++)
		{
			Element procedure = (Element)nodeList.item(i); 
           if (procedure.getElementsByTagName("deal").getLength() > 0) {
        	   statusMachine.appendStatus((Element) procedure.getElementsByTagName("deal").item(0));
           }
           else if (procedure.getElementsByTagName("audit").getLength() > 0) {
        	   statusMachine.appendStatus((Element) procedure.getElementsByTagName("audit").item(0));
           }
           else if (procedure.getElementsByTagName("ending").getLength() > 0) {
        	   statusMachine.appendStatus((Element) procedure.getElementsByTagName("ending").item(0));
           }
           else if (procedure.getElementsByTagName("discard").getLength() > 0) {
        	   statusMachine.appendStatus((Element) procedure.getElementsByTagName("discard").item(0));
           }
           else if (procedure.getElementsByTagName("play").getLength() > 0) {
        	   statusMachine.appendStatus((Element) procedure.getElementsByTagName("play").item(0));
           }
           else if (procedure.getElementsByTagName("wager").getLength() > 0) {
        	   statusMachine.appendStatus((Element) procedure.getElementsByTagName("play").item(0));
           }
		}
	}
	
	public void setMainDeck()
	{
		NodeList nodeList = rootElement.getElementsByTagName("header");
		 
		Element element = (Element)nodeList.item(0); 
		int pack_num = Integer.parseInt(element.getAttribute("pack_num"));
		for (int i = 0;i < pack_num;i ++)
		{
			for (int j = 2;j < 11;j ++)
				deckAdd(new Integer(j).toString());
			deckAdd("A");
			deckAdd("K");
			deckAdd("Q");
			deckAdd("J");
			if (element.getAttribute("joker").equals("true")) 
			{
				attributes.findDeck("main").add("B");
				attributes.findDeck("main").add("L");
			}
		}
		attributes.findDeck("main").shuffle();
	}
	
	public void deckAdd(String number)
	{
		attributes.findDeck("main").add("S"+number);
		attributes.findDeck("main").add("H"+number);
		attributes.findDeck("main").add("D"+number);
		attributes.findDeck("main").add("C"+number);
	}
	
	
	// TODO to be implemented
	public void restart() {
		
	}
	
	public void wager(Element element)
	{
		//System.out.println("a");
		String type = element.getAttribute("type");
		if (type.equals("wager"))
		{
		//	getRole_wager(element)
		//	System.out.println("a");
			for (int i = 0;i < userNumber;i ++)
				if (userDeck[i].size() == 0) gameWinnerNumber = i;
		//	System.out.println(diagonal);
			if (!diagonal)
			{
		//		System.out.println("a");
				for (int i = 0;i < userNumber;i ++)
				{
					if (userRole[i].equals(userRole[gameWinnerNumber]))
					{
						addGlobalAction("win", usernames[i]);
					}
					else 
					{
						addGlobalAction("lose", usernames[i]);
					}
				}
			}
		}
	}
	
	public void addGlobalAction(String type,String content)
	{
	//	System.out.println(type+" "+content+" "+userNumber);
		GameAction gameAction = new GameAction(type, content, userNumber);
		addGlobalAction(gameAction);
	}
	
	public void addGlobalAction(GameAction action) {
		for (UserInfo user: users) {
			user.actionQueue.appendAction(action);
		}
	}
	
	public RealTimeGameUpdateDTO realTime(String username) 
	{
		for (UserInfo user: users) {
			if (user.username.equals(username)) {
				ArrayList<GameAction> list = user.actionQueue.fetchActions();
				return new RealTimeGameUpdateDTO(list);
			}
		}
		return null;
	}
	
	public void update(RealTimeGameUpdateDTO update) 
	{
		for (GameAction action: update.gameActions) {
			manager.dealWithAction(action);
		}
	}
	
}