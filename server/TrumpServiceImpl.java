package com.lux.trump.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lux.trump.client.TrumpService;
import com.lux.trump.server.room.RoomService;
import com.lux.trump.shared.ChatDTO;
import com.lux.trump.shared.GameInfo;
import com.lux.trump.shared.Message;
import com.lux.trump.shared.RealTimeGameUpdateDTO;
import com.lux.trump.shared.RoomDTO;
import com.lux.trump.shared.UserGameInfoDTO;
import com.lux.trump.shared.UserInfoDTO;

public class TrumpServiceImpl extends RemoteServiceServlet implements
		TrumpService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1507301164161024473L;

	@Override
	public Boolean login(String username, String password) {
		// this is just a sample. TODO: finish the back-end part of the authentication.
		return new Boolean(true);
	}

	@Override
	public ArrayList<String> gameList() {
		ArrayList<String> gameList = new ArrayList<String>();
		gameList.add("斗地主");
		gameList.add("升级");
		return gameList;
	}

	@Override
	public ArrayList<RoomDTO> roomList(String game) {
		RoomDTO room = new RoomDTO(0);
		room.appendUser(new UserGameInfoDTO("kimi", "sb", 0, 100, true));
		room.appendUser(new UserGameInfoDTO("lmx", "2b", 0, 100, true));
		room.appendUser(new UserGameInfoDTO("uncle", "nb", 110, 100, true));
		room.gameinfo = new GameInfo("斗地主");
		RoomService.appendRoom(room);
		
		room = new RoomDTO(1);
		room.appendUser(new UserGameInfoDTO("alice", "sb", 0, 100, true));
		room.appendUser(new UserGameInfoDTO("bob", "sb", 0, 100, true));
		room.appendUser(new UserGameInfoDTO("cathy", "sb", 0, 100, true));
		room.gameinfo = new GameInfo("斗地主");
		RoomService.appendRoom(room);

		room = new RoomDTO(2);
		room.appendUser(new UserGameInfoDTO("david", "sb", 0, 100, true));
		room.appendUser(new UserGameInfoDTO("eric", "sb", 0, 100, true));
		room.appendUser(new UserGameInfoDTO("flank", "sb", 0, 100, true));
		room.gameinfo = new GameInfo("升级");
		RoomService.appendRoom(room);

		System.out.println(game);
		return RoomService.getRoomList(game);
	}

	@Override
	public String xmlRule(int roomID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<UserGameInfoDTO> usersInRoom(int roomID) {
		return RoomService.userInRoom(roomID);
	}

	@Override
	public ChatDTO chatsInRoom(int roomID) {
		return RoomService.chatInRoom(roomID);
	}

	@Override
	public Boolean usernameExists(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void signup(String username, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserInfoDTO userInfo(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserGameInfoDTO userGameInfo(String username, String gameName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void appendMessage(String username, Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Message> messages(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RealTimeGameUpdateDTO realtime(int roomID, String username) {
		return RoomService.realtime(roomID, username);
		// TODO Auto-generated method stub
		//return null;
	}

	@Override
	public void updateRealTime(int roomID, RealTimeGameUpdateDTO update) {
		// TODO Auto-generated method stub
		RoomService.updateRealTime(roomID, update);
	}

}