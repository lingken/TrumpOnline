package com.lux.trump.client.presenter;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.lux.trump.client.TrumpServiceAsync;
import com.lux.trump.client.event.LoginSuccessEvent;

public class LoginPresenter implements Presenter {
	private final TrumpServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;
	
	public LoginPresenter(TrumpServiceAsync rpcService, HandlerManager eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
	}
	
	public interface Display {
		HasClickHandlers getLoginButton();
		String getUsername();
		String getPassword();
		void clear();
		void showErrorMessage();
		Widget asWidget();
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

	private void bind() {
		display.getLoginButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				authenticate(display.getUsername(), display.getPassword());
			}
		});
	}

	protected void authenticate(final String username, String password) {
		rpcService.login(username, password, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				display.clear();
				display.showErrorMessage();
				System.out.println(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result.equals(true)) {
					final long DURATION = 1000 * 60 * 60 * 24;
					Date expireDate = new Date(System.currentTimeMillis() + DURATION);
					Cookies.setCookie("user", username, expireDate);
					eventBus.fireEvent(new LoginSuccessEvent());
				}
				else {
					display.clear();
					display.showErrorMessage();
				}
			}
			
		});
	}
}
