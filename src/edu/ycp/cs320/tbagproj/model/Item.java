package edu.ycp.cs320.tbagproj.model;

public class Item {
	private String name;
	private double damage;
	private double armour;
	
	public Item(String name, double damage, double armour) {
		this.name = name;
		this.damage = damage;
		this.armour = armour;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	public double getArmour() {
		return armour;
	}
	
	public void setArmour(double armour) {
		this.armour = armour;
	}
}
