package edu.ycp.cs320.tbagproj.model;


import java.util.*;
import java.util.Map.*;

public class Map {
	private HashMap<String, Room> rooms = new HashMap<String, Room>();

	public HashMap<String, Room> getRooms(){
		return rooms;
	}
	
	public void setRooms(HashMap<String, Room> rooms) {
		this.rooms = rooms;
	}
	
	public void createRoom(Inventory items, String name, ArrayList<NPC> NPCList, HashMap<String, Room> connections) {
		Room room = new Room(items, name, NPCList, connections);
		rooms.put(name, room);
	}
	
	public void addRoom(String key, Room room) {
		rooms.put(key, room);
	}
	
	public Room findRoom(String name) {
		return rooms.get(name);
	}
	
	public Set<Entry<String, Room>> getSet() {
		return rooms.entrySet();
	}
	
	public void updateRoom(String key, Room room) {
		rooms.replace(key, room);
	}
}
