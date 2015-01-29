package com.lux.trump.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class CardPriority extends XmlRule{
	public CardPriority(String filename, String color, String number){
		super(filename, color, number);
	}
	
	public CardPriority(XmlRule xmlRule){
		super(xmlRule);
	}
	/**
	 * use priority to represent each card selected by the user
	 * @param cardsToBePlayed
	 * @return the priority representation
	 */
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
