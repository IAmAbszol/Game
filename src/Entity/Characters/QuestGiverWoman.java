package Entity.Characters;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;

import javax.sound.sampled.Clip;

import Entity.Animation;
import Entity.AnimationStates;
import Entity.NPC;
import Entity.Player.Player;
import Entity.Player.Quest.VentureToUlrik;
import GameState.GameStateManager;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Main.GamePanel;
import TileMap.TileMap;

public class QuestGiverWoman extends NPC implements Serializable {

	public String outputText = "";
	
	// animation
	private static final int IDLE = 0;
	private static final int WALK = 1;
	
	// the quest
	private VentureToUlrik vtu;
	private boolean gaveQuest;
 	
	private boolean boot = true;
	
	public QuestGiverWoman(TileMap tm, Player p, int id) {
		super(tm, p, id);
		
		audio = new HashMap<String, Clip>();
	
		this.p = p;
		this.id = id;
		
		npcName = "Older Woman";
		doWalk = false;
		friendly = true;
		npcDescription = "A wise old woman on Neo.";
		
		width = 32;
		height = 32;
		cwidth = 22;
		cheight = 22;
		coffsetx = 0;
		coffsety = 0;

		moveSpeed = 1.5;
		maxSpeed = 1.5;
		stopSpeed = 1.5;
		
		walkDelay = 200;
		interactionDistance = 5;		
		
		facingRight = false;
		facingLeft = true;
		facingUp = false;
		facingDown = false;
		object = false;
		
		sprites = ContentHandler.questGiverWoman;
		
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
		
		currentAction = IDLE;
		
		doDraw = false;
		gaveQuest = false;
		
		vtu = new VentureToUlrik(tileMap);
		
	}
	
	public boolean onScreen() {
		return Math.abs(p.getx() - this.x) < GamePanel.WIDTH &&
			   Math.abs(p.gety() - this.y) < GamePanel.HEIGHT;
	}
	
	@Override
	public void update() {
		if(boot && GameStateManager.load) {
			interact();
			boot = false;
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
		if(boot && GameStateManager.load) {
			for(int i = 0; i < Player.QUESTS.size(); i++) {
				if(Player.QUESTS.get(i).questName.equals(vtu.questName)) {
					gaveQuest = true;
				}
			}
		} else
		if(!gaveQuest ) {
			boolean turn = false;
			for(int i = 0; i < Player.QUESTS.size(); i++) {
				if(Player.QUESTS.get(i).questName.equals(vtu.questName)) {
					turn = true;
					gaveQuest = true;
				}
			}
			if(!turn) {
				vtu.setActivated(true);
				Player.QUESTS.add(vtu);
				gaveQuest = true;
			}
		}
	}

	public String getNPCName() {
		return npcName;
	}

	@Override 
	public boolean isFriendly() {
		return friendly;
	}
	
	public void draw(Graphics2D g) {
		if(onScreen()) {
			setMapPosition();
			super.draw(g);
		}
	}

	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void hit(int d) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getHealth() {
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
