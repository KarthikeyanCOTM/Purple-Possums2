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
		item = new Item("sword", 0, 0);
	}
	
	@Test
	public void testAddGold() {
		model.addGold(5);
		assertTrue(5 == model.getGold());
	}
	
	@Test
	public void testGetItem() {
		model.addItem(item);
		assertEquals(item, model.getItem(item));
	}
}