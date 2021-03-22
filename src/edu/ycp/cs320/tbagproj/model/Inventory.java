package edu.ycp.cs320.tbagproj.model;

import java.util.ArrayList;

public class Inventory {
	private ArrayList<Object> itemArrayList;
	private int gold;
	
	public void newInventory() {
		itemArrayList = new ArrayList<Object>();
		gold = 0;
	}
	
	public int getGold() {
		return gold;
	}
	
	public void setGold(int gold) {
		this.gold += gold;
	}
	
	public Object getItem(Object item) {
		for (int i = 0; i < itemArrayList.size(); i++) {
			if (itemArrayList.get(i) == item) {
				return itemArrayList.get(i);
			}
		}
		return null;
	}
	
	public void addItem(Object item) {
		itemArrayList.add(item);
	}
}
