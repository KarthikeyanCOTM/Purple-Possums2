package edu.ycp.cs320.tbagproj.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbagproj.model.Inventory;

public class InventoryTest {
	private Inventory model;
	
	@Before
	public void setUp() {
		model = new Inventory();
	}
	
	@Test
	public void testAddGold() {
		model.addGold(5);
		assertTrue(5 == model.getGold());
	}
	
	public void testGetItem() {
	}
}