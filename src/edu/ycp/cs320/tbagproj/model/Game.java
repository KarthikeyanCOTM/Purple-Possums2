package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
	private boolean close = false;
	private Command enter;
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
		HashMap<String, Room> Rooms = new HashMap<>();
		enter.setPrompt(prompt);
		int result = enter.processPrompt(player, currentRoom);
		switch (result) {
			case 1:
				Rooms = currentRoom.getConnections();
				currentRoom = Rooms.get("north");
				break;
			case 2:
				Rooms = currentRoom.getConnections();
				currentRoom = Rooms.get("east");
				break;
			case 3:
				Rooms = currentRoom.getConnections();
				currentRoom = Rooms.get("south");
				break;
			case 4:
				Rooms = currentRoom.getConnections();
				currentRoom = Rooms.get("west");
				break;
			case 5:
				attackModel.attack(player, currentRoom.getNPC(enter.getSecond()), true);
				break;
			case 6:
				player.getInventory().addItem(currentRoom.getInventory().getItem(enter.getSecond()));
				currentRoom.getInventory().removeItem(enter.getSecond());
				break;
			case 7:
				player.equipItem(enter.getSecond());
				break;
			case 8:
				player.unequipItem(enter.getSecond());
				break;
			case 9:
				ArrayList<String> tempList = enter.getCommands();
				for (int i = 0; i < tempList.size(); i++) {
					System.out.println(tempList.get(i));
				}
				break;
			default:
				System.out.println("Invalid command");
				
				
		}
	}

}
