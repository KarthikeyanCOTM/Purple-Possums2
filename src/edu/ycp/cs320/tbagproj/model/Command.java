package edu.ycp.cs320.tbagproj.model;

import java.util.*;

public class Command {
	String prompt;
	Stack<String> previous;
	ArrayList<String> commands;
	
	public String getPrompt(){
		return prompt;
	}
	
	public void setPrompt() {
		Scanner keyboard = new Scanner(System.in);
		prompt = keyboard.nextLine();
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
		commands.add("new game");
		commands.add("save game");
		commands.add("load game");
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
	
	public void processPrompt() {
		if (commands.contains(prompt) == true && prompt == "north") {
			
		}
		else if (commands.contains(prompt) == true && prompt == "east") {
			
		}
		else if (commands.contains(prompt) == true && prompt == "south") {
			
		}
		else if (commands.contains(prompt) == true && prompt == "west") {
			
		}
		else if (commands.contains(prompt) == true && prompt == "attack") {
			
		}
	}
}
