package com.tungsten;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

public class ScoreCommunicator {

	private int highScore;
	private static final String fileName = "highscore.bin";
	
	public ScoreCommunicator() {
		
		highScore = 0;
	}
	
	public File getFile() {
		
		return new File(fileName);
	}
	
	// reset the score based on the board
	public void setScore(int newScore) {
		highScore = newScore;
	}
	
	// getting the score for display
	public int getScore() {
		return highScore;
	}
	
	// reading the score from the .bin file
	public void loadScore() {
		
		try {
			
			InputStream iStream = new BufferedInputStream(new FileInputStream(fileName));
			
			// fail safe mechanism: make space for 2 ints
			int[] num = new int[2];
			for (int i = 0; i < 2; i++) {
				
				int readNum = iStream.read();
				if (readNum != -1)
					num[i] = readNum;
			}
			
			highScore = num[0];
			
			iStream.close();
		}
			
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// writing the score to the .bin file
	public void saveScore() {
		
		try {
			
			byte[] buffer = new byte[1];
			buffer[0] = (byte) highScore;
			
			OutputStream oStream = new BufferedOutputStream(new FileOutputStream(fileName));
			
			oStream.write(buffer);
			oStream.close();
		}
			
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
