package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import edu.ycp.cs320.tbagproj.model.*;

public class InitialData {
	
	private static final int VOID = 0;
	
	public static List<Room> getRooms() throws IOException {
		List<Room> roomList = new ArrayList<Room>();
		HashMap<Integer, Room> roomMap = new HashMap<Integer, Room>();
		ReadCSV readRooms = new ReadCSV("rooms.csv");
		HashMap<Integer, Inventory> inventoryMap = getInventorys();
		try {
			Integer roomID = 0;
			ArrayList<NPC> npcList = new ArrayList<NPC>();
			String temp;
			while (true) {
				List<String> tuple = readRooms.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Room room = new Room();
				int tempCount = 0;
				
				/*if (tempCount < 3) {
					i.next();
					i.next();
				}*/
				roomID = Integer.parseInt(i.next());
				tempCount++;
				npcList = getNPCs(roomID);
				
				room.setName(i.next());
				temp = i.next();
				room.setInventory(inventoryMap.get(Integer.parseInt(temp)));
				room.setInventory_ID(Integer.parseInt(temp));
				if (npcList != null) {
					room.setNPCs(npcList);
				}
				room.setDescription(i.next());
				room.setRoom_ID(roomID);
				room.setGame_ID(Integer.parseInt(i.next()));
				roomMap.put(roomID, room);
			}
			getConnections(roomMap);
			for (int x = 0; x < roomMap.size(); x++) {
				roomList.add(roomMap.get(x));
			}
			return roomList;
		}finally {
			readRooms.close();
		}
	}
	
	public static HashMap<Integer, Inventory> getInventorys() throws IOException {
		HashMap<Integer, Inventory> inventoryMap = new HashMap<Integer, Inventory>();
		ReadCSV readInven = new ReadCSV("inventory.csv");
		try {
			Integer inventoryID = 0;
			ArrayList<Item> itemList = new ArrayList<Item>();
			while (true) {
				List<String> tuple = readInven.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Inventory inven = new Inventory();
				
				inventoryID = Integer.parseInt(i.next());
				
				itemList = getItems(false, inventoryID);
				
				inven.setItems(itemList);
				inven.addGold(Integer.parseInt(i.next()));
				inven.setOwner(i.next());
				inven.setInventory_ID(inventoryID);
				inventoryMap.put(inventoryID, inven);
			}
			return inventoryMap;
		}finally {
			readInven.close();
		}
	}
	
	public static HashMap<Integer, Inventory> getEquipments() throws IOException {
		HashMap<Integer, Inventory> equipmentMap = new HashMap<Integer, Inventory>();
		ReadCSV readEquip = new ReadCSV("equipment.csv");
		try {
			Integer equipmentID = 0;
			ArrayList<Item> itemList = new ArrayList<Item>();
			while (true) {
				List<String> tuple = readEquip.next();
				String temp;
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Inventory equip = new Inventory();
				
				temp = i.next();
				equipmentID = Integer.parseInt(temp);
				
				itemList = getItems(false, equipmentID);
				
				equip.setItems(itemList);
				equip.addGold(Integer.parseInt(i.next()));
				equip.setOwner(i.next());
				equipmentMap.put(equipmentID, equip);
			}
			return equipmentMap;
		}finally {
			readEquip.close();
		}
	}
	
	public static ArrayList<Item> getItems(boolean isInven, int id) throws IOException {
		ArrayList<Item> itemList = new ArrayList<Item>();
		ReadCSV readItem = new ReadCSV("item.csv");
		try {
			Integer itemID = 0;
			while (true) {
				List<String> tuple = readItem.next();
				int inven;
				int equip;
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Item item = new Item();
				
				Integer.parseInt(i.next());
				
				item.setName(i.next());
				item.setDamage(Integer.parseInt(i.next()));
				item.setArmour(Integer.parseInt(i.next()));
				item.setHealing(Integer.parseInt(i.next()));
				item.setIsUsable(Boolean.parseBoolean(i.next()));
				String temp = i.next();
				if (temp == null && isInven == false) {
					equip = Integer.parseInt(i.next());
					item.setEquipment_ID(equip);
					item.setInventory_ID(VOID);
					if (equip == id) {
						itemList.add(item);
					}
				}else {
					inven = Integer.parseInt(temp);
					item.setInventory_ID(inven);
					item.setEquipment_ID(VOID);
					if (inven == id) {
						itemList.add(item);
					}
				}
			}
			return itemList;
		}finally {
			readItem.close();
		}
	}
	
