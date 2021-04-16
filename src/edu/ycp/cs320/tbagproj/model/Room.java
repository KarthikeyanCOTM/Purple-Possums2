package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room {
	private Inventory inventory;
	private ArrayList<NPC> NPCList;
	private HashMap<String, Room> connections;
	private String name;
	private String description = " ";
	private String contents = "The room has ";
	
	public Room() {
		inventory = new Inventory();
		NPCList = new ArrayList<NPC>();
		connections = new HashMap<String, Room>();
	}
	
	public Room(Inventory inventory, String name, ArrayList<NPC> NPCList, HashMap<String, Room> connections) {
		this.inventory = inventory;
		this.name = name;
		this.NPCList = NPCList;
		if (connections == null) {
			this.connections = new HashMap<String, Room>();
		}else {
			this.connections = connections;
		}
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
		description = descrip + "\n";
	}
	
	public String getDescription() {
		return description;
	}
	
	//Sets the description of the items and npc's in the room
	private void setContents() {
		if (inventory != null && inventory.getItemList().isEmpty() == false) {
			for (int i = 0; i < inventory.getNumItems(); i++) {
				contents += inventory.getItemList().get(i).getName() + ", ";
			}
			contents += inventory.getGold() + " gold.";
		}else if (inventory != null && inventory.getGold() != 0) {
			contents += inventory.getGold() + " gold.";
		}else {
			contents += "nothing.";
		}
		if (NPCList != null && NPCList.isEmpty() == false) {
			contents += "\nThere is a ";
			for (int i = 0; i < NPCList.size(); i++) {
				contents += NPCList.get(i).getName();
			}
		}
	}
	
	public String getContents() {
		return contents;
	}
	
	public void updateContents() {
		contents = "The room has ";
		setContents();
	}
	
	public ArrayList<NPC> getNPCs(){
		return NPCList;
	}
	
	public NPC getNPC(String name) {
		for (int x = 0; x < NPCList.size(); x++) {
			if (NPCList.get(x).getName().equals(name)) {
				return NPCList.get(x);
			} 
		}
		return null;
	}
	
	public void setNPCs(ArrayList<NPC> NPCList){
		this.NPCList = NPCList;
	}
	
	public void updateNPC(String name, NPC npc) {
		for (int x = 0; x < NPCList.size(); x++) {
			if (NPCList.get(x).getName() == name) {
				NPCList.set(x, npc);
			} 
		}
	}
	
	public void addNPCs(ArrayList<NPC> NPCList) {
		this.NPCList.addAll(NPCList);
	}
	
	public void removeNPCs(ArrayList<NPC> NPCList) {
		this.NPCList.removeAll(NPCList);
	}
	
	public void deleteNPC(String name) {
		for (int i = 0; i < NPCList.size(); i++) {
			if (NPCList.get(i).getName().equals(name)) {
				NPCList.remove(i);
				i = NPCList.size();
			}
		}
	}
	
	public boolean containsNPC(String name) {
		for (int x = 0; x < NPCList.size(); x++) {
			if (NPCList.get(x).getName().equals(name)) {
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
