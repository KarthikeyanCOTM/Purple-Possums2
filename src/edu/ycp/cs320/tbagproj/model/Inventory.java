package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;

public class Inventory {
	private ArrayList<Item> itemArrayList;
	private int gold;
	private String owner;
	private int roomKey;
	
	public  Inventory() {
		itemArrayList = new ArrayList<Item>();
		gold = 0;
	}
	
	public Inventory(ArrayList items, int gold) {
		itemArrayList = new ArrayList<Item>(items);
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
}
