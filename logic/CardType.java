package com.lux.trump.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class CardType {
	private static final int MAX = 10000;
	public ArrayList<TypeItem> itemlist = new ArrayList<TypeItem>();
	String name;
	boolean color_consistency;
	boolean isSpecial;
	boolean isSuper;
	int[]  special_card;
	public int priority;
	
	public static CardType getType(int[] card, CardType[]cardtype){
		for (int i=0; i<cardtype.length; i++){
			if (cardtype[i].isMatch(card))
				return cardtype[i];
		}
		return null;
	}
	
	public CardType(String expression, boolean color_consistency, String name, boolean isSuper, int priority){
		this.priority = priority;
		this.name = name;
		this.isSuper = isSuper;
		this.color_consistency = color_consistency;
		char exp[] = expression.toCharArray();
		int i=0;
		
		if (exp[0] != '{'){
			this.isSpecial = true;
			special_card = new int[exp.length];
			for (int j=0; j<exp.length; j++){
				switch (exp[j]){
					case 'L':
						special_card[j] = MAX -1;
						break;
					case 'B':
						special_card[j] = MAX;
						break;
					default:
						special_card[j] = 0;
						break;
				}
			}
			Arrays.sort(special_card);
			return;
		}
		this.isSpecial = false;
		// get the repeat times of each item
		String num = "";
		while (exp[++i] != '}'){
			num += exp[i];
		}
		i++;
		int n = Integer.valueOf(num).intValue();

		// get each item 
		while (i<exp.length){
			i++;   // skip the '('
			String item = "";
			while (exp[i] != ')'){
				item += exp[i++];
			}
			i++;
			itemlist.add(new TypeItem(item, exp[i++], n));
		}
	}
	
	boolean isMatch(int[] card){
		//System.out.println(this.isSpecial);
		if (this.isSpecial == true){
			return isSpecialMatch(card);
		}	
		return isPartMatch(card, this.itemlist, -1);
	}
	
	boolean isSpecialMatch(int [] card){
		if (card.length != special_card.length)
			return false;
		Arrays.sort(card);
		for (int i=0; i<card.length; i++)
			if (card[i] != special_card[i])
				return false;
		return true;
	}
	boolean isPartMatch(int[] card, ArrayList<TypeItem> list, int n){
		if (list.size() == 0 && card.length == 0)
			return true;
		if (list.size() == 0 && card.length != 0)
			return false;
		if (list.size() != 0 && card.length == 0)
			return false;
		
		int num = n;
		for (int i= list.get(0).item.length(); i<card.length+1; i += list.get(0).item.length()){
			int[] subCard = new int[i];
			for (int j=0; j<subCard.length; j++){
				subCard[j] = card[j];
		//		System.out.print(card[j]);
			}
		//	System.out.println();
			
			if (list.get(0).isMatch(subCard, n) != 0){
				//System.out.print(card.length + " " + i+ ", ");
				num = list.get(0).isMatch(subCard, n);
				int[] lastcard = new int[card.length - i];
				for (int j=0; j<lastcard.length; j++){
					lastcard[j] = card[i+j];
				}
				@SuppressWarnings("unchecked")
				ArrayList<TypeItem> newlist = (ArrayList<TypeItem>) list.clone();
				newlist.remove(0);
				if (isPartMatch(lastcard, newlist, num))
					return true;
			}
		}
		return false;
	}
}