	public static ArrayList<Item> getAllItems(boolean isInven) throws IOException {
		ArrayList<Item> itemList = new ArrayList<Item>();
		ReadCSV readItem = new ReadCSV("item.csv");
		try {
			Integer itemID = 0;
			while (true) {
				List<String> tuple = readItem.next();
				int inven;
				int equip;
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Item item = new Item();
				
				item.setItem_ID(Integer.parseInt(i.next()));
				item.setName(i.next());
				item.setDamage(Integer.parseInt(i.next()));
				item.setArmour(Integer.parseInt(i.next()));
				item.setHealing(Integer.parseInt(i.next()));
				item.setIsUsable(Boolean.parseBoolean(i.next()));
				String temp = i.next();
				if (temp == "0") {
					equip = Integer.parseInt(i.next());
					item.setInventory_ID(VOID);
					item.setEquipment_ID(equip);
					itemList.add(item);
				}else {
					inven = Integer.parseInt(temp);
					item.setInventory_ID(inven);
					item.setEquipment_ID(VOID);
					itemList.add(item);
				}
			}
			return itemList;
		}finally {
			readItem.close();
		}
	}
	
	public static ArrayList<NPC> getNPCs(int roomID) throws IOException {
		ArrayList<NPC> npcList = new ArrayList<NPC>();
		ReadCSV readNPC = new ReadCSV("npcs.csv");
		HashMap<Integer, Inventory> inventoryMap = getInventorys();
		HashMap<Integer, Inventory> equipmentMap = getEquipments();
		try {
			Integer npcID = 0;
			String temp;
			while (true) {
				List<String> tuple = readNPC.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				NPC npc = new NPC();
				
				npc.setNPC_ID(Integer.parseInt(i.next()));
				
				npc.setName(i.next());
				npc.setNewHealth(Double.parseDouble(i.next()));
				npc.setDefence(Double.parseDouble(i.next()));
				npc.setTotalDamage(Double.parseDouble(i.next()));
				temp = i.next();
				npc.setInventory(inventoryMap.get(Integer.parseInt(temp)));
				npc.setInventory_ID(Integer.parseInt(temp));
				temp = i.next();
				npc.setEquipment(equipmentMap.get(Integer.parseInt(temp)));
				npc.setEquipment_ID(Integer.parseInt(temp));
				npc.setIsNPCAlive(Boolean.parseBoolean(i.next()));
				if (roomID == Integer.parseInt(i.next())) {
					npc.setRoom_ID(roomID);
					npcList.add(npc);
				}
			}
			return npcList;
		}finally {
			readNPC.close();
		}
	}
	
	public static ArrayList<NPC> getAllNPCs() throws IOException {
		ArrayList<NPC> npcList = new ArrayList<NPC>();
		ReadCSV readNPC = new ReadCSV("npcs.csv");
		HashMap<Integer, Inventory> inventoryMap = getInventorys();
		HashMap<Integer, Inventory> equipmentMap = getEquipments();
		try {
			Integer npcID = 0;
			String temp;
			while (true) {
				List<String> tuple = readNPC.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				NPC npc = new NPC();
				
				npc.setNPC_ID(Integer.parseInt(i.next()));
				
				npc.setName(i.next());
				npc.setNewHealth(Double.parseDouble(i.next()));
				npc.setDefence(Double.parseDouble(i.next()));
				npc.setTotalDamage(Double.parseDouble(i.next()));
				temp = i.next();
				npc.setInventory(inventoryMap.get(Integer.parseInt(temp)));
				npc.setInventory_ID(Integer.parseInt(temp));
				temp = i.next();
				npc.setEquipment(equipmentMap.get(Integer.parseInt(temp)));
				npc.setEquipment_ID(Integer.parseInt(temp));
				npc.setIsNPCAlive(Boolean.parseBoolean(i.next()));
				npc.setRoom_ID(Integer.parseInt(i.next()));
				npcList.add(npc);
			}
			return npcList;
		}finally {
			readNPC.close();
		}
	}

