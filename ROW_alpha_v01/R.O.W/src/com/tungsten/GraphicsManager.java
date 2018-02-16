package com.tungsten;

import javax.swing.JFrame;
import java.awt.EventQueue;

public class GraphicsManager extends JFrame {
	
	// for serialisation (versioning)
	private static final long serialVersionUID = 1L;
	
	public GraphicsManager() {
		
		initUI();
	}
	
	private void initUI() {
		
		add(new Board());
		
		setSize(768, 512);
		setResizable(false);
		
		setTitle("R.O.W");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void startGame() {
		
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				JFrame ex = new GraphicsManager();
				ex.setVisible(true);
			}
		});
	}
}