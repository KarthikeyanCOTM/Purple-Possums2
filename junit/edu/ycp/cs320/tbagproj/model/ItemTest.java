package edu.ycp.cs320.tbagproj.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbagproj.model.Item;

public class ItemTest {
	Item model;
	
	@Before
	public void setUp() {
		model = new Item();
	}

	@Test
	public void testSetName() {
		model.setName("Sword");
		assertEquals("Sword", model.getName());
	}
	
	@Test
	public void testSetDamage() {
		model.setDamage(5);
		assertTrue(model.getDamage() == 5);
	}
	
	@Test
	public void testSetArmour() {
		model.setArmour(5);
		assertTrue(model.getArmour() == 5);
	}
}
