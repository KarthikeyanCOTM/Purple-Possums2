package edu.ycp.cs320.tbagproj.model;

public class Player extends Actor {

	private int game_ID;
	private String roomName;
	private int player_ID;
	
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
	
	public void setRoomName(String r) {
		roomName = r;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public void setPlayer_ID(int p) {
		player_ID = p;
	}
	
	public int getPlayer_ID() {
		return player_ID;
	}
}
