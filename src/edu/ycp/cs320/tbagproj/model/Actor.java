package edu.ycp.cs320.tbagproj.model;


// model class for Player and NPC
public class Actor {
	private String name;
	private double health = 100.0;
	private double totalDamage;
	private double defence;
	private Inventory inventory;
	private Inventory equipment;
	
	public Actor() {
		
	}
	
	public Actor(String name) {
		this.name = name;
		inventory = new Inventory();
		equipment = new Inventory();
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
	
	public void equipItem(String item) {
		Item tempItem = inventory.getItem(item);
		equipment.addItem(tempItem);
		if (tempItem.getDamage() > 0) {
			totalDamage += tempItem.getDamage();
		}
		if (tempItem.getArmour() > 0) {
			defence += tempItem.getArmour();
		}
		inventory.removeItem(item);
	}
	
	public void unequipItem(String item) {
		Item temp = equipment.getItem(item);
		equipment.removeItem(item);
		if (temp.getDamage() > 0) {
			totalDamage -= temp.getDamage();
		}
		if (temp.getArmour() > 0) {
			defence -= temp.getArmour();
		}
		inventory.addItem(temp);
	}
	
	public Inventory getEquipment() {
		return equipment;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	
}
