package edu.ycp.cs320.tbagproj.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbagproj.model.Inventory;
import edu.ycp.cs320.tbagproj.model.Item;

public class InventoryTest {
	private Inventory model;
	private Item item;
	
	@Before
	public void setUp() {
		model = new Inventory();
		item = new Item("sword", 0.0, 0.0, 0.0, false);
	}
	
	@Test
	public void testAddGold() {
		model.addGold(5);
		assertTrue(5 == model.getGold());
	}
	
	@Test
	public void testGetItem() {
		model.addNewItem(item.getName(), item.getDamage(), item.getArmour(), item.getHealing(), item.getIsUsable());
		String temp = item.getName();
		assertEquals(item.getName(), model.getItem(temp).getName());
	}
	
	@Test
	public void testContainsItem() {
		String temp = item.getName();
		model.addNewItem(item.getName(), item.getDamage(), item.getArmour(), item.getHealing(), item.getIsUsable());
		model.addNewItem("healing potion", item.getDamage(), item.getArmour(), item.getHealing(), item.getIsUsable());
		String temp2 = "healing potion";
		assertEquals(model.containsItem(temp), true);
		assertEquals(model.containsItem(temp2), true);
	}
}