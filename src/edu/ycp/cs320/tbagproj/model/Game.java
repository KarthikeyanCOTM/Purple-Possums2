package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
	private boolean close = false;
	private Command enter;
	private String key;
	
	public void exitGame() {
		if(enter.getPrompt() == "quit") {
			close = true;
		}
		else {
			close = false;
		}
	}
	
	public void newGame() {
		HashMap<String, Room> rooms = new HashMap<>();
		Map map = new Map();
		map.createRoom(null, "Foyar", null, rooms);
	}
	
	public void saveGame() {
		if(enter.getPrompt() == "save game") {
			
		}
			
	}
	
	public void loadGame() {
		if(enter.getPrompt() == "load game") {
			
		}
			
	}

}
