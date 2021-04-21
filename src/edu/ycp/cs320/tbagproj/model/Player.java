package edu.ycp.cs320.tbagproj.model;

public class Player extends Actor {

	private int game_ID;
	
	public Player(String name) {
		super(name);
	}
	
	public Player() {
		super();
	}
	
	public void setGame_ID(int g) {
		game_ID = g;
	}
	
	public int getGame_ID() {
		return game_ID;
	}
}
