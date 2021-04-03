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
	private Game game = new Game();
	
	public String getPrompt(){
		return prompt;
	}
	
	public void setPrompt(String input) {
		//Scanner keyboard = new Scanner(System.in);
		prompt = input;
		first = "";
		second = "";
		for (int x = 0; x < prompt.length(); x++) {
			if (prompt.charAt(x) == ' ') {
				for (int y = x + 1; y < prompt.length(); y++) {
					if (prompt.charAt(y) == ' ') {
						y = prompt.length();
					}else {
						second += prompt.charAt(y);
					}
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
	
	public void processPrompt(Player player, Room room) {
		if (commands.contains(first) == true && prompt == "north") {
			
		}else if (commands.contains(first) == true && first == "east") {
			
		}else if (commands.contains(first) == true && first == "south") {
			
		}else if (commands.contains(first) == true && first == "west") {
			
		}else if (commands.contains(first) == true && first == "attack") {
			if (room.containsNPC(second) == true) {
				attackModel.attack(player, room.getNPC(second), true);
			}
		}else if (commands.contains(first) == true && first == "new") {
			game.newGame();
		}else if (commands.contains(first) ==  true && first == "load") {
			
		}else if (commands.contains(first) == true && first == "save") {
			
		}
	}
}
