package Entity.Characters.QuestNPCs;

import java.awt.image.BufferedImage;
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

public class GreatDemonAlcarcius extends NPC {
	
	private final int interactionDistance = 40;
	
	private BufferedImage[][] sprites;
	
	// animation ids
	private final int IDLE = 0;
	private final int WALK = 1;
	private final int ATTACK = 2;
	private final int DYING = 3;
	
	public GreatDemonAlcarcius(TileMap tm, Player p, int id) {
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
		npcName = "Great Demon Alcarcius";
		npcDescription = "The Great Demon Alcarcius, Straight from hell.";
		questNPC = false;
		
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
	public boolean isFriendly() {
		return friendly;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
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
	public int getId() {
		return id;
	}

	@Override
	public AnimationStates getAnimation() {
		// TODO Auto-generated method stub
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
