package edu.ycp.cs320.tbagproj.model;

public class NPC extends Actor {
	private boolean isNPCAlive = true;
	private int npc_id;
	
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
	
	public void setNPC_ID(int n) {
		npc_id = n;
	}
	
	public int getNPC_ID() {
		return npc_id;
	}
}
