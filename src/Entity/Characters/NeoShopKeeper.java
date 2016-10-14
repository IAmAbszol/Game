package Entity.Characters;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.Clip;

import Entity.Animation;
import Entity.AnimationStates;
import Entity.DialogueBox;
import Entity.NPC;
import Entity.Player.Player;
import Entity.Player.Quest.SpiderSlayer;
import GameState.GameStateManager;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Main.GamePanel;
import TileMap.TileMap;

public class NeoShopKeeper extends NPC implements Serializable {
	
	public static int spiderCount = 0;
	
	public String outputText = "";
	
	// animation
	private static final int IDLE = 0;
	private static final int WALK = 1;
	
	// the quest
	private SpiderSlayer ss;
	private boolean gaveQuest;
	
	private boolean boot = true;

	public NeoShopKeeper(TileMap tm, Player p, int id) {
		super(tm, p, id);
		
		audio = new HashMap<String, Clip>();
		
		this.p = p;
		this.id = id;
		
		npcName = "Shop Keeper";
		friendly = true;
		npcDescription = "A businessman by trade here in Neo.";
		
		width = 32;
		height = 32;
		cwidth = 22;
		cheight = 22;
		coffsetx = 0;
		coffsety = 0;

		facingRight = false;
		facingLeft = true;
		facingUp = false;
		facingDown = false;
		object = false;
		
		sprites = ContentHandler.questGiverWoman;
		
		walkDelay = 200;
		
		downAnimation = new Animation();
		downAnimation.setFrames(sprites[0]);
		downAnimation.setDelay(walkDelay);
		
		rightAnimation = new Animation();
		rightAnimation.setFrames(sprites[3]);
		rightAnimation.setDelay(walkDelay);
		
		leftAnimation = new Animation();
		leftAnimation.setFrames(sprites[2]);
		leftAnimation.setDelay(walkDelay);
		
		upAnimation = new Animation();
		upAnimation.setFrames(sprites[1]);
		upAnimation.setDelay(walkDelay);
		
		speed = .1;
		distanceAwayFromOriginal = 20;
		doWalk = false;
		timer = 0;
		wait = 10;
		idleDelay = 600;
		
		moveSpeed = speed;
		maxSpeed = speed;
		stopSpeed = speed;
		
		currentAction = IDLE;
		
		doDraw = false;
		gaveQuest = false;
		
		interactionDistance = 5;
		
		ss = new SpiderSlayer(tileMap);
	}
	
	public boolean onScreen() {
		return Math.abs(p.getx() - this.x) < GamePanel.WIDTH &&
			   Math.abs(p.gety() - this.y) < GamePanel.HEIGHT;
	}

	@Override
	public void hit(int d) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void returnToArea() {
		if(!withinBounds()) {
			doWalk = true;
			if(x > anchoredX) {
				facingLeft = true;
				facingRight = false;
				facingDown = false;
				facingUp = false;
				dx = -moveSpeed;
				dy = 0;
			} else
			if(x < anchoredX) {
				facingLeft = false;
				facingRight = true;
				facingDown = false;
				facingUp = false;
				dx = moveSpeed;
				dy = 0;
			} else
			if(y > anchoredY) {
				facingLeft = false;
				facingRight = false;
				facingDown = true;
				facingUp = false;
				dy = -moveSpeed;
				dx = 0;
			} else
			if(y < anchoredY) {
				facingLeft = false;
				facingRight = false;
				facingDown = false;
				facingUp = true;
				dy = moveSpeed;
				dx = 0;
			}
		}
	}
	
	@Override
	public void update() {
		if(gaveQuest) {
			if(spiderCount >= 1) {
				SpiderSlayer.spiderCounter += spiderCount;
				spiderCount = 0;
			}
		}
		if(onScreen()) {
			if(timer < idleDelay + wait) {
				timer++;
			}
			
			checkTileMapCollision();
			
			handleInput();
			
			stopWalk();
			
			returnToArea();
			walk();
			
			setPosition(xtemp, ytemp);
			
			updateAnimations();
			
		}
	
	}

	@Override
	public void handleInput() {
		if(p.mayInteract(this, interactionDistance)) {
			if(KeyHandler.isPressed(KeyHandler.INTERACT)) {
				interact();
			}
		}
	}

	@Override
	public void interact() {
		if(!gaveQuest) {
			ss.setActivated(true);
			Player.QUESTS.add(ss);
			ArrayList<String> string = new ArrayList<String>();
			string.add(npcName + "\nAdded Quest @red@" + ss.questName + "@reset@\nClear Out All The Spiders In @blue@Neo@reset@.\nThere Are @blue@(15)@reset@ Left. \nPlease this is our last hope.");
			DialogueBox.setupBox(string,
					ContentHandler.NPC_TEXT_SIZE, 
					null, 
					null, true);
			gaveQuest = true;
		}
	}

	@Override
	public String getNPCName() {
		return npcName;
	}
	
	public void draw(Graphics2D g) {
		if(onScreen()) {
			setMapPosition();
			super.draw(g);
		}
	}
	
	// custom
	@Override
	public boolean isFriendly() {
		return friendly;
	}

	@Override
	public int getHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getId() { return id; }
	
	@Override
	public AnimationStates getAnimation() {
		if(currentAction == IDLE) return AnimationStates.IDLE;
		if(currentAction == WALK) return AnimationStates.WALK;
		return null;
	}
	
	@Override
	public boolean isAttacking() { return false; }
	
	@Override
	public String getDescription() {
		return npcDescription;
	}
	
}
