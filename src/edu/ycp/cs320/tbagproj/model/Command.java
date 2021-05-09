package edu.ycp.cs320.tbagproj.model;

import java.util.*;


public class Command {
	private String prompt;
	private Stack<String> previous = new Stack<String>();
	private ArrayList<String> commandsList = new ArrayList<String>();
	private String first;
	private String second;
	private Attack attackModel = new Attack();
	private NPC npcModel = new NPC();
	private Inventory inventoryModel = new Inventory();
	
	public String getPrompt(){
		return prompt;
	}
	
	//Takes user command and breaks in into two pieces
	public void setPrompt(String input) {
		prompt = input;
		first = "";
		second = "";
		for (int x = 0; x < prompt.length(); x++) {
			if (prompt.charAt(x) == ' ') {
				for (int y = x + 1; y < prompt.length(); y++) {
						second += prompt.charAt(y);
				}
				x = prompt.length();
			}else {
				first += prompt.charAt(x);
			}
		}
		setLatest();
	}
	
	public String getFirst() {
		return first;
	}
	
	public String getSecond() {
		return second;
	}
	
	public ArrayList<String> getCommands(){
		return commandsList;
	}
	
	//sets all usable commands
	public void setCommands() {
		if(commandsList.isEmpty()) {
		commandsList.add("north");
		commandsList.add("east");
		commandsList.add("south");
		commandsList.add("west");
		commandsList.add("attack");
		commandsList.add("get");
		commandsList.add("use");
		commandsList.add("new");
		commandsList.add("save");
		commandsList.add("load");
		commandsList.add("help");
		commandsList.add("equip");
		commandsList.add("unequip");
		commandsList.add("inventory");
		commandsList.add("delete");
		commandsList.add("inspect");
		}

	}
	
	public Stack<String> getPrevious(){
		return previous;
	}
	
	public void setPrevious(Stack<String> previous){
		this.previous = previous;
	}
	
	public String getLatest() {
		return previous.pop();
	}
	
	public void setLatest() {
		previous.add(prompt);
	}
	
	//process the first and second part of the command
	public int processPrompt(Player player, Room room) {
		if (commandsList.contains(first) == true) {
			switch (first) {
				case "north":
					return 1;
				case "east":
					return 2;
				case "south":
					return 3;
				case "west":
					return 4;
				case "attack":
					if (room.containsNPC(second) == true) {
						return 5;
					}
					return 0;
				case "new":
					return 6;
				case "load":
					return 12;
				case "save":
					return 14;
				case "delete":
					return 16;
				case "get":
					Inventory tempInventory = room.getInventory();
					if (tempInventory.containsItem(second) == true || (second.equals("gold") && tempInventory.getGold() > 0)) {
						return 7;
					}
					return 0;
				case "equip":
					if (player.getInventory().containsItem(second) == true) {
						return 8;
					}
					return 0;
				case "unequip":
					if (player.getEquipment().containsItem(second) == true) {
						return 9;
					}
					return 0;
				case "help":
					return 10;
				case "use":
					if (player.getInventory().containsItem(second) == true && player.getInventory().getItem(second).getIsUsable() == true) {
						return 11;
					}
					return 0;
				case "inventory" :
						return 13;
				case "inspect" :
					return 15;
			}
		}
		return 0;
	}
}
