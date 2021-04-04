package edu.ycp.cs320.tbagproj.controller;

import java.util.Scanner;

import edu.ycp.cs320.tbagproj.model.*;

public class GameController {
	Scanner keyboard = new Scanner(System.in);
	Command command;
	String prompt;
	Room room;
	Player player;
	Game game = new Game();
	boolean end = false;
	
	public void main() {
		prompt = keyboard.nextLine();
		command.setCommands();
		command.setPrompt(prompt);
		command.processPrompt(player, room);
		end = game.getExitGame();
		while(end = false) {
			prompt = keyboard.nextLine();
			game.runGame(prompt);
		}
	}
}
