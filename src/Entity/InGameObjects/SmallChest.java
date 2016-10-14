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
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Main.GamePanel;
import TileMap.TileMap;
import Time.Timer;

public class SmallChest extends Objects {

	private BufferedImage[] smallChest;
	
	private String objectName;
	
	public SmallChest(TileMap tm, Player p, double x, double y, int id) {
		super(tm, p, x, y, id);
		
		this.id = id;
		
		audio = new HashMap<String, Clip>();
		
		width = 32;
		height = 48;
		cwidth = 28;
		cheight = 28;
		coffsetx = 0;
		coffsety = -10;

		objectName = "Small Chest";
		objectDescription = "WARNING! May contain loot.";
		object = true;
		
		interactionDistance = 5;
		
		animation = new Animation();
		
		smallChest = new BufferedImage[1];
		smallChest[0] = ContentHandler.smallChest[0][0];
		
		animation.setFrames(smallChest);
		animation.setDelay(100);
		
		drawMenu = false;
		currentSelection = 0;
		menuOptions = new ArrayList<String>();
		
		menuOptions.add("Open " + objectName);
		menuOptions.add("Examine " + objectName);
		
	}
	
	public boolean onScreen() {
		return Math.abs(p.getx() - this.x) < GamePanel.WIDTH &&
			   Math.abs(p.gety() - this.y) < GamePanel.HEIGHT;
	}
	
	public void update() {
		if(onScreen()) {
			setPosition(this.x, this.y);
			handleInput();
			animation.update();
			if(Timer.getDay()) {
				bgColor = new Color(0,0,0,.8f);
			} else {
				bgColor = new Color(0,0,0, 1f);
			}
			interact();
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
					// open
				} else
					examine(this);
				
				listen = false;
			}
		}
	}
	
	public void draw(Graphics2D g) {
		if(onScreen()) {
			setMapPosition();
			super.draw(g);
		}
	}

	// some stuff
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