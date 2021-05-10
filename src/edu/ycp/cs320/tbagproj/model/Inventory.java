package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;

public class Inventory {
	private ArrayList<Item> itemArrayList;
	private int gold;
	private String owner;
	private int roomKey;
	private int inventory_ID;
	private int equipment_ID;
	
	public  Inventory() {
		itemArrayList = new ArrayList<Item>();
		gold = 0;
	}
	
	public Inventory(ArrayList<Item> items, int gold) {
		itemArrayList = new ArrayList<Item>();
		this.gold = gold;
	}
	
	public void clearInventory() {
		itemArrayList.clear();
		gold = 0;
	}
	
	public int getGold() {
		return gold;
	}
	
	public void addGold(int gold) {
		this.gold += gold;
	}
	
	public Item getItem(String item) {
		for (int i = 0; i < itemArrayList.size(); i++) {
			if (itemArrayList.get(i).getName().equals(item)) {
				return itemArrayList.get(i);
			}
		}
		return null;
	}
	
	public void addNewItem(String name, double damage, double armour, double healing, boolean isUsable) {
		Item temp = new Item(name, damage, armour, healing, isUsable);
		itemArrayList.add(temp);
	}
	
	public void removeItem(String name) {
		for (int i = 0; i < itemArrayList.size(); i++) {
			if (itemArrayList.get(i).getName().equals(name)) {
				itemArrayList.remove(i);
			}
		}
	}
	
	public int getNumItems() {
		return itemArrayList.size();
	}
	
	public boolean containsItem(String name) {
		for (int i = 0; i < itemArrayList.size(); i++) {
			if (itemArrayList.get(i).getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsEquippedItem(String name) {
		String equippedItem = "";
		String inputItem = "";
		for (int x = 0; x < name.length(); x++) {
			if (name.charAt(x) == ' ') {
				for (int y = x + 1; y < name.length(); y++) {
					if (name.charAt(y) == ' ') {
						y = name.length();
					}else {
						inputItem += name.charAt(y);
					}
				}
				x = name.length();
			}
		}
		for (int a = 0; a < itemArrayList.size(); a++) {
			String tempName = itemArrayList.get(a).getName();
			for (int b = 0; b < tempName.length(); b++) {
				if (tempName.charAt(b) == ' ') {
					for (int c = b + 1; c < tempName.length(); c++) {
						if (name.charAt(c) == ' ') {
							c = name.length();
						}else {
							equippedItem += name.charAt(c);
						}
					}
					b = tempName.length();
				}
			}
			if (equippedItem.equals(inputItem)) {
				return true;
			}
			if (equippedItem.equals("staff") && inputItem.equals("sword")) {
				return true;
			}
			if (inputItem.equals("staff") && equippedItem.equals("sword")) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Item> getItemList() {
		return itemArrayList;
	}
	
	public void setItems(ArrayList<Item> list) {
		itemArrayList = list;
	}
	
	public void setOwner(String o) {
		owner = o;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public int getRoomKey() {
		return roomKey;
	}
	public void setRoomKey(int roomKey) {
		this.roomKey = roomKey;
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
}
