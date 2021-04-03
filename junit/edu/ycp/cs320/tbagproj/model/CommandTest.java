package edu.ycp.cs320.tbagproj.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbagproj.model.Command;

public class CommandTest {
	Command model;

	@Before
	public void setUp() {
		model = new Command();
	}
	
	@Test
	public void test() {
		model.setPrompt("Hello World");
		assertEquals(model.getFirst(), "Hello");
		assertEquals(model.getSecond(), "World");
	}

}
