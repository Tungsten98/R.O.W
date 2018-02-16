package com.tungsten;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

public class GameText {

	private String str;
	private Font font;
	private boolean centralised;
	
	private int x, y;
	
	public GameText(String str, int posX, int posY, int size, boolean centralised) {
		
		this.str = str;
		font = new Font("Sans-serif", Font.PLAIN, size);
		
		x = posX;
		y = posY;
		this.centralised = centralised;
	}
	
	public void updateString(String str) {
		this.str = str;
	}
	
	public void paint(Graphics2D g) {
				
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(font);
		
		if (centralised) {
			
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(str, g);
			g.drawString(str, x - (int) bounds.getCenterX(), y + (int) bounds.getCenterY());
		}
		else
			g.drawString(str, x, y);
	}
}
