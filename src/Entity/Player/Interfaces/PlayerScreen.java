package Entity.Player.Interfaces;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Entity.Screen;
import Entity.Player.Player;
import Handlers.KeyHandler;
import Main.GamePanel;

public class PlayerScreen {
	
	private Player p;
	private QUEST_SCREEN QS;
	private INVENTORY_SCREEN IS;
	private PLAYER_SCREEN PS;
	private MAP_SCREEN MS;
	
	private boolean doPause;
	private boolean left;
	private boolean right;
	private boolean down;
	private boolean up;
	private boolean interact;
	private boolean enter;
	
	private boolean animated;
	
	private long timer;
	private long wait = 10;
	
	private boolean rotateUp;
	private boolean rotateDown;
	
	private boolean rotatingLeft;
	private boolean rotatingRight;
	private double moveSpeed;
	private int position = 1;
	
	private BufferedImage[] screens; // Screens to swap through (Refer to notes)
	private int pos;
	
	private int bufferEdges = 20; // Allows 20 on left, top, right, and bottom of the interface of the image
	
	private ArrayList<Screen> Screen;
	
	public PlayerScreen(Player p) {
		this.p = p;
		animated = false;
		doPause = false;
		timer = wait;
		pos = 0;
		rotatingLeft = false;
		rotatingRight = false;
		rotateUp = false;
		moveSpeed = 3;
		
		Screen = new ArrayList<Screen>();
		
	}
	
	public void update() {
		
		handleInput();
		interactableScreen();
		
	}
	
	public void draw(Graphics2D g) {
		if(rotateUp) {
			moveSpeed = 100;
			goUp(g);
		}
		if(rotateDown) {
			moveSpeed = 100;
			goDown(g);
		}
		if((rotatingLeft || rotatingRight) && !rotateUp && !rotateDown) {
			moveSpeed = 5;
			rotateLeft(g);
			rotateRight(g);
		}
		if(!rotatingLeft && !rotatingRight && !rotateUp && !rotateDown) {
			g.drawImage(Screen.get(pos).getScreenImage(), 
					bufferEdges,
					bufferEdges,
					Screen.get(pos).getWidth(), 
					Screen.get(pos).getHeight(), 
					null
			);
			/*
			g.setColor(Screen.get(pos).getColor());
			g.fillRect(bufferEdges, 
					bufferEdges,
					Screen.get(pos).getWidth(), 
					Screen.get(pos).getHeight());
			 */
		}
	}
	
	// object rotation between screens
	public void rotateLeft(Graphics2D g) {
		// Rotates object leftward <-- Thus taking the one after into account
		if(rotatingLeft) {
			double movement = position * moveSpeed;
			int current = pos;
			int after;
			if(current >= Screen.size() - 1) {
				after = 0; 
			} else
				after = current + 1;
			
			// check
			if(Screen.get(current).getX() <= (-GamePanel.WIDTH) + bufferEdges * 2 /* something here */) {
				/*
				 * May have to do readjustment of the new screen here
				 */
				rotatingLeft = false;
				pos++;
				position = 0;
				if(pos > Screen.size() - 1) pos = 0;
				if(pos < 0) pos = Screen.size() - 1;
				return;
			}
			
			// drawing current
			g.drawImage(Screen.get(pos).getScreenImage(), 
					Screen.get(current).getX(), 
					Screen.get(current).getY(), 
					Screen.get(current).getWidth(), 
					Screen.get(current).getHeight(), 
					null
			);
			/*
			g.setColor(Screen.get(pos).getColor());
			g.fillRect(Screen.get(current).getX(), 
					Screen.get(current).getY(), 
					Screen.get(current).getWidth(), 
					Screen.get(current).getHeight());
			*/
			// drawing new
			g.drawImage(Screen.get(after).getScreenImage(), 
					Screen.get(after).getX(), 
					Screen.get(after).getY(), 
					Screen.get(after).getWidth(), 
					Screen.get(after).getHeight(), 
					null
			);
			/*
			g.setColor(Screen.get(after).getColor());
			g.fillRect(Screen.get(after).getX(), 
					Screen.get(after).getY(), 
					Screen.get(after).getWidth(), 
					Screen.get(after).getHeight());
			 */
			// adjustment
			position++;
			Screen.get(current).setX(Screen.get(current).getX() - (int)movement);
			Screen.get(after).setX(Screen.get(after).getX() - (int)movement);
		
		}
	}
	
	public void rotateRight(Graphics2D g) {
		// Rotates object leftward <-- Thus taking the one after into account
				if(rotatingRight) {
					double movement = position * moveSpeed;
					int current = pos;
					int previous;
					if(current <= 0) {
						previous = Screen.size() - 1; 
					} else
						previous = current - 1;
					
					// check
					if(Screen.get(current).getX() >= GamePanel.WIDTH - bufferEdges) {
						/*
						 * May have to do readjustment of the new screen here
						 */
						rotatingRight = false;
						pos--;
						position = 0;
						if(pos > Screen.size() - 1) pos = 0;
						if(pos < 0) pos = Screen.size() - 1;
						return;
					}
					
					// drawing current
					g.drawImage(Screen.get(pos).getScreenImage(), 
							Screen.get(current).getX(), 
							Screen.get(current).getY(), 
							Screen.get(current).getWidth(), 
							Screen.get(current).getHeight(), 
							null
					);
					/*
					g.setColor(Screen.get(pos).getColor());
					g.fillRect(Screen.get(current).getX(), 
							Screen.get(current).getY(), 
							Screen.get(current).getWidth(), 
							Screen.get(current).getHeight());
					*/
					// drawing new
					g.drawImage(Screen.get(previous).getScreenImage(), 
							Screen.get(previous).getX(), 
							Screen.get(previous).getY(), 
							Screen.get(previous).getWidth(), 
							Screen.get(previous).getHeight(), 
							null
					);
					/*
					g.setColor(Screen.get(previous).getColor());
					g.fillRect(Screen.get(previous).getX(), 
							Screen.get(previous).getY(), 
							Screen.get(previous).getWidth(), 
							Screen.get(previous).getHeight());
					*/
					// adjustment
					position++;
					Screen.get(current).setX(Screen.get(current).getX() + (int)movement);
					Screen.get(previous).setX(Screen.get(previous).getX() + (int)movement);
				
				}
	}
	
