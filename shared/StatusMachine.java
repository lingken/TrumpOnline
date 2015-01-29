package com.lux.trump.shared;

import java.util.ArrayList;

import com.google.gwt.xml.client.Element;


public class StatusMachine {
	
	private ArrayList<Element> statuses = new ArrayList<Element>();
	private Element runningStatus;

	public void appendStatus(Element procedureElement) {
		statuses.add(procedureElement);
	}
	
	private int currentStatus;
	
	public StatusMachine() {
		currentStatus = 0;
	}
	
	public boolean hasNext() {
		return currentStatus < statuses.size();
	}
	
	public Element nextStatus() {
		return hasNext() ? (runningStatus = statuses.get(currentStatus++)) : null;
	}
	
	public Element getStatus() {
		return runningStatus;
	}
}
