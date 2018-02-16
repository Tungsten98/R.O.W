package com.tungsten;

import java.util.Random;

public class EquationManager {

	// random number generator
	private Random rng;
	
	// equation components
	private int first;
	private int second;
	private int correctAns;
	private boolean plusMinus;
	private String equation;
	private boolean rightWrong;
	
	// displayed components
	private int displayedAns;
	private boolean displayRW; // to show the correct or wrong answer to the player
	
	// constructor
	public EquationManager() {
		
		rng = new Random();
		
		first = 0;
		second = 0;
		correctAns = 0;
		displayedAns = 0;
		plusMinus = false;
		displayRW = true;
		rightWrong = true;
		
		generate();
	}
	
	public void refresh() {
		
		generate();
	}
	
	private void generate() {
		
		genTwoNums();
		decideOperation();
		calculate();
		genAnswer();
		formEquation();
	}
	
	private void genTwoNums() {

		first = rng.nextInt(1001); // 0 to 1000
		
		// determine whether the first value is a negative, with 1/3 chance of being negative
		int firstNeg = rng.nextInt(3);
		if (firstNeg == 2)
			first = -first;

		second = rng.nextInt(1001);
	}
	
	private void decideOperation() {
		
		plusMinus = rng.nextBoolean();
	}
	
	private void calculate() {
		
		if (plusMinus)
			correctAns = first + second;
		else
			correctAns = first - second;
	}
	
	private void genAnswer() {
		
		// 1/3 chance of displaying wrong answer
		int displayKey = rng.nextInt(3);
		if (displayKey == 0) {
			
			displayRW = false;
			rightWrong = false;
		}
		else {
			
			displayRW = true;
			rightWrong = true;
		}
		
		if (!displayRW) {
			// display a random wrong answer depending on the number of places of the correct answer
			int difference = 0;
			
			if (correctAns >= 100) {
				int numToChange = rng.nextInt(4) + 1;
				int placeToChange = rng.nextInt(3);
				
				switch (placeToChange) {
				case 0: {
					difference = numToChange;
					break;
				}
					
				case 1: {
					difference = numToChange * 10;
					break;
				}
				
				default: {
					difference = numToChange * 100;
					break;
				}
				}
			}
			else if (correctAns < 100 && correctAns >= 10) {
				int numToChange = rng.nextInt(4) + 1;
				int placeToChange = rng.nextInt(2);
				
				switch (placeToChange) {
				case 0: {
					difference = numToChange;
					break;
				}
				default: {
					difference = numToChange * 10;
					break;
				}
				}
			}
			else 
				difference = rng.nextInt(4) + 1; // 1 to 4
				
			boolean neg = rng.nextBoolean();
			if (neg) // 1/2 chance for + or - from actual answer
				difference = -difference;
			
			displayedAns = correctAns + difference;
			
			// check answer bounds
			if (displayedAns < -2000)
				displayedAns = -2000;
			else if (displayedAns > 2000)
				displayedAns = 2000;
		}
		else
			displayedAns = correctAns;
		
	}
	
	private void formEquation() {
		
		if (plusMinus)
			equation = new String(first + " + " + second + " = " + displayedAns);
		else
			equation = new String(first + " - " + second + " = " + displayedAns);
	}
	
	public String getEquation() {
		return equation;
	}
	
	public boolean getRightWrong() {
		return rightWrong;
	}
}