	public static Player getPlayer() throws IOException {
		Player player = new Player();
		ReadCSV readPlayers = new ReadCSV("players.csv");
		HashMap<Integer, Inventory> inventoryMap = getInventorys();
		HashMap<Integer, Inventory> equipmentMap = getEquipments();
		List<Room> roomList = getRooms();
		try {
			Integer playerID = 0;
			String temp;
			while (true) {
				List<String> tuple = readPlayers.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				
				player.setPlayer_ID(Integer.parseInt(i.next()));
				
				player.setName(i.next());
				player.setNewHealth(Double.parseDouble(i.next()));
				player.setDefence(Double.parseDouble(i.next()));
				player.setTotalDamage(Double.parseDouble(i.next()));
				temp = i.next();
				player.setInventory(inventoryMap.get(Integer.parseInt(temp)));
				player.setInventory_ID(Integer.parseInt(temp));
				temp = i.next();
				player.setEquipment(equipmentMap.get(Integer.parseInt(temp)));
				player.setEquipment_ID(Integer.parseInt(temp));
				player.setGame_ID(Integer.parseInt(i.next()));
				player.setRoom_ID(Integer.parseInt(i.next()));
				player.setRoomName(roomList.get(player.getRoom_ID()).getName());
			}
			return player;
		}finally {
			readPlayers.close();
		}
	}
	
	public static void getConnections(HashMap<Integer, Room> roomMap) throws IOException {
		ReadCSV readConnections = new ReadCSV("connections.csv");
		try {
			Integer x = 0;
			while (true) {
				List<String> tuple = readConnections.next();
				Room tempRoom1 = new Room();
				Room tempRoom2 = new Room();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				
				Integer.parseInt(i.next());
				
				int room1 = Integer.parseInt(i.next());
				int room2 = Integer.parseInt(i.next());
				int direction = Integer.parseInt(i.next());
				
				if (direction == 0) {
					tempRoom1 = roomMap.get(room1);
					tempRoom2 = roomMap.get(room2);
					tempRoom1.setConnections("north", tempRoom2);
					roomMap.replace(room1, tempRoom1);
				}else if (direction == 1) {
					tempRoom1 = roomMap.get(room1);
					tempRoom2 = roomMap.get(room2);
					tempRoom1.setConnections("east", tempRoom2);
					roomMap.replace(room1, tempRoom1);
				}else if (direction == 2) {
					tempRoom1 = roomMap.get(room1);
					tempRoom2 = roomMap.get(room2);
					tempRoom1.setConnections("sout", tempRoom2);
					roomMap.replace(room1, tempRoom1);
				}else {
					tempRoom1 = roomMap.get(room1);
					tempRoom2 = roomMap.get(room2);
					tempRoom1.setConnections("west", tempRoom2);
					roomMap.replace(room1, tempRoom1);
				}
			}
		}finally {
			readConnections.close();
		}
	}

	public static ArrayList<String> getGames() throws IOException {
		ArrayList<String> games = new ArrayList<String>();
		ReadCSV readGames = new ReadCSV("games.csv");
		try {
			Integer gameID;
			String name;
			while (true) {
				List<String> tuple = readGames.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				
				gameID = Integer.parseInt(i.next());
				name = i.next();
				games.add(name);
			}
			return games;
		}finally {
			readGames.close();
		}
	}
	
}
