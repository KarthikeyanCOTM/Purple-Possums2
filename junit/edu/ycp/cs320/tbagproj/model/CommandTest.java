package edu.ycp.cs320.tbagproj.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbagproj.model.*;

public class CommandTest {
	Command model;
	Player playerModel;
	Room roomModel;

	@Before
	public void setUp() {
		model = new Command();
		playerModel = new Player("player");
		roomModel = new Room();
		model.setCommands();
	}
	
	@Test
	public void testSetPrompt() {
		model.setPrompt("Hello World");
		assertEquals(model.getFirst(), "Hello");
		assertEquals(model.getSecond(), "World");
	}
	
	@Test
	public void testProcessPrompt() {
		model.setPrompt("new");
		assertEquals(model.processPrompt(playerModel, roomModel), 6);
	}

}
