package com.lux.trump.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lux.trump.shared.ChatDTO;
import com.lux.trump.shared.Message;
import com.lux.trump.shared.RealTimeGameUpdateDTO;
import com.lux.trump.shared.RoomDTO;
import com.lux.trump.shared.UserGameInfoDTO;
import com.lux.trump.shared.UserInfoDTO;

public interface TrumpServiceAsync {
	public void login(String username, String password, AsyncCallback<Boolean> callback);
	public void gameList(AsyncCallback<ArrayList<String>> callback);
	public void roomList(String game, AsyncCallback<ArrayList<RoomDTO>> callback);
	public void xmlRule(int roomID, AsyncCallback<String> callback);
	public void usersInRoom(int roomID, AsyncCallback<ArrayList<UserGameInfoDTO>> callback);
	public void chatsInRoom(int roomID, AsyncCallback<ChatDTO> callback);
	public void usernameExists(String username, AsyncCallback<Boolean> callback);
	public void signup(String username, String password, AsyncCallback<Void> callback);
	public void userInfo(String username, AsyncCallback<UserInfoDTO> callback);
	public void userGameInfo(String username, String gameName, AsyncCallback<UserGameInfoDTO> callback);
	public void appendMessage(String username, Message message, AsyncCallback<Void> callback);
	public void messages(String username, AsyncCallback<ArrayList<Message>> callback);
	public void realtime(int roomID, String username, AsyncCallback<RealTimeGameUpdateDTO> callback);
	public void updateRealTime(int roomID, RealTimeGameUpdateDTO update, AsyncCallback<Void> callback);
}
