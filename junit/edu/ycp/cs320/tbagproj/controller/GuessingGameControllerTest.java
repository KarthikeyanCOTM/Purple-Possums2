package edu.ycp.cs320.tbagproj.controller;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbagproj.controller.GuessingGameController;
import edu.ycp.cs320.tbagproj.model.Actor;

public class GuessingGameControllerTest {
	private Actor model;
	private GuessingGameController controller;
	
	@Before
	public void setUp() {
		model = new Actor();
		controller = new GuessingGameController();
		
		model.setMin(1);
		model.setMax(100);
		
		controller.setModel(model);
	}
	
	@Test
	public void testNumberIsGreater() {
		int currentGuess = model.getGuess();
		controller.setNumberIsGreaterThanGuess();
		assertTrue(model.getGuess() > currentGuess);
	}
	
	@Test
	public void testNumberIsLess() {
		int currentGuess = model.getGuess();
		controller.setNumberIsLessThanGuess();
		assertTrue(model.getGuess() < currentGuess);
	}
	
	@Test
	public void testSetNumberFound() {
		int currentGuess = model.getGuess();
		controller.setNumberFound();
		assertTrue(model.getMin() == currentGuess && model.getMax() == currentGuess);
	}
	
	@Test
	public void testStartGame() {
		model.setMin(0);
		model.setMax(2);
		controller.startGame();
		assertTrue(model.getMin() == 1 && model.getMax() == 100);
	}
}
