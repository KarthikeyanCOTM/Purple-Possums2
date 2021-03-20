package edu.ycp.cs320.tbagproj.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbagproj.model.Actor;

public class GuessingGameTest {
	private Actor model;
	
	@Before
	public void setUp() {
		model = new Actor();
	}
	
	@Test
	public void testSetMin() {
		model.setMin(1);
		assertEquals(1, model.getMin());
	}
	
	public void testSetMax() {
		model.setMax(100);
		assertEquals(100, model.getMax());
	}
	
	@Test
	public void testIsDone() {
		model.setMax(4);
		model.setMin(4);
		assertEquals(true, model.isDone());
	}
	
	@Test
	public void testGetGuess() {
		model.setMin(1);
		model.setMax(100);
		assertEquals(50, model.getGuess());
	}
	
	@Test
	public void testSetIsLessThan() {
		model.setMin(1);
		model.setMax(100);
		int guess = model.getGuess();
		model.setIsLessThan(guess);
		assertEquals(49, model.getMax());
	}
	
	public void testSetIsGreaterThan() {
		model.setMin(1);
		model.setMax(100);
		int guess = model.getGuess();
		model.setIsGreaterThan(guess);
		assertEquals(51, model.getMin());
	}
}
