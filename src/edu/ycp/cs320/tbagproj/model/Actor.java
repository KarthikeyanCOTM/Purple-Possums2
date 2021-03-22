package edu.ycp.cs320.tbagproj.model;

// model class for Player and NPC
public class Actor {
	private String name;
	private double health = 100.0;
	
	public Actor(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public double getCurHealth() {
		return health;
	}
	
	public void setHealth(double health) {
		this.health += health;
	}
}
