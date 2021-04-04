package edu.ycp.cs320.tbagproj.model;

import java.util.*;


public class Command {
	private String prompt;
	private Stack<String> previous;
	private ArrayList<String> commands;
	private String first;
	private String second;
	private Attack attackModel = new Attack();
	private NPC npcModel = new NPC();
	private Game gameModel = new Game();
	private Inventory inventoryModel = new Inventory();
	
	public String getPrompt(){
		return prompt;
	}
	
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
		return commands;
	}
	
	public void setCommands() {
		commands.add("north");
		commands.add("east");
		commands.add("south");
		commands.add("west");
		commands.add("attack");
		commands.add("get");
		commands.add("use");
		commands.add("new");
		commands.add("save");
		commands.add("load");
		commands.add("help");
		commands.add("equip");
		commands.add("unequip");
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
	
	public int processPrompt(Player player, Room room) {
		if (commands.contains(first) == true) {
			if (prompt == "north") {
				return 1;
			}else if (first == "east") {
				return 2;
			}else if (first == "south") {
				return 3;
			}else if (first == "west") {
				return 4;
			}else if (first == "attack") {
				if (room.containsNPC(second) == true) {
					return 5;
				}
				return 0;
			}else if (first == "new") {
				gameModel.newGame();
				return 6;
			}else if (first == "load") {
			
			}else if (first == "save") {
			
			}else if (first == "get") {
				if (room.getInventory().containsItem(second) == true) {
					return 7;
				}
				return 0;
			}else if (first == "equip") {
				if (player.getInventory().containsItem(second) == true) {
					return 8;
				}
				return 0;
			}else if (first == "unequip") {
				if (player.getEquipment().containsItem(second) == true) {
					return 9;
				}
				return 0;
			}else if (first == "help") {
				return 10;
			}else if (first == "use") {
				if (player.getInventory().containsItem(second) == true && player.getInventory().getItem(second).getIsUsable() == true) {
					return 11;
				}
				return 0;
			}
		}
		return 0;
	}
}
