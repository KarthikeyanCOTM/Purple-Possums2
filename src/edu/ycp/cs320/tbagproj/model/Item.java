package edu.ycp.cs320.tbagproj.model;

public class Item {
	private String name;
	private double damage;
	private double armour;
	private double healing;
	
	public Item() {
		
	}
	
	public Item(String name, double damage, double armour, double healing) {
		this.name = name;
		this.damage = damage;
		this.armour = armour;
		this.healing = healing;
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
	
	public double getHealing() {
		return healing;
	}
	
	public void setHealing(double healing) {
		this.healing = healing;
	}
}
