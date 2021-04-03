package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room {
	private Inventory items;
	private ArrayList<NPC> NPCList;
	private HashMap<String, Room> connections;
	private String name;
	private String description;
	
	public Room() {
		
	}
	
	public Room(Inventory items, String name, ArrayList<NPC> NPCList, HashMap<String, Room> connections) {
		this.items = items;
		this.name = name;
		this.NPCList = NPCList;
		this.connections = connections;
	}
	
	public Inventory getInventory(){
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
	
	public ArrayList<NPC> getNPCs(){
		return NPCList;
	}
	
	public NPC getNPC(String name) {
		for (int x = 0; x < NPCList.size(); x++) {
			if (NPCList.get(x).getName() == name) {
				return NPCList.get(x);
			} 
		}
		return null;
	}
	
	public void setNPCs(ArrayList<NPC> NPCList){
		this.NPCList = NPCList;
	}
	
	public void addNPCs(ArrayList<NPC> NPCList) {
		this.NPCList.addAll(NPCList);
	}
	
	public void removeNPCs(ArrayList<NPC> NPCList) {
		this.NPCList.removeAll(NPCList);
	}
	
	public boolean containsNPC(String name) {
		for (int x = 0; x < NPCList.size(); x++) {
			if (NPCList.get(x).getName() == name) {
				return true;
			}
		}
		return false;
	}
	
	public HashMap<String, Room> getConnections(){
		return connections;
	}
	
	public void setConnections(String key, Room connection) {
		connections.put(key, connection);
	}
	
	public boolean isConnected(Room other) {
		if (connections.containsValue(other))
			return true;
		return false;
	}
	
}
