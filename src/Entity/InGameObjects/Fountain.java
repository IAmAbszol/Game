package Entity.InGameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.Clip;

import Audio.AudioPlayer;
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

public class Fountain extends Objects {
	
	private BufferedImage[] fountain;
	
	private String objectName;
	
	public Fountain(TileMap tm, Player p, double x, double y, int id) {
		super(tm, p, x, y, id);

		this.id = id;
		
		audio = new HashMap<String, Clip>();
		audioDistance = 125;
		interactionDistance = 5;
		
		// change these depending on what size the collision is needed. For instance, x will remain x but then cheight maybe less?
		width = 100;
		height = 100;
		cwidth = 80;
		cheight = 80;
		coffsetx = 0;
		coffsety = 0;

		objectName = "Fountain";
		objectDescription = "I can probably drink water from this.";
		object = true;
		
		animation = new Animation();
		fountain = ContentHandler.fountain[0];
		
		animation.setFrames(fountain);
		animation.setDelay(100);
		
		drawMenu = false;
		currentSelection = 0;
		menuOptions = new ArrayList<String>();
		
		menuOptions.add("Drink From " + objectName);
		menuOptions.add("Examine " + objectName);
		
		AudioPlayer.load(audio, "/SFX/WaterNoise.wav", "waternoise", 3);
		AudioPlayer.loop(audio, "waternoise");
		AudioPlayer.mute(audio, "waternoise", true);

	}
	
	public boolean onScreen() {
		return Math.abs(p.getx() - this.x) < GamePanel.WIDTH &&
			   Math.abs(p.gety() - this.y) < GamePanel.HEIGHT;
	}

	public void update() {
		if(onScreen()) {
			AudioPlayer.adjustDistanceVolume(audio, "waternoise", audioDistance, getActualDistance());
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
					if(Player.health < Player.maxHealth) {
						Player.health += 1;
					}
				} else
					examine(this);
				
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

