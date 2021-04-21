package edu.ycp.cs320.tbagproj.model;

public class Item {
	private String name;
	private double damage;
	private double armour;
	private double healing;
	private boolean isUsable;
	private int item_ID;
	private int inventory_ID;
	private int equipment_ID;
	
	public Item() {
		
	}
	
	public Item(String name, double damage, double armour, double healing, boolean isUsable) {
		this.name = name;
		this.damage = damage;
		this.armour = armour;
		this.healing = healing;
		this.isUsable = isUsable;
		this.inventory_ID = 0;
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
	
	public boolean getIsUsable() {
		return isUsable;
	}
	
	public void setIsUsable(boolean isUsable) {
		this.isUsable = isUsable;
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
	
	public int getItem_ID() {
		return item_ID;
	}
	
	public void setItem_ID(int item_ID) {
		this.item_ID = item_ID;
	}
}
