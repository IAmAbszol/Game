package Entity.InGameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.Clip;

import Entity.Animation;
import Entity.DialogueBox;
import Entity.MenuList;
import Entity.Objects;
import Entity.Player.Player;
import GameState.GameStateManager;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Main.GamePanel;
import TileMap.TileMap;
import Time.Timer;

public class Tree extends Objects {

	private BufferedImage[] tree;
	
	private String objectName;
	
	private TileMap tm;
	
	public Tree(TileMap tm, Player p, double x, double y, int id) {
		super(tm, p, x, y, id);
		
		this.tm = tm;
		this.id = id;
		
		audio = new HashMap<String, Clip>();

		width = 64;
		height = 85;
		cwidth = 20;
		cheight = 20;
		coffsetx = 0;
		coffsety = -22;

		objectName = "Tree(s)";
		objectDescription = "One of many beautiful trees.";
		object = true;
		
		interactionDistance = 5;
		
		animation = new Animation();
		tree = ContentHandler.tree[0];
		
		animation.setFrames(tree);
		animation.setDelay(100);
		
		drawMenu = false;
		currentSelection = 0;
		menuOptions = new ArrayList<String>();
		
		menuOptions.add("Cut-Down " + objectName);
		menuOptions.add("Examine " + objectName);
		
	}

	public boolean onScreen() {
		return Math.abs(p.getx() - this.x) < GamePanel.WIDTH &&
			   Math.abs(p.gety() - this.y) < GamePanel.HEIGHT;
	}

	public void update() {
		if(onScreen()) {
			setPosition(this.x, this.y);
			animation.update();
			if(Timer.getDay()) {
				bgColor = new Color(0,0,0,.8f);
			} else {
				bgColor = new Color(0,0,0, 1f);
			}
			interact();
		}
	}
	
	public void draw(Graphics2D g) {
		if(onScreen()) {
			setMapPosition();
			super.draw(g);
		}
	}
	
	public void interact() {
		if(p.mayInteract(this, interactionDistance)) {
			handleInput();
		} 
	}
	
	public void handleInput() {
		// setup menu list only if the drawmenu is not currently active else where i.e, one menu per time
		if(KeyHandler.isPressed(KeyHandler.INTERACT) && !MenuList.drawMenu()) {
			MenuList.setupMenuList(this, p, menuOptions, interactionDistance);
			KeyHandler.keyState[KeyHandler.INTERACT] = false;
			listen = true; // listening
		}
		if(listen) {
			if(MenuList.amSelected()) {
				if(MenuList.getSelection() == 0) {
					// create a function that finds the current tree being interacted with
					for(int i = 0; i < GameStateManager.grabState().getMapObjects().size(); i++) {
						if(GameStateManager.grabState().getMapObjects().get(i).isListening()) {
							GameStateManager.grabState().removeObject(
									GameStateManager.grabState().getMapObjects().get(i), 
									GameStateManager.grabState().getMapObjects().get(i).getId()
							);
						}
					}
					// adding stump
					ArrayList<String> string = new ArrayList<String>();
					string.add("You cut down a once beautiful tree.");
					DialogueBox.setupBox(string,
							30,
							ContentHandler.NPC_TEXT_SIZE, 
							null, 
							null, false);
					Stump s = new Stump(tm, p, this.getx(), this.gety(), GameStateManager.grabState().getMapObjects().size());
					GameStateManager.grabState().getMapObjects().add(s);
				} else
					examine(this);
				
				listen = false;
			}
		}
		
		/*
		if(KeyHandler.isPressed(KeyHandler.INTERACT) && !drawMenu) {
			drawMenu = true;
			KeyHandler.keyState[KeyHandler.INTERACT] = false;
		}
		if(KeyHandler.isPressed(KeyHandler.BACK) && drawMenu) {
			drawMenu = false;
		}
		if(drawMenu) {
			if(KeyHandler.isPressed(KeyHandler.DOWN_K)) {
				currentSelection++;
				if(currentSelection > menuOptions.size() - 1) {
					currentSelection = 0;
				}
			}
			if(KeyHandler.isPressed(KeyHandler.UP_K)) {
				currentSelection--;
				if(currentSelection < 0) {
					currentSelection = menuOptions.size() - 1;
				}
			}
			if(KeyHandler.isPressed(KeyHandler.INTERACT)) {
				if(currentSelection == 0) {
					// create a function that finds the current tree being interacted with
					for(int i = 0; i < GameStateManager.grabState().getMapObjects().size(); i++) {
						if(GameStateManager.grabState().getMapObjects().get(i).drawingMenu()) {
							GameStateManager.grabState().removeObject(
									GameStateManager.grabState().getMapObjects().get(i), 
									GameStateManager.grabState().getMapObjects().get(i).getId()
							);
						}
					}
					// adding stump
					ArrayList<String> string = new ArrayList<String>();
					string.add("You cut down a once beautiful tree.");
					DialogueBox.setupBox(string,
							30,
							ContentHandler.NPC_TEXT_SIZE, 
							null, 
							null, false);
					Stump s = new Stump(tm, p, this.getx(), this.gety(), GameStateManager.grabState().getMapObjects().size());
					GameStateManager.grabState().getMapObjects().add(s);
				} else
					examine(this);
				KeyHandler.keyState[KeyHandler.INTERACT] = false;
			}
		}
		*/
	}
	
	public void setObjectName(String n) { objectName = n; }
	public String getObjectName() { return objectName; } // Function will be drawn to HUD of map position

	@Override
	public String getObjectDescription() {
		return objectDescription;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public boolean isListening() {
		return listen;
	}
	
}