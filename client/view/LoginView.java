package com.lux.trump.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.lux.trump.client.presenter.LoginPresenter;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.FlowPanel;

public class LoginView extends Composite implements LoginPresenter.Display {
	private Button loginButton;
	private TextBox textBox;
	private PasswordTextBox passwordTextBox;
	private Label errorLabel;
	
	public LoginView() {
		
		FlowPanel verticalPanel = new FlowPanel();
		initWidget(verticalPanel);
		
		errorLabel = new Label("");
		verticalPanel.add(errorLabel);
		errorLabel.setHeight("31px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setSize("280px", "41px");
		
		Label lblUsername = new Label("Username");
		horizontalPanel.add(lblUsername);
		
		textBox = new TextBox();
		horizontalPanel.add(textBox);
		textBox.setWidth("157px");
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_1);
		horizontalPanel_1.setSize("281px", "37px");
		
		Label lblNewLabel = new Label("Password");
		horizontalPanel_1.add(lblNewLabel);
		
		passwordTextBox = new PasswordTextBox();
		horizontalPanel_1.add(passwordTextBox);
		passwordTextBox.setWidth("172px");
		
		loginButton = new Button("New button");
		loginButton.setText("Login");
		verticalPanel.add(loginButton);
		// Please create the view here.
	}

	@Override
	public HasClickHandlers getLoginButton() {
		return this.loginButton;
	}

	@Override
	public String getUsername() {
		return this.textBox.getText();
	}

	@Override
	public String getPassword() {
		return this.passwordTextBox.getText();
	}

	@Override
	public void clear() {
		this.textBox.setText("");
		this.passwordTextBox.setText("");
		this.errorLabel.setText("");
	}

	@Override
	public void showErrorMessage() {
		this.errorLabel.setText("Login error!");
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}

}
