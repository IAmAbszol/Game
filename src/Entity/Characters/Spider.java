package Entity.Characters;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;

import javax.sound.sampled.Clip;

import Audio.AudioPlayer;
import Entity.Animation;
import Entity.AnimationStates;
import Entity.NPC;
import Entity.Player.Player;
import Handlers.ContentHandler;
import Main.GamePanel;
import TileMap.TileMap;

public class Spider extends NPC implements Serializable {
	
	// animation ids
	private final int IDLE = 0;
	private final int WALK = 1;
	private final int ATTACK = 2;
	private final int DYING = 3;
	
	public Spider(TileMap tm, Player p, int id) {
		super(tm, p, id);
		
		audio = new HashMap<String, Clip>();
		audioDistance = 50;
		
		AudioPlayer.load(audio, "/SFX/NPC/Spider/Spider_Walk.wav", "spiderwalk", 1);
		AudioPlayer.loop(audio, "spiderwalk");
		AudioPlayer.mute(audio, "spiderwalk", true);

		this.p = p;
		this.id = id;
		
		health = 5;
		damage = 2;
		xp = 5;
		
		friendly = false; // enemy
		npcName = "Spider";
		npcDescription = "Creepy!";
		
		// walking
		speed = 1;
		distanceAwayFromOriginal = 300;
		doWalk = false;
		wait = 0;
		timer = 0;
		idleDelay = 5;
		walkDelay = 200;
		
		width = 32;
		height = 26;
		cwidth = 28;
		cheight = 22;
		coffsetx = 0;
		coffsety = 0;
		
		moveSpeed = speed;
		maxSpeed = speed;
		stopSpeed = speed;
		
		facingRight = false;
		facingLeft = true;
		facingUp = false;
		facingDown = false;
		object = false;
		
		sprites = ContentHandler.spider;
		
		rightAnimation = new Animation();
		rightAnimation.setFrames(sprites[2]);
		rightAnimation.setDelay(walkDelay);
		
		leftAnimation = new Animation();
		leftAnimation.setFrames(sprites[1]);
		leftAnimation.setDelay(walkDelay);
		
		downAnimation = new Animation();
		downAnimation.setFrames(sprites[0]);
		downAnimation.setDelay(walkDelay);
		
		upAnimation = new Animation();
		upAnimation.setFrames(sprites[3]);
		upAnimation.setDelay(walkDelay);
		
		currentAction = ATTACK;
		
	}
	
	@Override
	public int getHealth() {
		return health;
	}
	
	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public void hit(int d) {
		health -= d;
		if(health < 0) {
			AudioPlayer.stop("spiderwalk");
			NeoShopKeeper.spiderCount++;
			Player.xp += xp;
			health = 0;
		}
	}
	
	public boolean onScreen() {
		return Math.abs(p.getx() - this.x) < GamePanel.WIDTH &&
			   Math.abs(p.gety() - this.y) < GamePanel.HEIGHT;
	}
	
	@Override
	public void update() {
		if(onScreen()) {
			AudioPlayer.adjustDistanceVolume(audio, "spiderwalk", audioDistance, getActualDistance());
			if(timer < idleDelay + wait) {
				timer++;
			}
			
			checkTileMapCollision();
			inArea(this, p);
			
			handleInput();
			
			stopWalk();
			
			returnToArea();
			walk();
			
			setPosition(xtemp, ytemp);
			
			updateAnimations();
		}
	}

	public void draw(Graphics2D g) {
		if(onScreen()) {
			setMapPosition();
			super.draw(g);
		}
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFriendly() {
		return friendly;
	}
	
	@Override
	public void interact() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getNPCName() {
		return npcName;
	}
	
	@Override
	public int getId() { return id; }
	
	@Override
	public AnimationStates getAnimation() {
		if(currentAction == IDLE) return AnimationStates.IDLE;
		if(currentAction == ATTACK) return AnimationStates.ATTACK;
		if(currentAction == WALK) return AnimationStates.WALK;
		if(currentAction == DYING) return AnimationStates.DYING;
		return null;
	}
	
	@Override
	public boolean isAttacking() { 
		if(getAnimation() == AnimationStates.ATTACK) {
			return true;
		}
		return false; 
	}
	
	@Override
	public String getDescription() {
		return npcDescription;
	}

}
