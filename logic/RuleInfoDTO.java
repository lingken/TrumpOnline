package com.lux.trump.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RuleInfoDTO {
	public boolean xcard;
	public String primaryNumber;
	public String primaryColor;
	public boolean color_sensitive;
	public boolean num_consistency;
	public boolean color_consistency;
	public boolean type_consistency;
	public boolean order;
	public boolean strong_type_consistency;
	
	public int typeNum;
	public CardType[] cardtype = null;
	public int typePriority[] = null;
	public String typeList[] = null;
	
	public int cardPriority[] =  new int[0xFF];
	
	public RuleInfoDTO(){
		
	}
	
	
	public RuleInfoDTO(RuleInfoDTO ruleInfoDTO){
		this.color_consistency = ruleInfoDTO.color_consistency;
		this.num_consistency = ruleInfoDTO.num_consistency;
		this.color_sensitive = ruleInfoDTO.color_sensitive;
		this.type_consistency = ruleInfoDTO.type_consistency;
		this.order = ruleInfoDTO.order;
		this.xcard = ruleInfoDTO.xcard;
		
		this.typeNum = ruleInfoDTO.typeNum;
		this.cardtype = ruleInfoDTO.cardtype;
		this.typeList = ruleInfoDTO.typeList;
		this.strong_type_consistency = ruleInfoDTO.strong_type_consistency;
		
		this.primaryColor = ruleInfoDTO.primaryColor;
		this.primaryNumber = ruleInfoDTO.primaryNumber;
		
		this.cardPriority = ruleInfoDTO.cardPriority;
	}
	
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
	
	public int[] getcardpriority(String[] cardsToBePlayed){
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
			for (int j = 0; j < cardContent.size() - i - 1; j ++){
				if (cardNumber.get(j) < cardNumber.get(j+1)){
					Integer tmp = cardNumber.get(j+1);
					cardNumber.set(j+1, cardNumber.get(j));
					cardNumber.set(j, tmp);
					tmp = cardContent.get(j+1);
					cardContent.set(j+1, cardContent.get(j));
					cardContent.set(j, tmp);
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
}