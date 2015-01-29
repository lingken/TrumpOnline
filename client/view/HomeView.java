package com.lux.trump.client.view;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.lux.trump.client.presenter.HomePresenter;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FlexTable;

public class HomeView extends Composite implements HomePresenter.Display{
	private Label lblNewLabel;
	private Button logoutButton;
	private FlexTable table;
	
	public HomeView() {
		
		FlowPanel verticalPanel = new FlowPanel();
		initWidget(verticalPanel);
		
		lblNewLabel = new Label("");
		verticalPanel.add(lblNewLabel);
		
		logoutButton = new Button("Log out");
		verticalPanel.add(logoutButton);
		
		table = new FlexTable();
		verticalPanel.add(table);
		// please create the view here.
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void setUsername(String username) {
		this.lblNewLabel.setText(username);
	}

	@Override
	public HasClickHandlers getLogOutButton() {
		return logoutButton;
	}

	@Override
	public HasClickHandlers getList() {
		return table;
	}

	@Override
	public int getClickedRow(ClickEvent event) {
		HTMLTable.Cell cell = table.getCellForEvent(event);
		return cell.getRowIndex();
	}

	@Override
	public void setGameList(ArrayList<String> gameList) {
		table.removeAllRows();
		int i = 0;
		for (String game : gameList) {
			Button b = new Button();
			b.setText(game);
			table.setWidget(i++, 0, b);
		}
	}
}
