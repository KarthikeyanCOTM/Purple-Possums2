package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room {
	Inventory items;
	ArrayList<NPC> NPCList;
	HashMap<String, Room> connections;
	String name;
	
	public Room() {
		
	}
	
	public Room(Inventory items, String name, ArrayList<NPC> NPCList, HashMap<String, Room> connections) {
		this.items = items;
		this.name = name;
		this.NPCList = NPCList;
		this.connections = connections;
	}
	
	public Inventory GetInventory(){
		return items;
	}
	
	public void setInventory(Inventory items) {
		this.items = items;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<NPC> getNPC(){
		return NPCList;
	}
	
	public void setNPC(ArrayList<NPC> NPCList){
		this.NPCList = NPCList;
	}
	
	public void addNPCs(ArrayList<NPC> NPCList) {
		this.NPCList.addAll(NPCList);
	}
	
	public void removeNPCs(ArrayList<NPC> NPCList) {
		this.NPCList.removeAll(NPCList);
	}
	
	public HashMap<String, Room> getConnections(){
		return connections;
	}
	
	public void setConnections(HashMap<String, Room> connections) {
		this.connections = connections;
	}
	
	public boolean isConnected(Room other) {
		if (connections.containsValue(other))
			return true;
		return false;
	}
	
}
