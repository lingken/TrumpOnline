package com.lux.trump.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class XmlRule {	
	public void initlizeRuleInfo(RuleInfoDTO ruleInfoDTO){
		ruleInfoDTO.color_consistency = this.color_consistency;
		ruleInfoDTO.num_consistency = this.num_consistency;
		ruleInfoDTO.color_sensitive = this.color_sensitive;
		ruleInfoDTO.type_consistency = this.type_consistency;
		ruleInfoDTO.order = this.order;
		ruleInfoDTO.xcard = this.xcard;
		
		ruleInfoDTO.typeNum = this.typeNum;
		ruleInfoDTO.cardtype = this.cardtype;
		ruleInfoDTO.typeList = this.typeList;
		ruleInfoDTO.strong_type_consistency = this.strongTypeConsistency;
		
		ruleInfoDTO.primaryColor = this.primaryColor;
		ruleInfoDTO.primaryNumber = this.primaryNumber;
		
		ruleInfoDTO.cardPriority = this.cardPriority;		
	}
	
	public XmlRule(String filename, String color, String number){
		this.primaryColor = color;
		this.primaryNumber = number;
		
		try {
			//read in XML file
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			Element typeSetNode = (Element) doc.getElementsByTagName("type_set").item(0);
			typeTagList = typeSetNode.getElementsByTagName("type");
			Element orderSetNode = (Element) doc.getElementsByTagName("order").item(0);
			orderTagList = orderSetNode.getElementsByTagName("type");

			//some information
			this.color_sensitive = typeSetNode.getAttribute("color_sensitive").equals("true");
			this.num_consistency = typeSetNode.getAttribute("num_consistency").equals("true");
			this.color_consistency = typeSetNode.getAttribute("color_consistency").equals("true");
			this.type_consistency = typeSetNode.getAttribute("type_consistency").equals("true");
			this.order = typeSetNode.getAttribute("order").equals("true");
			this.xcard = orderSetNode.getAttribute("Xcard").equals("true");

			//setCardPriority();
			setCardPriority();
			
			//setCardType
			this.createCardTypeChecker();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public XmlRule (XmlRule xmlRule){
		this.color_consistency = xmlRule.color_consistency;
		this.num_consistency = xmlRule.num_consistency;
		this.color_sensitive = xmlRule.color_sensitive;
		this.type_consistency = xmlRule.type_consistency;
		this.order = xmlRule.order;
		this.xcard = xmlRule.xcard;
		
		this.typeNum = xmlRule.typeNum;
		this.typeTagList = xmlRule.typeTagList;
		this.cardtype = xmlRule.cardtype;
		this.typeList = xmlRule.typeList;
		this.strongTypeConsistency = xmlRule.strongTypeConsistency;
		
		this.primaryColor = xmlRule.primaryColor;
		this.primaryNumber = xmlRule.primaryNumber;
		
		this.cardPriority = xmlRule.cardPriority;
		this.orderTagList = xmlRule.orderTagList;
		this.orderNum = xmlRule.orderNum;
	}
	
	public boolean color_sensitive;
	public boolean num_consistency;
	public boolean color_consistency;
	public boolean type_consistency;
	public boolean order;
	public boolean xcard;
	
	public int typeNum = 0;               // the number of types in type_set in the XML
	public NodeList typeTagList = null;
	public CardType[] cardtype = null; // checkers to get the type of several cards (e.g aa)
	public int typePriority[] = null;     // the priority of each type
	public String typeList[] = null;
	public boolean strongTypeConsistency; // see the comment in the XML
	
	public String primaryColor;
	public String primaryNumber;
	public int MAX = 10000;
	private void createCardTypeChecker(){
		typeNum = typeTagList.getLength();
		cardtype = new CardType[typeNum];
		typeList = new String[typeNum];
		typePriority = new int[typeNum];
		int priority = MAX; // use int_max
		for (int i = 0; i < typeTagList.getLength(); i ++){
			Element element = (Element)(typeTagList.item(i));
			int p;
			if (element.getAttribute("priority").equals(""))
				p = 0;
			else
				p = Integer.parseInt(element.getAttribute("priority"));
			cardtype[i] = new CardType(element.getAttribute("term"), true, element.getAttribute("name"), 
					element.getAttribute("super").equals("true"), p); 
			typeList[i] = element.getAttribute("name");
			// deal with type priority
			typePriority[i] = priority;
			if (strongTypeConsistency){
				if (element.getAttribute("super").equals("true")) { // not null but empty
					priority --;
				}
			}
			else{
				priority --;
			}
		}
	}
	
	/**
	 * get the priority of the type of cards
	 * @param typeName (e.g "double_flush", "single")
	 * @return the priority of the type of cardss
	 */
	public int getTypePriority(String typeName) {
		for (int i = 0; i < typeNum; i ++){
			if (typeList[i].equals(typeName)) {
				return typePriority[i];
			}
		}
		return -1;
	}
	
	public int cardPriority[] =  new int[0xFF]; // record the priority of each card
	                                             // the index of cardPriority is the result of encodeCard()
	public NodeList orderTagList = null;
	public int orderNum = 0;
	/**
	 * Set the priority of each card.
	 * Record the priority in cardPriority[]
	 * The priority reveals the order of card and the continuity.
	 */
	private void setCardPriority() {
		orderNum = orderTagList.getLength();
		int priority = MAX;
		for (int i = 0; i < orderNum; i ++){
			Element element = (Element)(orderTagList.item(i));
			String cardContent = element.getAttribute("term");
			
			char cardContentTag = cardContent.charAt(0);
			if (cardContent.equals("B") || cardContent.equals("L")){// for B and L
				cardPriority[encodeCard(cardContent)] = priority;
				priority --;
				continue;
			}
			String number = cardContent.substring(1);
			if (number.equals("X")){
				number = primaryNumber;
			}
			String colorList[] = {"S", "H", "D", "C"};
			switch(cardContentTag){
			case '/':
				priority = priority - 10;
				break;
			case 'P':
				if (cardPriority[encodeCard(primaryColor+number)] != 0){
					priority ++;
					break;
				}
				cardPriority[encodeCard(primaryColor + number)] = priority;
				break;
			case 'N':
				for (int j = 0; j < colorList.length; j ++){
					if (!colorList[j].equals(primaryColor)){
						if (cardPriority[encodeCard(colorList[j]+number)] == 0){
							if (number == primaryNumber){
								cardPriority[encodeCard(colorList[j]+number)] = priority - 2 * j; //µ±ºìÌÒÎªÖ÷»¨É«£¬2ÎªÖ÷¼¶ÅÆÊ±£¬ºÚÌÒ2Óë·½Æ¬2Óë²Ý»¨2¾ù²»ÏàÍ¬¡¢²»ÏàÁ¬¡£
							}
							else{
								cardPriority[encodeCard(colorList[j]+number)] = priority - 20 * j;
							}
						}
						else{
							priority ++;
							break;
						}
					}
				}
				break;
			case '*':
				for (int j = 0; j < colorList.length; j ++){
					if (cardPriority[encodeCard(colorList[j]+number)] == 0){
						cardPriority[encodeCard(colorList[j] + number)] = priority;
					}
				}
				break;
			}
			priority --;
		}
	}
	
	/**
	 * encode the card by the following rules
	 * Spade: 0x1X Heart: 0x2X Club: 0x3X Diamond: 0x4X Little Joker: 0x50 Big Joker: 0x51
	 * @param card
	 * @return the numeric representation of card
	 */
	public int encodeCard(String card){
		//System.out.println("in encodeCard: " + card);
		if (card.equals("B"))
			return 0x51;
		if (card.equals("L"))
			return 0x50;
		if (card.equals("//"))
			return 0xF0;
		Pattern pattern = Pattern.compile("([SHCD])(.*)");
		Matcher matcher = pattern.matcher(card);
		matcher.find();
		
		char cardColor = matcher.group(1).charAt(0);
		char cardContent = matcher.group(2).charAt(0);
		int result = 0x00;
		switch (cardColor) {
		case 'S': result += 0x10; break;
		case 'H': result += 0x20; break;
		case 'C': result += 0x30; break;
		case 'D': result += 0x40; break;
		default:  break;
		}
		if (cardContent >= 'A'){ //0x11 stands for S10, 1:10, 2 - 9, A:A, B: J, C: Q, D: K
			switch(cardContent){
			case 'J': result += 0x0B; break;
			case 'Q': result += 0x0C; break;
			case 'K': result += 0x0D; break;
			case 'A': result += 0x0E; break;
			default: break;
			}
		}
		else if (card.length() == 3){
			result += 10;
		}
		else{
			result += cardContent - '0';
		}
		return result;		
	}
}