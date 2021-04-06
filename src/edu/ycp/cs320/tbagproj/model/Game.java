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
		Map map = new Map();
		ArrayList<NPC> tempNPCList = new ArrayList<NPC>();
		NPC cultist = new NPC("cultist");
		
		//foyer creation
		Room foyer = new Room(null, "Foyer", null, null);
		foyer.setDescription("A large room that has two pillars that reach up to the ceiling. There are some paintings on the walls with a door to the north, and one too the west.");
		foyer.setContents();
		
		//closet creation
		HashMap<String, Room> closetConnections = new HashMap<>();
		closetConnections.put("east", foyer);
		Inventory closetInventory = new Inventory();
		closetInventory.addNewItem("fur armor", 0, 1, 0, false);
		closetInventory.addNewItem("wooden sword", 3, 0, 0, false);
		closetInventory.addNewItem("wooden staff", 3, 0, 0, false);
		closetInventory.addNewItem("healing potion", 0, 0, 25, true);
		Room closet = new Room(closetInventory, "Closet", null, closetConnections);
		closet.setDescription("A walk in closet that has various coats, boots, and scarves along with a large open trunk. There is a door to the east.");
		map.addRoom("Closet", closet);
		foyer.setConnections("west", closet);
		
		//main hall creation
		HashMap<String, Room> mainHallConnections = new HashMap<>();
		mainHallConnections.put("south", foyer);
		tempNPCList.add(cultist);
		Inventory mainHallInventory = new Inventory();
		mainHallInventory.addGold(5);
		Room mainHall = new Room(mainHallInventory, "Main Hall", tempNPCList, mainHallConnections);
		mainHall.setDescription("A grand corridor filled with painting, sculptures, and tapastries. It has a door to the north, south, east, and west.");
		closet.setContents();
		mainHall.setContents();
		foyer.setConnections("north", mainHall);
		map.addRoom("Foyer", foyer);
		map.addRoom("Main Hall", mainHall);
		
		//creates full map and sets starting room
		fullMap = map;
		currentRoom = fullMap.findRoom("Foyer");
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
				double totalDamageTaken = 0;
				currentRoom.getNPC(enter.getSecond()).setHealth(attackModel.attack(player, currentRoom.getNPC(enter.getSecond()), true) * -1);
				if (currentRoom.getNPC(enter.getSecond()).getCurHealth() <= 0) {
					currentRoom.deleteNPC(enter.getSecond());
					currentRoom.setContents();
				}
				if (currentRoom.getNPCs() != null) {
					for (int i = 0; i < currentRoom.getNPCs().size(); i++) {
						String tempNPCName = currentRoom.getNPCs().get(i).getName();
						player.setHealth(attackModel.attack(player, currentRoom.getNPC(tempNPCName), false) * -1);
						totalDamageTaken += attackModel.attack(player, currentRoom.getNPC(tempNPCName), false) * -1;
					}
					totalDamageTaken *= -1;
				}
				return "You hit for " + player.getTotalDamage() + ". You took " + totalDamageTaken + "." + "\n" + currentRoom.getDescription() + currentRoom.getContents();
			case 6:
				newGame();
				System.out.print(currentRoom.getConnections().get("west").getInventory().getItem("healing potion").getName());
				return currentRoom.getDescription() + currentRoom.getContents();
			case 7:
				Item tempItem = currentRoom.getInventory().getItem(enter.getSecond());
				int tempGold = currentRoom.getInventory().getGold();
				if (tempItem != null) {
					player.getInventory().addNewItem(tempItem.getName(), tempItem.getDamage(), tempItem.getArmour(), tempItem.getHealing(), tempItem.getIsUsable());
				}
				player.getInventory().addGold(tempGold);
				currentRoom.getInventory().removeItem(enter.getSecond());
				currentRoom.getInventory().addGold(tempGold * -1);
				currentRoom.setContents();
				return "You picked up the " + enter.getSecond() + ".\n" + currentRoom.getDescription() + currentRoom.getContents();
			case 8:
				player.equipItem(enter.getSecond());
				return "You equipped the " + enter.getSecond() + ".\n" + currentRoom.getDescription() + currentRoom.getContents();
			case 9:
				player.unequipItem(enter.getSecond());
				return "You unequipped the " + enter.getSecond() + ".\n" + currentRoom.getDescription() + currentRoom.getContents();
			case 10:
				ArrayList<String> tempList = enter.getCommands();
				String tempString = "";
				for (int i = 0; i < tempList.size(); i++) {
					tempString += tempList.get(i) + ",\n";
				}
				return tempString;
			case 11:
				double tempHealing = player.getInventory().getItem(enter.getSecond()).getHealing();
				player.setHealth(tempHealing);
				player.getInventory().removeItem(enter.getSecond());
				return "You healed for " + tempHealing + "." + "\n" + currentRoom.getDescription() + currentRoom.getContents();
			case 12:
				return "Load Successful";
				
		}
		return "Command could not be processed";
	}

}
