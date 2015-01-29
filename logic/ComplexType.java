package com.lux.trump.logic;

import java.awt.List;
import java.util.ArrayList;


public class ComplexType {
	public ArrayList<CardType> cardTypes = new ArrayList<CardType>();
	public ArrayList<ArrayList<int[]>> card = new ArrayList<ArrayList<int[]>>();
	
	public boolean differentcolor;
	public int flush_num;
	public int double_num;
	public int single_num;
	public int flush_length;
	
	public ComplexType(int[] card, CardType[] cardtype){
		for (int i=0; i<cardtype.length; i++){
			this.card.add(new ArrayList<int[]>());
		}
		init(card, cardtype);
		flush_num = 0;
		flush_length = 0;
		double_num = 0;
		single_num = 0;
		
		for (int[] list:this.card.get(0)){
			flush_num++;
			double_num += list.length/2;
			flush_num += list.length;
		}
		double_num += this.card.get(1).size();
		single_num += this.card.get(2).size();
		single_num += double_num*2;
	}
	
	void init(int[] card, CardType[] cardtype){
		if (card.length == 0)
			return;
		int i=0;
		for (; i<card.length-1;i++){
			if (i%2 == 0 && card[i]!=card[i+1]){
				break;
			}
			if (i%2 == 1){
				if (i==card.length-2 || card[i]!=card[i+1]-1 || card[i+1]!=card[i+2]){
					break;
				}
			}
		}
		i++;
		//System.out.println(i);
		int[] newcard = new int[i];
		for (int j=0; j<i; j++){
			newcard[j] = card[j];
		}
		CardType newType = CardType.getType(newcard, cardtype);
		if (newType.name.equals("double_flush")){
			this.card.get(0).add(0, newcard);
			this.cardTypes.add(0, newType);
		}else if (newType.name.equals("double")){
			this.card.get(1).add(newcard);
			this.cardTypes.add(newType);
		}else {
			this.card.get(2).add(newcard);
			this.cardTypes.add(newType);
		}
		
		int[] nextcard = new int[card.length-i];
		for (int j=0; j<nextcard.length; j++){
			nextcard[j] = card[i+j];
		}
		init(nextcard, cardtype);
	}
	
	boolean isSameType(ComplexType c){
		if (this.flush_num != c.flush_num || this.double_num != c.double_num || 
			this.single_num != c.single_num || this.flush_length != c.flush_length)
			return false;
		if (this.card.get(0).size() != c.card.get(0).size() ||
			this.card.get(1).size() != c.card.get(1).size() ||
			this.card.get(2).size() != c.card.get(2).size()
			)
			return false;
		return true;
	}
	
	public int getPresent(){
		if (this.card.get(0).size() != 0)
			return this.card.get(0).get(0)[0];
		if (this.card.get(1).size() != 0)
			return this.card.get(0).get(1)[0];
		if (this.card.get(2).size() != 0)
			return this.card.get(0).get(2)[0];
		return 0;
	}
	void show(){
		System.out.println("flush "+this.flush_num);
		System.out.println("double "+this.double_num);
		System.out.println("single "+this.single_num);
		/*
		for (ArrayList<int[]> list:this.card){
			for (int[] c:list){
				for (int j:c){
					System.out.print(j+" ");
				}
				System.out.print(",  ");
			}
			System.out.println();
		}*/
	}
}
