package com.tungsten;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
	
	// for serialisation (versioning)
	private static final long serialVersionUID = 1L;
	
	private static final int CENTER_X = 384;
	private static final int CENTER_Y = 256;
	
	private EquationManager em;
	private ScoreCommunicator sc;
	private TAdapter tAdapter;
	
	private int points;
	private int lives;
	private boolean choice;
	private long startTime;
	private long timeLeft;
	
	private Timer timer;
	private final int DELAY = 10;
	
	private Button correct;
	private Button wrong;
	private Button start;
	private Button replay;
	
	private GameText titleText;
	private GameText highScoreText1; // for start page
	private GameText highScoreText2; // for game over page
	private GameText equation;
	private GameText timeText;
	private GameText pointsText;
	private GameText livesText;
	private GameText gameOverText;
	private GameText finalScoreText;
	
	private boolean hasSignal;
	
	private enum GameStates {
		
		GAME_START,
		GAME_PLAYING,
		GAME_END;
	}
	
	private GameStates gameState;
	
	public Board() {
		
		initBoard();
	}
	
	private void initBoard() {
		
		tAdapter = new TAdapter();
		
		addMouseListener(tAdapter);
		addMouseMotionListener(tAdapter);
		setFocusable(true);
		setBackground(Color.WHITE);
		
		loadResources();
		
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	private void loadResources() {
		
		sc = new ScoreCommunicator();
		if (sc.getFile().exists())
			sc.loadScore();
		
		em = new EquationManager();
		
		correct = new Button(Button.ButtonType.CORRECT, CENTER_X - (Button.SQ_BUTTON_WIDTH / 2) - 186, CENTER_Y);
		wrong = new Button(Button.ButtonType.WRONG, CENTER_X - (Button.SQ_BUTTON_WIDTH / 2) + 186, CENTER_Y);
		start = new Button(Button.ButtonType.START, CENTER_X - (Button.RC_BUTTON_WIDTH / 2), CENTER_Y + 32);
		replay = new Button(Button.ButtonType.REPLAY, CENTER_X - (Button.RC_BUTTON_WIDTH / 2), CENTER_Y + 24);
		
		
		titleText = new GameText("R.O.W", CENTER_X, 160, 72, true);
		highScoreText1 = new GameText("Your best score: " + sc.getScore(), CENTER_X, 256 , 48, true);
		highScoreText2 = new GameText("Your best score: " + sc.getScore(), CENTER_X, 256 , 36, true);
		equation = new GameText("xxx", CENTER_X, CENTER_Y - 48, 48, true);
		timeText = new GameText("Time Left: ", 616, 72, 24, true);
		pointsText = new GameText("Points: ", 104, 72, 24, true);
		livesText = new GameText("Lives: ", 360, 72, 24, true);
		gameOverText = new GameText("Game Over", 384, 120, 48, true);
		finalScoreText = new GameText("Final Score: ", 384, 192, 36, true);
		
		gameState = GameStates.GAME_START;
	}
	
	private void startGame() {
		
		hasSignal = false;
		choice = false;
		points = 0;
		lives = 3;
		startTime = System.currentTimeMillis();
		timeLeft = (60000 - System.currentTimeMillis() + startTime) / 1000;
		gameState = GameStates.GAME_PLAYING;
	}
	
	private void refresh() {
		
		timeLeft = (60000 - System.currentTimeMillis() + startTime) / 1000;
		
		if (gameState == GameStates.GAME_PLAYING && (timeLeft <= 0 || lives == 0))
			gameState = GameStates.GAME_END;
		
		if (gameState == GameStates.GAME_PLAYING && hasSignal) {
			
			compare();
			reset();
			hasSignal = false;
		}
		
		if (gameState == GameStates.GAME_END) {
			if (points > sc.getScore())
				updateHighScore();
		}
	}
	
	private void compare() {
		
		if (choice == em.getRightWrong())
			awardPoints();
		else
			deductLives();
	}
	
	private void awardPoints() {
		++points;
	}
	
	private void deductLives() {
		--lives;
	}
	
	private void reset() {
		
		em.refresh();
	}
	
	// update equation
	public void updateEqText(String eq) {
		
		equation.updateString(eq);
	}
	
	// update time remaining
	public void updateTimeText(long timeLeft) {
		
		int minutes = (int) timeLeft / 60;
		int seconds = (int) timeLeft % 60;
		String time;
		if (seconds < 10)
			time = new String("Time Left: " + minutes + " : " + "0" + seconds);
		else
			time = new String("Time Left: " + minutes + " : " + seconds);
		timeText.updateString(time);
	}
	
	// update points
	public void updatePointsText(int points) {
		
		if (gameState == GameStates.GAME_PLAYING)
			pointsText.updateString("Points: " + points);
		else if (gameState == GameStates.GAME_END)
			finalScoreText.updateString("Final Score: " + points);
	}
	
	public void updateLivesText(int lives) {
		
		livesText.updateString("Lives: " + lives);
	}
	
	public void updateHighScore() {
		
		sc.setScore(points);
		sc.saveScore();
		highScoreText2.updateString("Your best score: " + sc.getScore());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		draw(g);
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void draw(Graphics g) {
		
		// refresh the game before drawing
		refresh();
		
		Graphics2D g2d = (Graphics2D) g;
		
		switch (gameState) {
		case GAME_START:
		{
			g2d.drawImage(start.getCurrentImage(), start.getX(), start.getY(), this);
			
			titleText.paint(g2d);
			highScoreText1.paint(g2d);
			
			break;
		}
		case GAME_PLAYING: 
		{
			g2d.drawImage(correct.getCurrentImage(), correct.getX(), correct.getY(), this);
			g2d.drawImage(wrong.getCurrentImage(), wrong.getX(), wrong.getY(), this);
		
			updateEqText(em.getEquation());
			updateTimeText(timeLeft);
			updatePointsText(points);
			updateLivesText(lives);
		
			equation.paint(g2d);
			timeText.paint(g2d);
			pointsText.paint(g2d);
			livesText.paint(g2d);
			break;
		}
		case GAME_END: 
		{
			g2d.drawImage(replay.getCurrentImage(), replay.getX(), replay.getY(), this);
			
			updatePointsText(points);
			
			gameOverText.paint(g2d);
			finalScoreText.paint(g2d);
			highScoreText2.paint(g2d);
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		switch (gameState) {
		case GAME_START: 
		{
			if (start.sendSignal())
				startGame();
			break;
		}
		case GAME_PLAYING:
		{
			boolean signal_1 = correct.sendSignal();
			boolean signal_2 = wrong.sendSignal();
			
			if (signal_1 == true || signal_2 == true) {
				
				if (signal_1 == true) // send 'correct' signal
					choice = true;
				else if (signal_2 == true) // send 'wrong' signal
					choice = false;
				hasSignal = true;
			}
			break;
		}
		case GAME_END:
		{
			if (replay.sendSignal())
				startGame();
			break;
		}
		default:
			break;
		}
		
		repaint();
	}
	
	private class TAdapter extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent e) {
			
			switch (gameState) {
			case GAME_START:
			{
				start.mousePressed(e);
				break;
			}
			case GAME_PLAYING:
			{
				correct.mousePressed(e);
				wrong.mousePressed(e);
				break;
			}
			case GAME_END:
			{
				replay.mousePressed(e);
				break;
			}
			default:
				break;
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			
			switch (gameState) {
			case GAME_START:
			{
				start.mouseReleased(e);
				break;
			}
			case GAME_PLAYING:
			{
				correct.mouseReleased(e);
				wrong.mouseReleased(e);
				break;
			}
			case GAME_END:
			{
				replay.mouseReleased(e);
				break;
			}
			default:
				break;
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			
			switch (gameState) {
			case GAME_START:
			{
				start.mouseMoved(e);
				break;
			}
			case GAME_PLAYING:
			{
				correct.mouseMoved(e);
				wrong.mouseMoved(e);
				break;
			}
			case GAME_END:
			{
				replay.mouseMoved(e);
				break;
			}
			default:
				break;
			}
		}
	}
}


