package edu.ycp.cs320.tbagproj.model;

public class Item {
	private String name;
	private double damage;
	private double armour;
	private double healing;
	private boolean isUsable;
	private int inventory_ID;
	private int equipment_ID;
	private int item_ID;
	private boolean hasMoved;
	
	public Item() {
		inventory_ID = 0;
		equipment_ID = 0;
		hasMoved = false;
	}
	
	public Item(String name, double damage, double armour, double healing, boolean isUsable) {
		this.name = name;
		this.damage = damage;
		this.armour = armour;
		this.healing = healing;
		this.isUsable = isUsable;
		inventory_ID = 0;
		equipment_ID = 0;
		hasMoved = false;
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
		if (inventory_ID == 0 && equipment_ID == 0) {
			inventory_ID = i;
		}else {
			inventory_ID = i;
			hasMoved = true;
		}
	}
	
	public int getInventory_ID() {
		return inventory_ID;
	}
	
	public void setEquipment_ID(int e) {
		if (inventory_ID == 0 && equipment_ID == 0) {
			equipment_ID = e;
		}else {
			equipment_ID = e;
			hasMoved = true;
		}
	}
	
	public int getEquipment_ID() {
		return equipment_ID;
	}
	
	public boolean getHasMoved() {
		return hasMoved;
	}
	
	public void setItem_ID(int i) {
		item_ID = i;
	}
	
	public int getItem_ID() {
		return item_ID;
	}
}
