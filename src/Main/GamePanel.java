package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import GameState.GameStateManager;
import Handlers.KeyHandler;
import Handlers.MouseHandler;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements KeyListener, MouseListener, Runnable {

	// default dimensions
	public static int WIDTH = 400;
	public static int HEIGHT = 300;
	public static final int SCALE = 2;
	
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private GameStateManager gsm;
	
	/*
	 * New Game Loop attempt
	 */
	
	public GamePanel() {
		super();

		setMinimumSize(new Dimension(640, 480));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		requestFocus();
		setFocusable(true);
		
	}
	
	public void addNotify() {
		super.addNotify();
		// When the panel is called, this gets called
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			thread.start();
		}
		
	}
	
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager();
	}
	
	private long redraw() {
		
		long t = System.currentTimeMillis();
		
		update();
		
		draw();
		
		drawToScreen();
		
		
		return System.currentTimeMillis() - t;
	}
	
	public void run() {
		
		init();
		
		while(running) {
			
			long durationMs = redraw();
			
			try {
				Thread.sleep(Math.max(0, FPS - durationMs));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void update() {
		gsm.update();
		KeyHandler.update();
	}
	
	public void draw() {
		gsm.draw(g);
	}
	
	public void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, 
				WIDTH * SCALE, HEIGHT * SCALE, 
				null);
		g2.dispose();
		
	}
	
	public void mouseClicked(MouseEvent arg0) {
		KeyHandler.getKeyAction(arg0.getButton(), false);
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent arg0) {
		MouseHandler.getMouseAction(arg0.getButton(), true);
	}
	public void mouseReleased(MouseEvent arg0) {
	}
	public void keyPressed(KeyEvent arg0) {
		KeyHandler.getKeyAction(arg0.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent arg0) {
		KeyHandler.getKeyAction(arg0.getKeyCode(), false);
	}
	public void keyTyped(KeyEvent arg0) {
	}
}
