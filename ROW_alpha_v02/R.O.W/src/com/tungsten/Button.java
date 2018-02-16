package com.tungsten;

import java.awt.Image;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class Button {

	public enum ButtonType {
		
		CORRECT,
		WRONG,
		START,
		REPLAY;
	}
	
	// constants to represent whether the button is pressed
	private static final int UNPRESSED = 0;
	private static final int PRESSED = 1;
	private static final int HOVERED = 2;
	
	// absolute position on screen and dimension information
	private int x, y;
	public static final int SQ_BUTTON_WIDTH = 128, SQ_BUTTON_HEIGHT = 128;
	public static final int RC_BUTTON_WIDTH = 324, RC_BUTTON_HEIGHT = 114;
	
	// delay for button response 
	private static final long BUTTON_DELAY = 50; // 50 ms
	private long timeAfterPressed;
	private long timeSincePressed;
	
	// boundary coordinates for each button
	private class BoundCoordinates {
		
		public int x[];
		public int y[];
		
		// array identifier constants, for human understanding
		public static final int TOP_LEFT = 0;
		public static final int TOP_RIGHT = 1;
		public static final int BOTTOM_LEFT = 2;
		public static final int BOTTOM_RIGHT = 3;
		
		public BoundCoordinates(int absX, int absY, int width, int height) {
			
			x = new int[4];
			y = new int[4];
			
			x[TOP_LEFT] = absX;
			y[TOP_LEFT] = absY;
			
			x[TOP_RIGHT] = absX + width;
			y[TOP_RIGHT] = absY;
			
			x[BOTTOM_LEFT] = absX;
			y[BOTTOM_LEFT] = absY + height;
			
			x[BOTTOM_RIGHT] = absX + width;
			y[BOTTOM_RIGHT] = absY + height;
		}
	}
	
	private byte reference;
	private boolean pressFlag; // temporary measure to check if the player is pressing the button
	private boolean hasPressed; // if the player has pressed the button already
	private Image[] images;
	private BoundCoordinates bc;
	
	public Button(ButtonType type, int x, int y) {
		
		initButton(type, x, y);
	}
	
	private void initButton(ButtonType type, int x, int y) {
		
		this.x = x;
		this.y = y;
		
		images = new Image[3];
		reference = UNPRESSED;
		pressFlag = false;
		hasPressed = false;
		
		switch (type) {
		case CORRECT: {
			
			// the 'resources' directory is temporary
			ImageIcon ii = new ImageIcon("resources/Correct_0.png");
			images[UNPRESSED] = ii.getImage();
			
			ii = null;
			
			ii = new ImageIcon("resources/Correct_1.png");
			images[PRESSED] = ii.getImage();
			
			ii = null;
			
			ii = new ImageIcon("resources/Correct_2.png");
			images[HOVERED] = ii.getImage();
			
			bc = new BoundCoordinates(x, y, SQ_BUTTON_WIDTH, SQ_BUTTON_HEIGHT);
			break;
		}
		case WRONG: {
			
			ImageIcon ii = new ImageIcon("resources/Wrong_0.png");
			images[UNPRESSED] = ii.getImage();
			
			ii = null;
			
			ii = new ImageIcon("resources/Wrong_1.png");
			images[PRESSED] = ii.getImage();
			
			ii = null;
			
			ii = new ImageIcon("resources/Wrong_2.png");
			images[HOVERED] = ii.getImage();
			
			bc = new BoundCoordinates(x, y, SQ_BUTTON_WIDTH, SQ_BUTTON_HEIGHT);
			break;
		}
		case START: {
			
			ImageIcon ii = new ImageIcon("resources/Start_0.png");
			images[UNPRESSED] = ii.getImage();
			
			ii = null;
			
			ii = new ImageIcon("resources/Start_1.png");
			images[PRESSED] = ii.getImage();
			
			ii = null;
			
			ii = new ImageIcon("resources/Start_2.png");
			images[HOVERED] = ii.getImage();
			
			bc = new BoundCoordinates(x, y, RC_BUTTON_WIDTH, RC_BUTTON_HEIGHT);
			break;
		}
		case REPLAY: {
			
			ImageIcon ii = new ImageIcon("resources/Replay_0.png");
			images[UNPRESSED] = ii.getImage();
			
			ii = null;
			
			ii = new ImageIcon("resources/Replay_1.png");
			images[PRESSED] = ii.getImage();
			
			ii = null;
			
			ii = new ImageIcon("resources/Replay_2.png");
			images[HOVERED] = ii.getImage();
			
			bc = new BoundCoordinates(x, y, RC_BUTTON_WIDTH, RC_BUTTON_HEIGHT);
			break;
		}
		default:
			break;
		}
		
		timeAfterPressed = 0;
		timeSincePressed = 0;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	// for communication with the GameManager
	public boolean sendSignal() {
		
		timeSincePressed = System.currentTimeMillis();
		boolean canAnswer = (timeSincePressed >= timeAfterPressed + BUTTON_DELAY);
		
		if (hasPressed && canAnswer) {
			
			hasPressed = false;
			return true;
		}
		else
			return false;
	}
	
	public Image getCurrentImage() {
		return images[reference];
	}
	
	public void mousePressed(MouseEvent e) {
		
		// check if cursor is within the boundaries
		if (e.getX() >= bc.x[BoundCoordinates.TOP_LEFT] && e.getX() <= bc.x[BoundCoordinates.TOP_RIGHT]) {
			
			if (e.getY() >= bc.y[BoundCoordinates.TOP_LEFT] && e.getY() <= bc.y[BoundCoordinates.BOTTOM_LEFT]) {
				
				reference = PRESSED;
				pressFlag = true;
			} 
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		
		// check if cursor is within the boundaries
		if (e.getX() >= bc.x[BoundCoordinates.TOP_LEFT] && e.getX() <= bc.x[BoundCoordinates.TOP_RIGHT]) {
					
			if (e.getY() >= bc.y[BoundCoordinates.TOP_LEFT] && e.getY() <= bc.y[BoundCoordinates.BOTTOM_LEFT]) {
				
				if (pressFlag) {
					
					hasPressed = true;
					timeAfterPressed = System.currentTimeMillis();
				}
			} 
		}
		
		reference = UNPRESSED;
		pressFlag = false;
	}
	
	public void mouseMoved(MouseEvent e) {
		
		// check if cursor is within the boundaries
		if (e.getX() >= bc.x[BoundCoordinates.TOP_LEFT] && e.getX() <= bc.x[BoundCoordinates.TOP_RIGHT]) {
					
			if (e.getY() >= bc.y[BoundCoordinates.TOP_LEFT] && e.getY() <= bc.y[BoundCoordinates.BOTTOM_LEFT]) {
						
				reference = HOVERED;
			} 
			else
				reference = UNPRESSED;
		}
		else
			reference = UNPRESSED;
	}
}
