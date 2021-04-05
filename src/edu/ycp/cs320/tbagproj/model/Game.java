package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
	private boolean close = false;
	private Command enter = new Command();
	private String key;
	private Player player;
	private Room currentRoom;
	private Map fullMap;
	private Attack attackModel = new Attack();
	private NPC npcModel = new NPC();
	private Inventory inventoryModel = new Inventory();
	
	public void exitGame() {
			close = true;
	}
	
	public boolean getExitGame() {
		return close;
	}
	
	public void newGame() {
		player = new Player("Player");
		HashMap<String, Room> Rooms = new HashMap<>();
		Map map = new Map();
		Room room = new Room();
		Room tempRoom = new Room();
		Item item = new Item();
		Inventory tempInventory = new Inventory();
		
		//foyar creation
		map.createRoom(null, "Foyar", null, Rooms);
		room.setDescription("A large room that has two pillars that reach up to the ceiling. There are some paintings on the walls with a door to the north, and one too the west.");
		room.setContents();
		
		//closet creation
		Rooms.put("east", room);
		map.createRoom(null, "Closet", null, Rooms);
		tempRoom = map.findRoom("Closet");
		tempRoom.setDescription("A walk in closet that has various coats, boots, and scarves along with a large open trunk. There is a door to the east.");
		tempInventory.addNewItem("fur armor", 0, 1, 0, false);
		tempInventory.addNewItem("wooden sword", 3, 0, 0, false);
		tempInventory.addNewItem("wooden staff", 3, 0, 0, false);
		tempInventory.addNewItem("healing potion", 0, 0, 25, true);
		tempRoom.setInventory(tempInventory);
		tempRoom.setContents();
		map.updateRoom("Closet", tempRoom);
		room.setConnections("west", tempRoom);
		
		//main hall creation
		Rooms.clear();
		Rooms.put("south", room);
		map.createRoom(null, "Main Hall", null, Rooms);
		tempRoom = map.findRoom("Main Hall");
		tempRoom.setDescription("A grand corridor filled with painting, sculptures, and tapastries. It has a door to the north, south, east, and west.");
		tempInventory.clearInventory();
		tempInventory.addGold(5);
		tempRoom.setInventory(tempInventory);
		tempRoom.setContents();
		map.updateRoom("MainHall", tempRoom);
		room.setConnections("north", tempRoom);
		map.updateRoom("Foyar", room);
		room = tempRoom;
		
		//creates full map and sets starting room
		fullMap = map;
		currentRoom = map.findRoom("Foyar");
	}
	
	public void saveGame() {
		
	}
	
	public void loadGame() {
		
	}
	
	public String runGame(String prompt) {
		HashMap<String, Room> tempRoomsMap = new HashMap<String, Room>();
		enter.setCommands();
		enter.setPrompt(prompt);
		int result = enter.processPrompt(player, currentRoom);
		switch (result) {
			case 0:
				return "invalid command";
			case 1:
				tempRoomsMap = currentRoom.getConnections();
				currentRoom = tempRoomsMap.get("north");
				return currentRoom.getDescription() + currentRoom.getContents();
			case 2:
				tempRoomsMap = currentRoom.getConnections();
				currentRoom = tempRoomsMap.get("east");
				return currentRoom.getDescription() + currentRoom.getContents();
			case 3:
				tempRoomsMap = currentRoom.getConnections();
				currentRoom = tempRoomsMap.get("south");
				return currentRoom.getDescription() + currentRoom.getContents();
			case 4:
				tempRoomsMap = currentRoom.getConnections();
				currentRoom = tempRoomsMap.get("west");
				return currentRoom.getDescription() + currentRoom.getContents();
			case 5:
				attackModel.attack(player, currentRoom.getNPC(enter.getSecond()), true);
				return "hit";
			case 6:
				newGame();
				return currentRoom.getDescription() + currentRoom.getContents();
			case 7:
				Item tempItem = currentRoom.getInventory().getItem(enter.getSecond());
				player.getInventory().addNewItem(tempItem.getName(), tempItem.getDamage(), tempItem.getArmour(), tempItem.getHealing(), tempItem.getIsUsable());
				currentRoom.getInventory().removeItem(enter.getSecond());
				currentRoom.setContents();
				break;
			case 8:
				player.equipItem(enter.getSecond());
				break;
			case 9:
				player.unequipItem(enter.getSecond());
				break;
			case 10:
				ArrayList<String> tempList = enter.getCommands();
				String tempString = "";
				for (int i = 0; i < tempList.size(); i++) {
					tempString += tempList.get(i) + "\n";
				}
				return tempString;
			case 11:
				player.setHealth(player.getInventory().getItem(enter.getSecond()).getHealing());
				player.getInventory().removeItem(enter.getSecond());
				break;
			case 12:
				return "Load Successful";
				
		}
		return "Command could not be processed";
	}

}
