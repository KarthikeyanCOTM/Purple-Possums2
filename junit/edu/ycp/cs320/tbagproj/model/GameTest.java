package edu.ycp.cs320.tbagproj.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbagproj.model.*;

public class GameTest {
	Game model;

	@Before
	public void setUp() {
		model = new Game();
	}
	
	@Test
	public void testRunGame() {
		String temp = model.runGame("load");
		assertTrue(temp == "Load Successful");
	}

}
