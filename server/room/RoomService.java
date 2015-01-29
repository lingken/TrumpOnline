package com.lux.trump.server.room;

import java.util.ArrayList;

import com.lux.trump.server.game.GameService;
import com.lux.trump.shared.ChatDTO;
import com.lux.trump.shared.RealTimeGameUpdateDTO;
import com.lux.trump.shared.RoomDTO;
import com.lux.trump.shared.UserGameInfoDTO;

public class RoomService {
	private static class InstanceHolder {
		private static RoomService instance = new RoomService();	
		private InstanceHolder(){
		}
	}
	
	public ArrayList<RoomDTO> roomlist = new ArrayList<RoomDTO>();
	public ArrayList<GameService> gameServiceList = new ArrayList<GameService>();
	public int roomID[] = new int[1000];   //max for 1000 rooms
	
	private RoomService() {
		for (int i=0; i<1000; i++)
			roomID[i] = -1;
	}
	
	public static RoomService getInstance() {
		return InstanceHolder.instance;
	}
	
	public static void appendRoom(RoomDTO room){
		if (getInstance().roomID[room.id] != -1){
			System.out.println("this id is been taken");
			return;
		}			
		getInstance().roomID[room.id] = getInstance().roomlist.size();
		getInstance().roomlist.add(room);
		getInstance().gameServiceList.add(new GameService(room.gameinfo.name, room.getUserInRoom()));
	}
	
	public static void removeRoom(RoomDTO room){
		int index = getInstance().roomID[room.id];
		getInstance().roomID[room.id] = -1;
		getInstance().roomlist.remove(index);
		getInstance().gameServiceList.remove(index);
	}
	
	public static ArrayList<RoomDTO> getRoomList(String name){
		ArrayList<RoomDTO> list = new ArrayList<RoomDTO>();
		for (RoomDTO room: getInstance().roomlist){
			if (room.gameinfo.name.equals(name)){
				list.add(room);
			}
		}
		return list;
	}
	
	public static String getXMLRule(int roomID){
		String xml = "";
		for (RoomDTO room:getInstance().roomlist){
			if (room.id == roomID){
				xml = room.gameinfo.xml;
				break;
			}
		}
		return xml;
	}
	
	public static ArrayList<UserGameInfoDTO> userInRoom(int roomID){
		ArrayList<UserGameInfoDTO> list = new ArrayList<UserGameInfoDTO>();
		for (RoomDTO room: getInstance().roomlist){
			if (room.id == roomID){
				list = room.users;
				break;
			}
		}
		return list;
	}
	
	public static ChatDTO chatInRoom(int roomID){
		for (RoomDTO room: getInstance().roomlist){
			if (room.id == roomID){ 
				return room.chatInRoom();
			}
		}
		return null;
	}
	
	public static boolean createRoom(UserGameInfoDTO user){
		for (int i=0; i<1000; i++){
			if (getInstance().roomID[i] == -1){
				RoomDTO room = new RoomDTO(i);
				room.appendUser(user);
				RoomService.appendRoom(room); 
				return true;
			}
		}
		
		return false;
	}

	public static RealTimeGameUpdateDTO realtime(int roomID, String username) {
		// TODO Auto-generated method stub
		int index = getInstance().roomID[roomID];
		return getInstance().gameServiceList.get(index).realTime(username);
	}

	public static void updateRealTime(int roomID, RealTimeGameUpdateDTO update) {
		// TODO Auto-generated method stub
		int index = getInstance().roomID[roomID];
		getInstance().gameServiceList.get(index).update(update);
	}
}
