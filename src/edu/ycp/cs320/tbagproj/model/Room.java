package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room {
	private Inventory inventory;
	private ArrayList<NPC> NPCList;
	private HashMap<String, Room> connections;
	private String name;
	private String description;
	
	public Room() {
		inventory = new Inventory();
		NPCList = new ArrayList<NPC>();
		connections = new HashMap<String, Room>();
	}
	
	public Room(Inventory inventory, String name, ArrayList<NPC> NPCList, HashMap<String, Room> connections) {
		this.inventory = inventory;
		this.name = name;
		this.NPCList = NPCList;
		this.connections = connections;
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String descrip) {
		description = descrip;
	}
	
	public String getDescription() {
		return description;
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
