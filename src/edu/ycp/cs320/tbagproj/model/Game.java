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
	private Map fullMap = new Map();
	private Attack attackModel = new Attack();
	private DerbyDatabase db = new DerbyDatabase();
	private boolean isLoaded = false;
	
	public Game(){
		enter.setCommands();
		player = new Player();
		currentRoom = new Room();
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
		player.setInventory_ID(4);
		player.setEquipment_ID(1);
		player.setPlayer_ID(2);
		player.setRoom_ID(0);
		Map map = new Map();
		NPC cultist1 = new NPC("cultist");
		cultist1.setInventory_ID(5);
		cultist1.setEquipment_ID(2);
		cultist1.setNPC_ID(0);
		cultist1.setNewHealth(10.0);
		cultist1.setTotalDamage(2.0);
		NPC skellington = new NPC("skellington");
		skellington.setNewHealth(25.0);
		skellington.setTotalDamage(10.0);
		skellington.setNPC_ID(1);
		skellington.setInventory_ID(14);
		skellington.setEquipment_ID(3);
		NPC demon = new NPC("demon");
		demon.setNewHealth(40.0);
		demon.setTotalDamage(15.0);
		demon.setDefence(5.0);
		demon.setNPC_ID(2);
		demon.setInventory_ID(15);
		demon.setEquipment_ID(4);
		NPC cultist2 = new NPC("cultist");
		cultist2.setInventory_ID(13);
		cultist2.setEquipment_ID(2);
		cultist2.setNPC_ID(3);
		cultist2.setNewHealth(10.0);
		cultist2.setTotalDamage(2.0);
		NPC demonLord = new NPC("demon lord");
		demonLord.setNewHealth(50.0);
		demonLord.setTotalDamage(20.0);
		demonLord.setDefence(15.0);
		demonLord.setNPC_ID(4);
		demonLord.setInventory_ID(16);
		demonLord.setEquipment_ID(5);
		NPC demonKing = new NPC("demon king");
		demonKing.setNewHealth(100.0);
		demonKing.setDefence(20.0);
		demonKing.setTotalDamage(25.0);
		demonKing.setNPC_ID(5);
		demonKing.setInventory_ID(17);
		demonKing.setEquipment_ID(6);
		NPC cultist3 = new NPC("cultist1");
		cultist3.setInventory_ID(6);
		cultist3.setEquipment_ID(2);
		cultist3.setNPC_ID(3);
		cultist3.setNewHealth(10.0);
		cultist3.setTotalDamage(2.0);
		NPC cultist4 = new NPC("cultist2");
		cultist4.setInventory_ID(7);
		cultist4.setEquipment_ID(2);
		cultist4.setNPC_ID(3);
		cultist4.setNewHealth(10.0);
		cultist4.setTotalDamage(2.0);
		NPC cultist5 = new NPC("cultist");
		cultist4.setInventory_ID(7);
		cultist4.setEquipment_ID(2);
		cultist4.setNPC_ID(3);
		cultist4.setNewHealth(10.0);
		cultist4.setTotalDamage(2.0);
		NPC demon2 = new NPC("demon");
		demon.setNewHealth(40.0);
		demon.setTotalDamage(15.0);
		demon.setDefence(5.0);
		demon.setNPC_ID(2);
		demon.setInventory_ID(15);
		demon.setEquipment_ID(4);
		
		//foyer creation
		Room foyer = new Room(null, "Foyer", null, null);
		foyer.setDescription("A large room that has two pillars that reach up to the ceiling. There are some paintings on the walls with a door to the north, and one too the west.");
		foyer.setInventory_ID(0);
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
		closetInventory.getItem("fur armor").setInventory_ID(1);
		closetInventory.addNewItem("wooden sword", 3, 0, 0, false);
		closetInventory.getItem("wooden sword").setInventory_ID(1);
		closetInventory.addNewItem("wooden staff", 3, 0, 0, false);
		closetInventory.getItem("wooden staff").setInventory_ID(1);
		closetInventory.addNewItem("healing potion", 0, 0, 25, true);
		closetInventory.getItem("healing potion").setInventory_ID(1);
		closetInventory.setOwner("Closet");
		Room closet = new Room(closetInventory, "Closet", null, closetConnections);
		closet.setDescription("A walk in closet that has various coats, boots, and scarves along with a large open trunk. There is a door to the east.");
		closet.setInventory_ID(1);
		closet.setRoom_ID(4);
		map.addRoom("Closet", closet);
		foyer.setConnections("west", closet);
		
		//main hall creation
		HashMap<String, Room> mainHallConnections = new HashMap<>();
		mainHallConnections.put("south", foyer);
		ArrayList<NPC> mainHallNPCList = new ArrayList<NPC>();
		mainHallNPCList.add(cultist1);
		Inventory mainHallInventory = new Inventory();
		mainHallInventory.addGold(5);
		mainHallInventory.setOwner("Main Hall");
		Room mainHall = new Room(mainHallInventory, "Main Hall", mainHallNPCList, mainHallConnections);
		mainHall.setDescription("A grand corridor filled with painting, sculptures, and tapastries. It has a door to the north, south, east, and west.");
		closet.updateContents();
		mainHall.updateContents();
		mainHall.setInventory_ID(2);
		mainHall.setRoom_ID(5);
		foyer.setConnections("north", mainHall);
		map.addRoom("Foyer", foyer);
		
		//Sun Room Creation
		HashMap<String, Room> sunRoomConnections = new HashMap<String, Room>();
		sunRoomConnections.put("east", mainHall);
		ArrayList<NPC> sunRoomNPCList = new ArrayList<NPC>();
		sunRoomNPCList.add(skellington);
		Inventory sunRoomInven = new Inventory();
		sunRoomInven.setInventory_ID(3);
		Room sunRoom = new Room(sunRoomInven, "Sun Room", sunRoomNPCList, sunRoomConnections);
		sunRoom.setDescription("It is a sun room with large windows and a door that seems to lead to a court yard to the west.");
		sunRoom.setInventory_ID(sunRoomInven.getInventory_ID());
		sunRoom.updateContents();
		mainHall.setConnections("west", sunRoom);
		
		//Court Yard Creation
		HashMap<String, Room> courtYardConnections = new HashMap<String, Room>();
		courtYardConnections.put("east", sunRoom);
		Inventory courtYardInven = new Inventory();
		courtYardInven.setInventory_ID(4);
		Room courtYard = new Room(courtYardInven, "Court Yard", null, courtYardConnections);
		courtYard.setDescription("The court yard has multiple gardens in it with a variety of flowers. There is a door to the east that leads into a sun room.");
		courtYard.setInventory_ID(courtYardInven.getInventory_ID());
		courtYard.updateContents();
		sunRoom.setConnections("west", courtYard);
		map.addRoom("Sun Room", sunRoom);
		map.addRoom("Court Yard", courtYard);
		
		//Dinning Hall Creation
		HashMap<String, Room> dinningHallConnections = new HashMap<String, Room>();
		dinningHallConnections.put("south", mainHall);
		ArrayList<NPC> dinningHallNPCs = new ArrayList<NPC>();
		dinningHallNPCs.add(cultist2);
		dinningHallNPCs.add(demon);
		Inventory diningHallInven = new Inventory();
		diningHallInven.setInventory_ID(5);
		Room diningHall = new Room(diningHallInven, "Dining Hall", dinningHallNPCs, dinningHallConnections);
		diningHall.setDescription("There is a large dining table in the center of the room that has a large and ornate chandelier hanging above it. There is a door to the east and west.");
		diningHall.setInventory_ID(diningHallInven.getInventory_ID());
		diningHall.updateContents();
		mainHall.setConnections("north", diningHall);
		
		//Summoning Room
		HashMap<String, Room> summoningRoomConnections = new HashMap<String, Room>();
		summoningRoomConnections.put("east", diningHall);
		ArrayList<NPC> summoningRoomNPCs = new ArrayList<NPC>();
		summoningRoomNPCs.add(demonLord);
		Inventory summoningRoomInven = new Inventory();
		summoningRoomInven.setInventory_ID(6);
		summoningRoomInven.addNewItem("steel armor of harming", 5, 20, 0, false);
		Room summoningRoom = new Room(summoningRoomInven, "Summoning Room", summoningRoomNPCs, summoningRoomConnections);
		summoningRoom.setDescription("The room appears to have once been a waiting room of some kind but it is now filled with alters, candles, and symbols on the ground. There is a door to the north and east.");
		summoningRoom.setInventory_ID(summoningRoomInven.getInventory_ID());
		summoningRoom.updateContents();
		diningHall.setConnections("west", summoningRoom);
		
		//Throne Room
		HashMap<String, Room> throneRoomConnections = new HashMap<String, Room>();
		throneRoomConnections.put("south", summoningRoom);
		ArrayList<NPC> throneRoomNPCs = new ArrayList<NPC>();
		throneRoomNPCs.add(demonKing);
		Inventory throneRoomInven = new Inventory();
		throneRoomInven.setInventory_ID(7);
		throneRoomInven.addGold(40);
		Room throneRoom = new Room(throneRoomInven, "Throne Room", throneRoomNPCs, throneRoomConnections);
		throneRoom.setDescription("You enter into a grand room with tapestries lining the walls to the east and west. To the north is a throne made of stone with designs inlayed into it with gold. There is a door to the east and south.");
		throneRoom.setInventory_ID(throneRoomInven.getInventory_ID());
		throneRoom.updateContents();
		summoningRoom.setConnections("north", throneRoom);
		map.addRoom("Summoning Room", summoningRoom);
		
		//Barracks Creation
		HashMap<String, Room> barracksConnections = new HashMap<String, Room>();
		barracksConnections.put("west", mainHall);
		ArrayList<NPC> barracksNPCs = new ArrayList<NPC>();
		barracksNPCs.add(cultist3);
		barracksNPCs.add(cultist4);
		Inventory barracksInven = new Inventory();
		barracksInven.setInventory_ID(8);
		barracksInven.addGold(10);
		Room barracks = new Room(barracksInven, "Barracks", barracksNPCs, barracksConnections);
		barracks.setDescription("This room is filled with bunks and each has a foot locker at it's base. There is a door to the north and west.");
		barracks.setInventory_ID(barracks.getInventory_ID());
		barracks.updateContents();
		mainHall.setConnections("east", barracks);
		map.addRoom("Main Hall", mainHall);
		
		//Armory Creation
		HashMap<String, Room> armoryConnections = new HashMap<String, Room>();
		armoryConnections.put("south", barracks);
		armoryConnections.put("west", diningHall);
		Inventory armoryInven = new Inventory();
		armoryInven.addNewItem("iron armor", 0, 10, 0, false);
		armoryInven.getItem("iron armor").setInventory_ID(9);
		armoryInven.addNewItem("iron helmet", 0, 5, 0, false);
		armoryInven.getItem("iron helmet").setInventory_ID(9);
		armoryInven.addNewItem("iron sword", 15, 0, 0, false);
		armoryInven.getItem("iron sword").setInventory_ID(9);
		armoryInven.addNewItem("healing potion1", 0, 0, 25, true);
		armoryInven.getItem("healing potion1").setInventory_ID(9);
		armoryInven.addNewItem("healing potion2", 0, 0, 25, true);
		armoryInven.getItem("healing potion2").setInventory_ID(9);
		armoryInven.addNewItem("healing potion3", 0, 0, 25, true);
		armoryInven.getItem("healing potion3").setInventory_ID(9);
		armoryInven.setInventory_ID(9);
		Room armory = new Room(armoryInven, "Armory", null, armoryConnections);
		armory.setDescription("The room appears to be and armory. Most of it's shelves have been picked clean but a few items remain. There are doors to the north, south, and west.");
		armory.setInventory_ID(armoryInven.getInventory_ID());
		armory.updateContents();
		diningHall.setConnections("east", armory);
		map.addRoom("Dining Hall", diningHall);
		map.addRoom("Barracks", barracks);
		
		//Antichamber Creation
		HashMap<String, Room> antichamberConnections = new HashMap<String, Room>();
		antichamberConnections.put("south", armory);
		ArrayList<NPC> antichamberNPCs = new ArrayList<NPC>();
		antichamberNPCs.add(cultist5);
		Inventory antichamberInven = new Inventory();
		antichamberInven.setInventory_ID(10);
		Room antichamber = new Room(antichamberInven, "Antichamber", antichamberNPCs, antichamberConnections);
		antichamber.setDescription("You enter a small antichamber with a door to the south and west.");
		antichamber.setInventory_ID(antichamberInven.getInventory_ID());
		antichamber.updateContents();
		armory.setConnections("north", antichamber);
		map.addRoom("Armory", armory);
		
		//Bed Room Creation
		HashMap<String, Room> bedRoomConnections = new HashMap<String, Room>();
		bedRoomConnections.put("west", throneRoom);
		bedRoomConnections.put("east", antichamber);
		ArrayList<NPC> bedRoomNPCs = new ArrayList<NPC>();
		bedRoomNPCs.add(demon2);
		Inventory bedRoomInven = new Inventory();
		bedRoomInven.setInventory_ID(11);
		bedRoomInven.addNewItem("steel helmet of healing", 0, 20, 5, false);
		bedRoomInven.addNewItem("steel sword of draining", 25, 0, 0, false);
		bedRoomInven.addGold(20);
		Room bedRoom = new Room(bedRoomInven, "Bed Room", bedRoomNPCs, bedRoomConnections);
		bedRoom.setDescription("A grand bedroom with large and fluffy bed along the north wall and various dressers, and drawers, along with a desk and a few chairs near a fire place.");
		bedRoom.setInventory_ID(bedRoomInven.getInventory_ID());
		bedRoom.updateContents();
		antichamber.setConnections("west", bedRoom);
		map.addRoom("Antichamber", antichamber);
		map.addRoom("Throne Room", throneRoom);
		map.addRoom("Bedroom", bedRoom);
		
		//creates full map and sets starting room
		fullMap = map;
		currentRoom = foyer;
		//loadGame("initial");
	}
	
	private String saveGame(String key) {
		List<Room> roomList = new ArrayList<Room>();
		roomList.add(fullMap.getRooms().get("Foyer"));
		roomList.add(fullMap.getRooms().get("Closet"));
		roomList.add(fullMap.getRooms().get("Main Hall"));
		roomList.add(fullMap.getRooms().get("Sun Room"));
		roomList.add(fullMap.getRooms().get("Court Yard"));
		roomList.add(fullMap.getRooms().get("Dining Hall"));
		roomList.add(fullMap.getRooms().get("Summoning Room"));
		roomList.add(fullMap.getRooms().get("Throne Room"));
		roomList.add(fullMap.getRooms().get("Barracks"));
		roomList.add(fullMap.getRooms().get("Armory"));
		roomList.add(fullMap.getRooms().get("Antichamber"));
		roomList.add(fullMap.getRooms().get("Bedroom"));
		return db.saveGame(key, player, roomList);
	}
	
	private void loadGame(String key) {
		ArrayList<Room> roomList = new ArrayList<Room>();
		Room room0 = new Room();
		roomList.add(room0);
		Room room1 = new Room();
		roomList.add(room1);
		Room room2 = new Room();
		roomList.add(room2);
		Room room3 = new Room();
		roomList.add(room3);
		Room room4 = new Room();
		roomList.add(room4);
		Room room5 = new Room();
		roomList.add(room5);
		Room room6 = new Room();
		roomList.add(room6);
		Room room7 = new Room();
		roomList.add(room7);
		Room room8 = new Room();
		roomList.add(room8);
		Room room9 = new Room();
		roomList.add(room9);
		Room room10 = new Room();
		roomList.add(room10);
		Room room11 = new Room();
		roomList.add(room11);
		db.loadGame(key, player, roomList);
		fullMap.addRoom("Foyer", roomList.get(0));
		fullMap.addRoom("Closet", roomList.get(1));
		fullMap.addRoom("Main Hall", roomList.get(2));
		fullMap.addRoom("Sun Room", roomList.get(3));
		fullMap.addRoom("Court Yard", roomList.get(4));
		fullMap.addRoom("Dining Hall", roomList.get(5));
		fullMap.addRoom("Summoning Room", roomList.get(6));
		fullMap.addRoom("Throne Room", roomList.get(7));
		fullMap.addRoom("Barracks", roomList.get(8));
		fullMap.addRoom("Armory", roomList.get(9));
		fullMap.addRoom("Antichamber", roomList.get(10));
		fullMap.addRoom("Bedroom", roomList.get(11));
		currentRoom = roomList.get(player.getRoom_ID());
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
			if (player.getCurHealth() <= 0) {
				return "game over";
			}
			if (player.getEquipment().containsItem("steel helmet of healing") == true) {
				player.setHealth(player.getEquipment().getItem("steel helmet of healing").getHealing());
			}
			if (currentRoom.getLivingNPCs() != null && enter.getFirst() != "attack" && result != 0) {
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
				currentNPC.setHealth(totalDamageDelt * -1);
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
				if (player.getEquipment().containsEquippedItem(enter.getSecond()) == false) {
					player.equipItem(enter.getSecond());
					return "You equipped the " + enter.getSecond() + ".\n" + currentRoom.getDescription() + currentRoom.getContents();
				}else {
					return "That slot is already filled.";
				}
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
