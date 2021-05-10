package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.List;

import database.DerbyDatabase;

public class Game {
	private boolean close = false;
	private Command enter = new Command();
	private String key;
	private Player player;
	private Room currentRoom;
	private Map fullMap;
	private Attack attackModel = new Attack();
	private DerbyDatabase db = new DerbyDatabase();
	private boolean isLoaded = false;
	
	public Game(){
		enter.setCommands();
	}
	
	private void exitGame() {
			close = true;
	}
	
	public boolean getExitGame() {
		return close;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Map getMap() {
		return fullMap;
	}
	
	public void setMap(Map fullMap) {
		this.fullMap = fullMap;
	}
	
	private void newGame() {
		player = new Player("Player");
		//Map map = new Map();
		player.setInventory_ID(4);
		player.setEquipment_ID(1);
		player.setPlayer_ID(2);
		Map map = new Map();
		NPC cultist = new NPC("cultist");
		cultist.setHealth(-85.0);
		cultist.setInventory_ID(5);
		cultist.setEquipment_ID(2);
		cultist.setNPC_ID(2);
		NPC ghost = new NPC("ghost");
		ghost.setHealth(2.0);
		
		//foyer creation
		Room foyer = new Room(null, "Foyer", null, null);
		foyer.setDescription("A large room that has two pillars that reach up to the ceiling. There are some paintings on the walls with a door to the north, and one too the west.");
		foyer.setInventory_ID(1);
		Inventory i = new Inventory();
		i.setOwner("Foyer");
		foyer.setInventory(i);
		foyer.setRoom_ID(3);
		foyer.updateContents();
		
		//closet creation
		HashMap<String, Room> closetConnections = new HashMap<>();
		closetConnections.put("east", foyer);
		Inventory closetInventory = new Inventory();
		closetInventory.addNewItem("fur armor", 0, 1, 0, false);
		closetInventory.getItem("fur armor").setInventory_ID(2);
		closetInventory.addNewItem("wooden sword", 3, 0, 0, false);
		closetInventory.getItem("wooden sword").setInventory_ID(2);
		closetInventory.addNewItem("wooden staff", 3, 0, 0, false);
		closetInventory.getItem("wooden staff").setInventory_ID(2);
		closetInventory.addNewItem("healing potion", 0, 0, 25, true);
		closetInventory.getItem("healing potion").setInventory_ID(2);
		closetInventory.setOwner("Closet");
		Room closet = new Room(closetInventory, "Closet", null, closetConnections);
		closet.setDescription("A walk in closet that has various coats, boots, and scarves along with a large open trunk. There is a door to the east.");
		closet.setInventory_ID(2);
		closet.setRoom_ID(4);
		map.addRoom("Closet", closet);
		foyer.setConnections("west", closet);
		
		//main hall creation
		HashMap<String, Room> mainHallConnections = new HashMap<>();
		mainHallConnections.put("south", foyer);
		ArrayList<NPC> mainHallNPCList = new ArrayList<NPC>();
		mainHallNPCList.add(cultist);
		Inventory mainHallInventory = new Inventory();
		mainHallInventory.addGold(5);
		mainHallInventory.setOwner("Main Hall");
		Room mainHall = new Room(mainHallInventory, "Main Hall", mainHallNPCList, mainHallConnections);
		mainHall.setDescription("A grand corridor filled with painting, sculptures, and tapastries. It has a door to the north, south, east, and west.");
		closet.updateContents();
		mainHall.updateContents();
		mainHall.setInventory_ID(3);
		mainHall.setRoom_ID(5);
		foyer.setConnections("north", mainHall);
		map.addRoom("Foyer", foyer);
		
		//West Hallway Creation
		HashMap<String, Room> westHallwayConnections = new HashMap<>();
		westHallwayConnections.put("east", mainHall);
		ArrayList<NPC> westHallwayNPCList = new ArrayList<NPC>();
		westHallwayNPCList.add(ghost);
		Inventory westHallwayInv = new Inventory();
		westHallwayInv.addNewItem("Sword", 5.0, 0.0, 0.0, true);
		Room westHallway = new Room(westHallwayInv, "West Hallway", westHallwayNPCList, westHallwayConnections);
		westHallway.setDescription("A hallway with a mirror at the end. It has a door to the north and south.");
		westHallway.updateContents();
		mainHall.setConnections("west", westHallway);
		map.addRoom("West Hallway", westHallway);
		map.addRoom("Main Hall", mainHall);
		
		//creates full map and sets starting room
		fullMap = map;
		currentRoom = fullMap.findRoom("Foyer");
	}
	
	private String saveGame(String key) {
		List<Room> roomList = new ArrayList<Room>();
		roomList.add(fullMap.getRooms().get("Foyer"));
		roomList.add(fullMap.getRooms().get("Closet"));
		roomList.add(fullMap.getRooms().get("Main Hall"));
		return db.saveGame(key, player, roomList);
	}
	
	private void loadGame(String key) {
		List<Room> roomList = new ArrayList<Room>();
		roomList.addAll(fullMap.getRooms().values());
		  db.loadGame(key, player, roomList);
	}
	
	private void deleteTables() {
		db.deleteGame();
	}
	
	//receives command from user and runs the game
	public String runGame(String prompt) {
		HashMap<String, Room> tempRoomsMap = new HashMap<String, Room>();
		enter.setCommands();
		enter.setPrompt(prompt);
		int result = enter.processPrompt(player, currentRoom);
		if (isLoaded == true) {
			if (player.getEquipment().containsItem("steel helmet of healing") == true) {
				player.setHealth(player.getEquipment().getItem("steel helmet of healing").getHealing());
			}
			if (currentRoom.getLivingNPCs() != null) {
				for (int i = 0; i < currentRoom.getNPCs().size(); i++) {
					String tempNPCName = currentRoom.getNPCs().get(i).getName();
					player.setHealth(attackModel.attack(player, currentRoom.getNPC(tempNPCName), false) * -1);
				}
			}
		}
		switch (result) {
			case 0:
				return "invalid command";
			case 1:
				tempRoomsMap = currentRoom.getConnections();
				if (tempRoomsMap.containsKey("north")){
					currentRoom = tempRoomsMap.get("north");
					player.setRoom_ID(currentRoom.getRoom_ID());
					return currentRoom.getDescription() + currentRoom.getContents();
				}
				else {
					return "You cannot go that way.";
				}
			case 2:
				tempRoomsMap = currentRoom.getConnections();
				if (tempRoomsMap.containsKey("east")){
					currentRoom = tempRoomsMap.get("east");
					player.setRoom_ID(currentRoom.getRoom_ID());
					return currentRoom.getDescription() + currentRoom.getContents();
				}
				else {
					return "You cannot go that way.";
				}
			case 3:
				tempRoomsMap = currentRoom.getConnections();
				if (tempRoomsMap.containsKey("south")){
					currentRoom = tempRoomsMap.get("south");
					player.setRoom_ID(currentRoom.getRoom_ID());
					return currentRoom.getDescription() + currentRoom.getContents();
				}
				else {
					return "You cannot go that way.";
				}
			case 4:
				tempRoomsMap = currentRoom.getConnections();
				if (tempRoomsMap.containsKey("west")){
					currentRoom = tempRoomsMap.get("west");
					player.setRoom_ID(currentRoom.getRoom_ID());
					return currentRoom.getDescription() + currentRoom.getContents();
				}
				else {
					return "You cannot go that way.";
				}
			case 5:
				double totalDamageTaken = 0;
				double totalHealed = 0;
				double totalDamageDelt = 0;
				String outputText;
				NPC currentNPC = currentRoom.getNPC(enter.getSecond());
				if (player.getEquipment().containsItem("steel sword of draining") == true) {
					player.setHealth(attackModel.attack(player, currentNPC, true));
					totalHealed = attackModel.attack(player, currentNPC, true);
				}
				totalDamageDelt = attackModel.attack(player, currentNPC, true);
				System.out.println(currentNPC.getCurHealth());
				currentNPC.setHealth(totalDamageDelt * -1);
				System.out.println(currentNPC.getCurHealth());
				if (currentNPC.getCurHealth() <= 0) {
					currentNPC.setIsNPCAlive(false);
					currentRoom.updateNPC(currentNPC.getName(), currentNPC);
					currentRoom.updateContents();
				}else {
					currentRoom.updateNPC(enter.getSecond(), currentNPC);
				}
				if (currentRoom.getLivingNPCs() != null) {
					for (int i = 0; i < currentRoom.getNPCs().size(); i++) {
						String tempNPCName = currentRoom.getNPCs().get(i).getName();
						totalDamageTaken += attackModel.attack(player, currentRoom.getNPC(tempNPCName), false);
						player.setHealth(attackModel.attack(player, currentRoom.getNPC(tempNPCName), false) * -1);
					}
				}
				if (totalDamageTaken == 0 && totalHealed == 0) {
					outputText = "You hit for " + totalDamageDelt + ". Your armor blocked all damage.";
					return outputText;
				}else if (totalHealed == 0) {
					outputText = "You hit for " + totalDamageDelt + ". You took " + totalDamageTaken;
					return outputText;
				}else if (totalDamageTaken == 0) {
					outputText = "You hit for " + totalDamageDelt + ". You healed for " + totalHealed + ". Your armor blocked all damage.";
					return outputText;
				}else {
					outputText = "You hit for " + totalDamageDelt + ". You healed for " + totalHealed + ". You took " + totalDamageTaken;
				}
			case 6:
				newGame();
				isLoaded = true;
				return currentRoom.getDescription() + currentRoom.getContents();
			case 7:
				Item tempItem = currentRoom.getInventory().getItem(enter.getSecond());
				int tempGold = currentRoom.getInventory().getGold();
				if (tempItem != null) {
					tempItem.setInventory_ID(player.getInventory_ID());
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
				//db.updateGame("temp", player, roomList);
				return "You unequipped the " + enter.getSecond() + ".\n" + currentRoom.getDescription() + currentRoom.getContents();
			case 10:
				ArrayList<String> tempList = enter.getCommands();
				String tempString = "";
				for (int i = 0; i < tempList.size(); i++) {
					tempString += tempList.get(i) + ",\n";
				}
				//db.updateGame("temp", player, roomList);
				return tempString;
			case 11:
				double tempHealing = player.getInventory().getItem(enter.getSecond()).getHealing();
				player.setHealth(tempHealing);
				player.getInventory().removeItem(enter.getSecond());
				//db.updateGame("temp", player, roomList);
				return "You healed for " + tempHealing + "." + "\n" + currentRoom.getDescription() + currentRoom.getContents();
			case 12:
				loadGame(enter.getSecond());
				return "Load Successful " + currentRoom.getDescription() + currentRoom.getContents();
			case 13:
				if(player.getInventory().getItemList().isEmpty()) {
					//db.updateGame("temp", player, roomList);
					return "Your inventory is empty.";
				}
				else {
				String items = "";
				ListIterator<Item> iter = player.getInventory().getItemList().listIterator();
				
				
				while (iter.hasNext()) {
					if(!iter.hasPrevious())
						items += " " + iter.next().getName();
					else
						items += ", " + iter.next().getName();
				}
				//db.updateGame("temp", player, roomList);
				return "Your inventory has" + items;
				}
			case 14:
				return saveGame(enter.getSecond());
			case 15:
				if (player.getInventory().containsItem(enter.getSecond()) == true) {
					tempItem = player.getInventory().getItem(enter.getSecond());
					return enter.getSecond() + "\nDamage: " + tempItem.getDamage() + "\nArmor: " + tempItem.getArmour() + "\nHealing: " + tempItem.getHealing();
				}else if (currentRoom.getInventory().containsItem(enter.getSecond()) == true) {
					tempItem = currentRoom.getInventory().getItem(enter.getSecond());
					return enter.getSecond() + "\nDamage: " + tempItem.getDamage() + "\nArmor: " + tempItem.getArmour() + "\nHealing: " + tempItem.getHealing();
				}
				return enter.getSecond() + " does not exist";
			case 16:
				deleteTables();
				return "Delete has worked.";
			case 17:
				if(player.getEquipment().getItemList().isEmpty()) {
					//db.updateGame("temp", player, roomList);
					return "Your equipment is empty.";
				}
				else {
				String items = "";
				ListIterator<Item> iter = player.getEquipment().getItemList().listIterator();
				
				
				while (iter.hasNext()) {
					if(!iter.hasPrevious())
						items += " " + iter.next().getName();
					else
						items += ", " + iter.next().getName();
				}
				//db.updateGame("temp", player, roomList);
				return "Your equipment has" + items;
				}
		}
		return "Command could not be processed";
	}

}
