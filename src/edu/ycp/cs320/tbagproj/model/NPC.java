package edu.ycp.cs320.tbagproj.model;

public class NPC extends Actor {
	private boolean isNPCAlive = true;
	private int room_ID;
	
	public NPC() {
		super();
	}
	
	public NPC(String name) {
		super(name);
	}
	
	public boolean getIsNPCAlive() {
		return isNPCAlive;
	}
	
	public void setIsNPCAlive(boolean alive) {
		isNPCAlive = alive;
	}
	
	public void setRoom_ID(int r) {
		room_ID = r;
	}
	
	public int getRoom_ID() {
		return room_ID;
	}
}
