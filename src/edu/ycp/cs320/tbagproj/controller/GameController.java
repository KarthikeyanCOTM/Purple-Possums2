package edu.ycp.cs320.tbagproj.controller;

import edu.ycp.cs320.tbagproj.model.*;

public class GameController {
	Command command;
	String prompt;
	Room room;
	Player player;
	
	public void main() {
		command.setCommands();
		command.setPrompt(prompt);
		command.processPrompt(player, room);
	}
}
