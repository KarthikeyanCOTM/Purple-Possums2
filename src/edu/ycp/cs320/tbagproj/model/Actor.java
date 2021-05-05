package edu.ycp.cs320.tbagproj.model;


// model class for Player and NPC
public class Actor {
	private String name;
	private double health = 100;
	private double maxHealth;
	private double excessHealth;
	private double totalDamage = 1.0;
	private double defence = 0.0;
	private Inventory inventory;
	private Inventory equipment;
	private int inventory_ID = 0;
	private int equipment_ID = 0;
	private int room_ID;
	
	public Actor() {
		inventory = new Inventory();
		equipment = new Inventory();
	}
	
	public Actor(String name) {
		this.name = name;
		inventory = new Inventory();
		equipment = new Inventory();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getCurHealth() {
		return health;
	}
	
	public void setHealth(double health) {
		if (this.health + health < maxHealth) {
			this.health += health;
			excessHealth = 0;
		}else {
			this.health += health;
			excessHealth = this.health - maxHealth;
			this.health -= excessHealth;
		}
	}
	
	public void setNewHealth(double health) {
		this.health = health;
		maxHealth = health;
	}
	
	public double getTotalDamage() {
		return totalDamage;
	}
	
	public void setTotalDamage(double totalDamage) {
		this.totalDamage = totalDamage;
	}
	
	public double getDefence() {
		return defence;
	}
	
	public void setDefence(double defense) {
		this.defence = defense;
	}
	
	public void equipItem(String item) {
		Item tempItem = inventory.getItem(item);
		tempItem.setInventory_ID(0);
		tempItem.setEquipment_ID(getEquipment_ID());
		equipment.addNewItem(tempItem.getName(), tempItem.getDamage(), tempItem.getArmour(), tempItem.getHealing(), tempItem.getIsUsable());
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
		temp.setEquipment_ID(0);
		temp.setInventory_ID(getInventory_ID());
		equipment.removeItem(item);
		if (temp.getDamage() > 0) {
			totalDamage -= temp.getDamage();
		}
		if (temp.getArmour() > 0) {
			defence -= temp.getArmour();
		}
		inventory.addNewItem(temp.getName(), temp.getDamage(), temp.getArmour(), temp.getHealing(), temp.getIsUsable());
	}
	
	public Inventory getEquipment() {
		return equipment;
	}
	
	public void setEquipment(Inventory equipment) {
		this.equipment = equipment;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public void setInventory_ID(int i) {
		inventory_ID = i;
	}
	
	public int getInventory_ID() {
		return inventory_ID;
	}
	
	public void setEquipment_ID(int e) {
		equipment_ID = e;
	}
	
	public int getEquipment_ID() {
		return equipment_ID;
	}
	
	public void setRoom_ID(int r) {
		room_ID = r;
	}
	
	public int getRoom_ID() {
		return room_ID;
	}
	
	public double getMaxHealth() {
		return maxHealth;
	}
	
	public double getExcessHealth() {
		return excessHealth;
	}
}
