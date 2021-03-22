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
	
	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
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
		if (prompt.contains("move")) {
			
		}
		if (prompt.contains("attack")) {
			
		}
		if (prompt.contains("use")) {
			
		}
	}
}
