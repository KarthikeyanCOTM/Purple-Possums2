package edu.ycp.cs320.tbagproj.controller;

import java.util.Scanner;

import edu.ycp.cs320.tbagproj.model.*;

public class GameController {
	Scanner keyboard = new Scanner(System.in);
	Command command;
	String prompt;
	Room room;
	Player player = null;
	Game game = null;
	boolean end = false;
	
	public void setModel(Game game) {
		this.game = game;
		command.setCommands();
	}
	
	public String gameRun(String prompt) {
		return game.runGame(prompt);
	}
}
