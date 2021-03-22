package edu.ycp.cs320.tbagproj.model;

public class Game {
	boolean close = false;
	Command enter;
	
	public void exitGame() {
		if(enter.getPrompt() == "quit") {
			close = true;
		}
		else {
			close = false;
		}
	}
	
	public void newGame() {
		if(enter.getPrompt() == "new game") {
			
		}
			
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
