package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;

// model class for Player and NPC
public class Actor {
	private String name;
	private double health = 100.0;
	private double totalDamage;
	private double defence;
	private Inventory inventory;
	private ArrayList<Item> equipment;
	
	public Actor() {
		
	}
	
	public Actor(String name) {
		this.name = name;
		inventory = new Inventory();
		equipment = new ArrayList<>();
		totalDamage = 1.0;
		defence = 0.0;
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
	
	public double getTotalDamage() {
		return totalDamage;
	}
	
	public double getDefence() {
		return defence;
	}
	
	public void equipItem(Item item) {
		equipment.add(item);
		if (item.getDamage() > 0) {
			totalDamage += item.getDamage();
		}
		if (item.getArmour() > 0) {
			defence += item.getArmour();
		}
	}
	
	public void unequipItem(Item item) {
		equipment.remove(item);
		if (item.getDamage() > 0) {
			totalDamage -= item.getDamage();
		}
		if (item.getArmour() > 0) {
			defence -= item.getArmour();
		}
	}
	
	public ArrayList<Item> getEquipment() {
		return equipment;
	}
}
