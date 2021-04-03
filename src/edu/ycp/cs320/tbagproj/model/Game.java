package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
	private boolean close = false;
	private Command enter;
	private String key;
	
	public void exitGame() {
			close = true;
	}
	
	public void newGame() {
		HashMap<String, Room> Rooms = new HashMap<>();
		Map map = new Map();
		map.createRoom(null, "Foyar", null, Rooms);
	}
	
	public void saveGame() {
		
	}
	
	public void loadGame() {
		
	}
	
	public void runGame(String prompt) {
		enter.setPrompt(prompt);
	}

}
