package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;

public class Inventory {
	private ArrayList<Item> itemArrayList;
	private int gold;
	
	public  Inventory() {
		itemArrayList = new ArrayList<Item>();
		gold = 0;
	}
	
	public Inventory(ArrayList items, int gold) {
		itemArrayList = new ArrayList<Item>(items);
		this.gold = gold;
	}
	
	public int getGold() {
		return gold;
	}
	
	public void addGold(int gold) {
		this.gold += gold;
	}
	
	public Object getItem(Item item) {
		if (itemArrayList.contains(item) == true) {
			for (int i = 0; i < itemArrayList.size(); i++) {
				if (itemArrayList.get(i) == item) {
					return itemArrayList.get(i);
				}
			}
		}
		return null;
	}
	
	public void addItem(Item item) {
		itemArrayList.add(item);
	}
	
	public int getNumItems() {
		return itemArrayList.size();
	}
}
