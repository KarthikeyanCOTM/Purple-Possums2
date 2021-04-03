package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
	private boolean close = false;
	private Command enter;
	private String key;
	
	public void exitGame() {
			close = true;
	}
	
	public void newGame() {
		HashMap<String, Room> Rooms = new HashMap<>();
		Map map = new Map();
		Room room = new Room();
		Room tempRoom = new Room();
		Item item = new Item();
		Inventory tempInventory = new Inventory();
		
		//foyar creation
		map.createRoom(null, "Foyar", null, Rooms);
		room = map.findRoom("Foyar");
		room.setDescription("A large room that has two pillars that reach up to the ceiling. There are some paintings on the walls with a door to the north, and one two the west.");
		
		//closet creation
		Rooms.put("east", room);
		map.createRoom(null, "Closet", null, Rooms);
		tempRoom = map.findRoom("Closet");
		tempRoom.setDescription("A walk in closet that has various coats, boots, and scarves along with a large open trunk. There is a door to the east");
		tempInventory = tempRoom.getInventory();
		item.setName("Fur Armor");
		item.setArmour(1.0);
		tempInventory.addItem(item);
		item.setName("Wooden Sword");
		item.setArmour(0);
		item.setDamage(3.0);
		tempInventory.addItem(item);
		item.setName("Wooden Staff");
		tempInventory.addItem(item);
		tempRoom.setInventory(tempInventory);
		room.setConnections("west", tempRoom);
		
		//main hall creation
		Rooms.clear();
		Rooms.put("south", room);
		map.createRoom(null, "Main Hall", null, Rooms);
		tempRoom = map.findRoom("Main Hall");
		tempRoom.setDescription("A grand corridor filled with painting, sculptures, and tapastries. It has a door to the north, south, east, and west.");
		tempInventory = tempRoom.getInventory();
		tempInventory.addGold(5);
		tempRoom.setInventory(tempInventory);
		room.setConnections("north", tempRoom);
		room = tempRoom;
	}
	
	public void saveGame() {
		
	}
	
	public void loadGame() {
		
	}
	
	public void runGame(String prompt) {
		enter.setPrompt(prompt);
	}

}
