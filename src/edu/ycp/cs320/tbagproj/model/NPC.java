package edu.ycp.cs320.tbagproj.model;

public class NPC extends Actor {
	private boolean isNPCAlive = true;
	
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
}
