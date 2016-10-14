package Entity.InGameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.Clip;

import Entity.Animation;
import Entity.MenuList;
import Entity.Objects;
import Entity.Player.Player;
import GameState.GameStateManager;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Main.GamePanel;
import TileMap.TileMap;
import Time.Timer;

public class Stump extends Objects {

	private BufferedImage[] stump;
	
	private long respawn;
	private long respawnTime;
	private TileMap tm;
	
	public Stump(TileMap tm, Player p, double x, double y, int id) {
		super(tm, p, x, y, id);
		this.id = id;
		this.tm = tm;
		respawn = 0;
		respawnTime = 800;
		
		audio = new HashMap<String, Clip>();

		width = 64;
		height = 85;
		cwidth = 20;
		cheight = 15;
		coffsetx = 0;
		coffsety = -25;

		objectName = "Tree Stump";
		objectDescription = "Once existed a beautiful tree.";
		object = true;
		
		interactionDistance = 5;
		
		animation = new Animation();
		stump = ContentHandler.stump[0];
		
		animation.setFrames(stump);
		animation.setDelay(100);
		
		drawMenu = false;
		currentSelection = 0;
		menuOptions = new ArrayList<String>();
		
		menuOptions.add("Examine " + objectName);
	}

	public boolean onScreen() {
		return Math.abs(p.getx() - this.x) < GamePanel.WIDTH &&
			   Math.abs(p.gety() - this.y) < GamePanel.HEIGHT;
	}

	public void update() {
		respawn++;
		if(respawn > respawnTime) {
			Tree s = new Tree(tm, p, this.getx(), this.gety(), this.getId());
			GameStateManager.grabState().getMapObjects().remove(this);
			GameStateManager.grabState().getMapObjects().add(s);
		}
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
					examine(this);
				}
				listen = false;
			}
		}
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
