package edu.ycp.cs320.tbagproj.controller;

import edu.ycp.cs320.tbagproj.model.Actor;

/**
 * Controller for the guessing game.
 */
public class GuessingGameController {
	private Actor model;

	/**
	 * Set the model.
	 * 
	 * @param model the model to set
	 */
	public void setModel(Actor model) {
		this.model = model;
	}

	/**
	 * Start a new guessing game by setting the minimum to 1 and the maximum to 100.
	 */
	public void startGame() {
		//model.setMin(1);
		//model.setMax(100);
	}

	/**
	 * Called to indicate that the current guess is correct.
	 * Set the min and max to the current guess.
	 */
	public void setNumberFound() {
		/*int guess = model.getGuess();
		model.setMin(guess);
		model.setMax(guess);*/
	}

	/**
	 * Called to indicate that the user is thinking of a number that
	 * is less than the current guess.
	 */
	public void setNumberIsLessThanGuess() {
		//model.setIsLessThan(model.getGuess());
	}

	/**
	 * Called to indicate that the user is thinking of a number that
	 * is greater than the current guess.
	 */
	public void setNumberIsGreaterThanGuess() {
		//model.setIsGreaterThan(model.getGuess());
	}
}
