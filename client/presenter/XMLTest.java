package com.lux.trump.client.presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java_cup.internal_error;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.regexp.recompile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.gwt.dev.asm.tree.IntInsnNode;


public class XMLTest {
	public void test() {
		try {
			//read in XML file
			File fXmlFile = new File("E:/NewWorkPlace/FileReadInTest/shengji_lk.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			Element typeSetNode = (Element) doc.getElementsByTagName("type_set").item(0);
			typeTagList = typeSetNode.getElementsByTagName("type");
			Element orderSetNode = (Element) doc.getElementsByTagName("order").item(0);
			orderTagList = orderSetNode.getElementsByTagName("type");
			
			//createCardTypeChecker();
			//setCardPriority();
			String [] testStrings = {"D10", "D9", "DJ", "DQ", "DK", "DA"};
			setCardPriority();
			System.out.println("finish set card priority");
			int [] result = getPriorityPresentationOfCard(testStrings);
			for (int i = 0; i < result.length; i ++){
				System.out.println(result[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private int typeNum = 0;               // the number of types in type_set in the XML
	private NodeList typeTagList = null;
	private CardType[] checkerList = null; // checkers to get the type of several cards (e.g aa)
	private int typePriority[] = null;     // the priority of each type
	private String typeList[] = null;
	private boolean strongTypeConsistency; // see the comment in the XML
	
	private String primaryColor = "H";
	private String primaryNumber = "Q";
	
	/**
	 * Create the checkers to check the type of different combinations of cards, and set the priority of each type at the same time.
	 */
	private void createCardTypeChecker(){
		typeNum = typeTagList.getLength();
		checkerList = new CardType[typeNum];
		typeList = new String[typeNum];
		typePriority = new int[typeNum];
		int priority = Integer.MAX_VALUE; // use int_max
		for (int i = 0; i < typeTagList.getLength(); i ++){
			Element element = (Element)(typeTagList.item(i));
			//System.out.println(element.getAttribute("term"));
			//System.out.println(element.getAttribute("name"));
			checkerList[i] = new CardType(element.getAttribute("term"), true, element.getAttribute("name")); 
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
	private int getTypePriority(String typeName) {
		for (int i = 0; i < typeNum; i ++){
			if (typeList[i].equals(typeName)) {
				return typePriority[i];
			}
		}
		return -1;
	}
	
	private int cardPriority[] =  new int[0xFF]; // record the priority of each card
	                                             // the index of cardPriority is the result of encodeCard()
	private NodeList orderTagList = null;
	private int orderNum = 0;
	/**
	 * Set the priority of each card.
	 * Record the priority in cardPriority[]
	 * The priority reveals the order of card and the continuity.
	 */
	private void setCardPriority() {
		orderNum = orderTagList.getLength();
		int priority = Integer.MAX_VALUE;
		for (int i = 0; i < orderNum; i ++){
			Element element = (Element)(orderTagList.item(i));
			String cardContent = element.getAttribute("term");
			
			//System.out.println(cardContent);
			
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
								cardPriority[encodeCard(colorList[j]+number)] = priority - 2 * j; //������Ϊ����ɫ��2Ϊ������ʱ������2�뷽Ƭ2��ݻ�2����ͬ����������
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
	private int encodeCard(String card){
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
			case 'A': result += 0x0A; break;
			case 'J': result += 0x0B; break;
			case 'Q': result += 0x0C; break;
			case 'K': result += 0x0D; break;
			default: break;
			}
		}
		else{
			result += cardContent - '0';
		}
		return result;		
	}
	/**
	 * check the cards selected by user
	 * @param cardsToBePlayed
	 * @return whether the cards are playable
	 */
//	private boolean checkCardPlayable(String[] cardsToBePlayed){
//		if (getTypePriority(getType(cardsToBePlayed, checkerList)) < getTypePriority(getType(lastRoundCard, checkerList))){
//			return false;
//		}
//		if (cardPriority(encodeCard(getMinCard(cardsToBePlayed))) > cardPriority(encodeCard(getMinCard(lastRoundCard)))){
//			return true;
//		}
//		else{
//			return false;
//		}
//	}
	
	/**
	 * use priority to represent each card selected by the user
	 * @param cardsToBePlayed
	 * @return the priority representation
	 */
	private int[] getPriorityPresentationOfCard(String[] cardsToBePlayed){
		int [] priorityPresentationOfCard = new int[cardsToBePlayed.length];
		for (int i = 0; i < cardsToBePlayed.length; i ++){
			priorityPresentationOfCard[i] = cardPriority[encodeCard(cardsToBePlayed[i])];
		}
		Arrays.sort(priorityPresentationOfCard);
		ArrayList<Integer> cardContent = new ArrayList<Integer>();
		ArrayList<Integer> cardNumber = new ArrayList<Integer>();
		int prev = Integer.MIN_VALUE;
		for (int i = 0; i < priorityPresentationOfCard.length; i ++){
			if (priorityPresentationOfCard[i] != prev){
				cardContent.add(priorityPresentationOfCard[i]);
				cardNumber.add(1);
				prev = priorityPresentationOfCard[i];
			}
			else{
				cardNumber.set(cardNumber.size()-1, cardNumber.get(cardNumber.size()-1)+1);
			}
		}
		for (int i = 0; i < cardContent.size() - 1; i ++){
			for (int j = 0; j < cardContent.size() - i - 2; j ++){
				if (cardNumber.get(j) < cardNumber.get(j+1)){
					Integer tmp = cardNumber.get(j+1);
					cardNumber.set(j+1, cardNumber.get(j));
					cardNumber.set(j, tmp);
				}
			}
		}
		int k = 0;
		for (int i = 0; i < cardContent.size(); i ++){
			for (int j = 0; j < cardNumber.get(i); j ++){
				priorityPresentationOfCard[k ++] = cardContent.get(i);
			}
		}
		return priorityPresentationOfCard;
	}
	/**
	 * use checkers to get the type of this combination of cards
	 * @param contentOfCard
	 * @param cardtype
	 * @return the type of this combination of cards
	 */
	public static String getType(String[] contentOfCard, CardType[] cardtype){
		for (int i=0; i<cardtype.length; i++){
	    	if (cardtype[i].isMatch(contentOfCard)){
	    	return cardtype[i].name;
	    	}
		}
		return "no such type";
	}
}
