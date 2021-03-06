package edu.ycp.cs320.tbagproj.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbagproj.model.Player;
import edu.ycp.cs320.tbagproj.model.NPC;

public class PlayerNPCTest {
	private Player player;
	private NPC npc;
	
	@Before
	public void setUp() {
		player = new Player("Player");
		npc = new NPC("NPC");
	}
	
	@Test
	public void testgetName() {
		assertEquals("Player", player.getName());
	}
	
	public void testSetHealth() {
		player.setHealth(90.0);
		assertEquals(90.0, player.getCurHealth());
	}
	
	@Test
	public void testSetIsNPCAlive() {
		npc.setIsNPCAlive(false);
		assertEquals(false, npc.getIsNPCAlive());
	}
	
	@Test
	public void testGetGuess() {
		/*model.setMin(1);
		model.setMax(100);
		assertEquals(50, model.getGuess());*/
	}
	
	@Test
	public void testSetIsLessThan() {
		/*model.setMin(1);
		model.setMax(100);
		int guess = model.getGuess();
		model.setIsLessThan(guess);
		assertEquals(49, model.getMax());*/
	}
	
	public void testSetIsGreaterThan() {
		/*model.setMin(1);
		model.setMax(100);
		int guess = model.getGuess();
		model.setIsGreaterThan(guess);
		assertEquals(51, model.getMin());*/
	}
}
