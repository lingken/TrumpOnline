package com.lux.trump.server.game;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	public ArrayList<String> cards = new ArrayList<String>();
	public String name;
	
	public Deck(String name) {
		this.name = name;
	}
	
	public Deck() {
		
	}
	
	public synchronized void add(String card) {
		cards.add(card);
	}
	
	public synchronized void shuffle() {
		Collections.shuffle(cards);
	}
	
	public synchronized String dealOne() {
		String card = cards.get(cards.size() - 1);
		cards.remove(cards.size() - 1);
		return card;
	}
	
	public synchronized void append(String[] cardList) {
		for (String card: cardList) {
			cards.add(card);
		}
	}
	
	public void removeOne(String s) {
		String target = null;
		for (String card: cards) {
			if (card.equals(s)) {
				target = card;
				break;
			}
		}
		cards.remove(target);
	}
	
	public boolean empty() {
		return cards.isEmpty();
	}
}
