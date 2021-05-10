package edu.ycp.cs320.tbagproj.controller;

import edu.ycp.cs320.tbagproj.model.*;

public class GameController {
	String prompt;
	Game game;
	boolean end = false;
	
	public void setModel(Game game) {
		this.game = game;
	}
	
	public Game getModel() {
		return game;
	}
	
	public String gameRun(String prompt) {
		return game.runGame(prompt);
	}
}
