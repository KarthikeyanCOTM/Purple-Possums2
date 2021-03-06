package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room {
	private Inventory inventory;
	private ArrayList<NPC> NPCList = null;
	private HashMap<String, Room> connections;
	private HashMap<String, Integer> connectionsID;
	private String name;
	private String description = " ";
	private String contents = "The room has ";
	private int inventory_ID = 0;
	private int room_ID;
	private int game_ID;
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	
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
		updateContents();
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
				if (NPCList.get(i).getIsNPCAlive() == true) {
					contents += NPCList.get(i).getName();
				}else {
					contents += NPCList.get(i).getName() + " that lies dead";
				}
				if (i + 1 < NPCList.size()) {
					contents += ", ";
				}
			}
			contents += ".";
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
		if (NPCList != null && NPCList.isEmpty() == false) {
			return NPCList;
		}else {
			return null;
		}
	}
	
	public ArrayList<NPC> getLivingNPCs() {
		ArrayList<NPC> livingNPCs = new ArrayList<NPC>();
		if (NPCList != null && NPCList.isEmpty() == false) {
			for (int i = 0; i < NPCList.size(); i++) {
				if (NPCList.get(i).getIsNPCAlive() == true) {
					livingNPCs.add(NPCList.get(i));
				}
			}
			if (livingNPCs != null && livingNPCs.isEmpty() == false) {
				return livingNPCs;
			}else {
				return null;
			}
		}
		return null;
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
	
	public void addNPC(NPC npc) {
		this.NPCList.add(npc);
	}
	
	public NPC getNPC(int element) {
		return this.NPCList.get(element);
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
	
	public HashMap<String, Integer> getConnectionsID(){
		return connectionsID;
	}
	
	public void setConnectionsID(String key, int ID) {
		connectionsID.put(key, ID);
	}
	
	public boolean isConnected(Room other) {
		if (connections.containsValue(other))
			return true;
		return false;
	}
	
	public void setInventory_ID(int i) {
		inventory_ID = i;
	}
	
	public int getInventory_ID() {
		return inventory_ID;
	}
	
	public void setRoom_ID(int r) {
		room_ID = r;
	}
	
	public int getRoom_ID() {
		return room_ID;
	}
	
	public void setGame_ID(int g) {
		game_ID = g;
	}
	
	public int getGame_ID() {
		return game_ID;
	}
}
