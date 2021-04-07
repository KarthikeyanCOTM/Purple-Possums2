package edu.ycp.cs320.tbagproj.controller;

import edu.ycp.cs320.tbagproj.model.*;

public class GameController {
	Command command = new Command();
	String prompt;
	Game game;
	boolean end = false;
	
	public void setModel(Game game) {
		this.game = game;
	}
	
	public String gameRun(String prompt) {
		//command.setCommands();
		return game.runGame(prompt);
	}
}