	// up or down animation
	public void goUp(Graphics2D g) {
		if(rotateUp) {
			if(!animated) {
				Screen.get(pos).setY(GamePanel.HEIGHT);
				animated = true;
			}
			if(Screen.get(pos).getY() <= bufferEdges) {
				position = 0;
				rotateUp = false;
				Screen.get(pos).setY(bufferEdges);
				return;
			}
			double movement = position * moveSpeed;
			g.drawImage(Screen.get(pos).getScreenImage(), 
					bufferEdges,
					Screen.get(pos).getY(), 
					Screen.get(pos).getWidth(), 
					Screen.get(pos).getHeight(), 
					null
			);
			/*
			g.setColor(Screen.get(pos).getColor());
			g.fillRect(bufferEdges, 
					Screen.get(pos).getY(), 
					Screen.get(pos).getWidth(),
					Screen.get(pos).getHeight());
			*/
			Screen.get(pos).setY(GamePanel.HEIGHT - (int)movement);
			position++;
		}
	}
	
	public void goDown(Graphics2D g) {
		if(rotateDown) {
			if(animated) {
				Screen.get(pos).setY(bufferEdges);
				animated = false;
			}
			if(Screen.get(pos).getY() >= GamePanel.HEIGHT) {
				position = 0;
				rotateUp = false;
				rotatingLeft = false;
				rotatingRight = false;
				Screen.get(pos).setY(GamePanel.HEIGHT);animated = false;
				doPause = false;
				QS = null;
				IS = null;
				PS = null;
				MS = null;
				Screen.removeAll(Screen); // hopefully clears all of them
				return;
			}
			double movement = position * moveSpeed;
			g.drawImage(Screen.get(pos).getScreenImage(), 
					bufferEdges,
					Screen.get(pos).getY(), 
					Screen.get(pos).getWidth(), 
					Screen.get(pos).getHeight(), 
					null
			);
			/*
			g.setColor(Screen.get(pos).getColor());
			g.fillRect(bufferEdges, 
					Screen.get(pos).getY(), 
					Screen.get(pos).getWidth(),
					Screen.get(pos).getHeight());
			*/
			Screen.get(pos).setY(bufferEdges + (int)movement);
			position++;
		}
	}
	
	// interaction between the screen
	public void doScreen() {
		Screen.get(pos).controller();// Do the screen manipulation
	}
	
	public boolean getPause() {
		return doPause;
	}
	
	public void interactableScreen() {
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setScreen(boolean b) { enter = b; }
	public void setInteract(boolean b) { interact = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	
	public void handleInput() {
		if(timer >= wait) {
			if(enter && !left && !right && !rotatingRight && !rotatingLeft 
					&& !rotateUp) {
				if(doPause) {
					rotateDown = true;
				} else {
					rotateDown = false;
					rotateUp = true;
					doPause = true;
					QS = new QUEST_SCREEN(p, bufferEdges);
					IS = new INVENTORY_SCREEN(p, bufferEdges);
					PS = new PLAYER_SCREEN(p, bufferEdges);
					MS = new MAP_SCREEN(p, bufferEdges);
					Screen.add(IS);
					Screen.add(QS);
					Screen.add(PS);
					Screen.add(MS);
					
					for(int i = 0; i < Screen.size(); i++) {
						Screen.get(i).setX(bufferEdges);
						Screen.get(i).setY(bufferEdges);
						Screen.get(i).setWidth(GamePanel.WIDTH - (bufferEdges*2));
						Screen.get(i).setHeight(GamePanel.HEIGHT - (bufferEdges*2));
					}
					
				}
				timer = 0;
			}
			if(interact && doPause) {
				timer = 0;
				doScreen(); // Working on it
			}
			if(left && !rotatingRight && !rotatingLeft 
					&& !rotateDown && !rotateUp && !enter && doPause) {
				timer = 0;
				rotatingLeft = true;
				int after = pos+1;
				if(after >= Screen.size()) after = 0;
				Screen.get(after).setX(GamePanel.WIDTH + bufferEdges);
			} else
			if(right && !rotatingRight && !rotatingLeft 
					&& !rotateDown && !rotateUp && !enter && doPause) {
				timer = 0;
				rotatingRight = true;
				int previous = pos-1;
				if(previous < 0) previous = Screen.size() - 1;
				Screen.get(previous).setX(-GamePanel.WIDTH + bufferEdges);
			}
			
		}
		timer++;
	}
	
}
