package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class Game {
	private boolean close = false;
	private Command enter = new Command();
	private String key;
	private Player player;
	private Room currentRoom;
	private Map fullMap;
	private Attack attackModel = new Attack();
	
	public Game(){
		enter.setCommands();
	}
	
	private void exitGame() {
			close = true;
	}
	
	public boolean getExitGame() {
		return close;
	}
	
	private void newGame() {
		player = new Player("Player");
		Map map = new Map();
		NPC cultist = new NPC("cultist");
		cultist.setHealth(-75.0);
		NPC ghost = new NPC("ghost");
		ghost.setHealth(2.0);
		
		//foyer creation
		Room foyer = new Room(null, "Foyer", null, null);
		foyer.setDescription("A large room that has two pillars that reach up to the ceiling. There are some paintings on the walls with a door to the north, and one too the west.");
		map.addRoom("Foyer", foyer);
		foyer.updateContents();
		
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
		ArrayList<NPC> mainHallNPCList = new ArrayList<NPC>();
		mainHallNPCList.add(cultist);
		Inventory mainHallInventory = new Inventory();
		mainHallInventory.addGold(5);
		Room mainHall = new Room(mainHallInventory, "Main Hall", mainHallNPCList, mainHallConnections);
		mainHall.setDescription("A grand corridor filled with painting, sculptures, and tapastries. It has a door to the north, south, east, and west.");
		closet.updateContents();
		mainHall.updateContents();
		foyer.setConnections("north", mainHall);
		map.addRoom("Main Hall", mainHall);
		
		//West Hallway Creation
		HashMap<String, Room> westHallwayConnections = new HashMap<>();
		westHallwayConnections.put("east", mainHall);
		ArrayList<NPC> westHallwayNPCList = new ArrayList<NPC>();
		westHallwayNPCList.add(ghost);
		Inventory westHallwayInv = new Inventory();
		westHallwayInv.addNewItem("Sword", 2.0, 0.0, 0.0, true);
		Room westHallway = new Room(westHallwayInv, "West Hallway", westHallwayNPCList, westHallwayConnections);
		westHallway.setDescription("A hallway with a mirror at the end. It has a door to the north and south.");
		westHallway.updateContents();
		mainHall.setConnections("west", westHallway);
		map.addRoom("West Hallway", westHallway);
		
		//creates full map and sets starting room
		fullMap = map;
		currentRoom = fullMap.findRoom("Foyer");
	}
	
	private void saveGame() {
		
	}
	
	private void loadGame() {
		
	}
	
	//receives command from user and runs the game
	public String runGame(String prompt) {
		HashMap<String, Room> tempRoomsMap = new HashMap<String, Room>();
		//enter.setCommands();
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
				NPC currentNPC = currentRoom.getNPC(enter.getSecond());
				currentNPC.setHealth(attackModel.attack(player, currentNPC, true) * -1);
				if (currentNPC.getCurHealth() <= 0) {
					currentRoom.deleteNPC(enter.getSecond());
					currentRoom.updateContents();
				}else {
					currentRoom.updateNPC(enter.getSecond(), currentNPC);
				}
				if (currentRoom.getNPCs() != null) {
					for (int i = 0; i < currentRoom.getNPCs().size(); i++) {
						String tempNPCName = currentRoom.getNPCs().get(i).getName();
						player.setHealth(attackModel.attack(player, currentRoom.getNPC(tempNPCName), false) * -1);
						totalDamageTaken += attackModel.attack(player, currentRoom.getNPC(tempNPCName), false) * -1;
					}
					totalDamageTaken *= -1;
				}
				if (totalDamageTaken == 0 ) {
					return "You hit for " + player.getTotalDamage() + ". Your armor blocked all damage." + "\n" + currentRoom.getDescription() + currentRoom.getContents();
				}
				return "You hit for " + player.getTotalDamage() + ". You took " + totalDamageTaken + "." + "\n" + currentRoom.getDescription() + currentRoom.getContents();
			case 6:
				newGame();
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
				currentRoom.updateContents();
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
			case 13:
				if(player.getInventory().getItemList().isEmpty())
					return "Your inventory is empty.";
				else {
				String items = "";
				ListIterator<Item> iter = player.getInventory().getItemList().listIterator();
				
				
				while (iter.hasNext()) {
					if(!iter.hasPrevious())
						items += " " + iter.next().getName();
					else
						items += ", " + iter.next().getName();
				}
				return "Your inventory has" + items;
			}
		}
		return "Command could not be processed";
	}

}
