package com.lux.trump.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.lux.trump.shared.ChatDTO;
import com.lux.trump.shared.Message;
import com.lux.trump.shared.RealTimeGameUpdateDTO;
import com.lux.trump.shared.RoomDTO;
import com.lux.trump.shared.UserGameInfoDTO;
import com.lux.trump.shared.UserInfoDTO;

@RemoteServiceRelativePath("TrumpService")
public interface TrumpService extends RemoteService {
	/*
	 * Interact with user service
	 */
	Boolean login(String username, String password);
	Boolean usernameExists(String username);
	void signup(String username, String password);
	
	UserInfoDTO userInfo(String username);
	UserGameInfoDTO userGameInfo(String username, String gameName);
	
	void appendMessage(String username, Message message);
	ArrayList<Message> messages(String username);
	
	/*
	 * Interact with room service
	 */
	ArrayList<String> gameList();
	ArrayList<RoomDTO> roomList(String game);
	
	String xmlRule(int roomID);
	ArrayList<UserGameInfoDTO> usersInRoom(int roomID);
	ChatDTO chatsInRoom(int roomID);
	RealTimeGameUpdateDTO realtime(int roomID, String username);
	void updateRealTime(int roomID, RealTimeGameUpdateDTO update);
}
