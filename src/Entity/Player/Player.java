package Entity.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import Entity.Animation;
import Entity.DialogueBox;
import Entity.Magic;
import Entity.MapObject;
import Entity.Characters.Spider;
import Entity.NPC;
import Entity.Objects;
import GameState.GameStateManager;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Save.SaveState;
import Spells.EarthStrike;
import TileMap.TileMap;

public class Player extends MapObject {
	
	// player stuff
	public static int health;
	public static int maxHealth;
	public static double mana;
	public static double maxMana;
	public static double strength;
	public static double maxStrength;
	public static double defense;
	public static double maxDefense;
	public static double stamina;
	public static double maxStamina;
	
	public static int gold;
	public static int xp;
	public static int level;
	
	public static double regeneration;
	
	public static int forceLevel = 0; // If you level up again with this being one, you can choose twice.
	
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	// spells
	private long recast = 0; // recast rate, time to wait
	private int ttw = 1; // in seconds
	private int duration = 0;
	private long timer = 0;
	private boolean casting;
	private boolean releasedSpell;
	public static Magic spell;
	public static int manaCost;
	public static int castDamage;
	public static int currentSpell = 0;
	public ArrayList<Magic> spells;	// currently released spells

	// core player components
	public static ArrayList<Quests> QUESTS;
	public static Inventory INVENTORY;
	
	// scratch
	private boolean swiping;
	public static int swipeDamage;
	public static int swipeRange;
	
	private double dashSpeed;
	
	// animations
	public static BufferedImage[][] sprites;

	// animation actions
	private static final int IDLE = 0;
	private static final int WALK = 1;
	private static final int CAST = 2;
	private static final int ATTACK = 3;
	private static final int BLOCK = 4;
	private static final int HEAL = 5;
	private static final int DYING = 6;
	private static final int DASHING = 7;
	
	// delays
	private final int idleDelay = 0;
	private final int walkDelay = 250;
	private final int castDelay = 0;
	private final int attackDelay = 0;
	private final int blockDelay = 0;
	private final int healDelay = 0;
	private final int dyingDelay = 0;
	
	// collision
	private ArrayList<Objects> obj;
	private ArrayList<NPC> npc;
	
	public Player(TileMap tm) {
		super(tm);
		
		QUESTS = new ArrayList<Quests>();
		INVENTORY = new Inventory();
		
		width = 30;
		height = 40;
		cwidth = 20;
		cheight = 30;
		coffsetx = 0;
		coffsety = 0;
		
		moveSpeed = 2.5;
		maxSpeed = 2.5;
		stopSpeed = 2.5;
		dashSpeed = moveSpeed * 2;

		health = maxHealth = 10;
		mana = maxMana = 20;
		strength = maxStrength = 20;
		defense = maxDefense = 20;
		xp = 0;
		level = 1;
		
		gold = 25;
		regeneration = .2;
		
		swipeDamage = (int)(strength / 1.75);
		swipeRange = 40;
		
		facingRight = true;
		facingLeft = false;
		facingUp = false;
		facingDown = false;
		object = false;
		
		sprites = ContentHandler.PlayerWalk;
		
		downAnimation = new Animation();
		downAnimation.setFrames(sprites[0]);
		downAnimation.setDelay(walkDelay);
		
		rightAnimation = new Animation();
		rightAnimation.setFrames(sprites[1]);
		rightAnimation.setDelay(walkDelay);
		
		leftAnimation = new Animation();
		leftAnimation.setFrames(sprites[2]);
		leftAnimation.setDelay(walkDelay);
		
		upAnimation = new Animation();
		upAnimation.setFrames(sprites[3]);
		upAnimation.setDelay(walkDelay);
		
		releasedSpell = false;
		casting = false;
		spells = new ArrayList<Magic>();
		manaCost = 5;
		castDamage = 3;
		recast = ttw * 30;
		
		currentAction = IDLE;
		
		obj = new ArrayList<Objects>();
		npc = new ArrayList<NPC>();
		
	}
	
	public void resetAnimations() { // For save state if new skins are involved
		downAnimation.setFrames(sprites[0]);
		downAnimation.setDelay(walkDelay);
		
		rightAnimation.setFrames(sprites[1]);
		rightAnimation.setDelay(walkDelay);
		
		leftAnimation.setFrames(sprites[2]);
		leftAnimation.setDelay(walkDelay);
		
		upAnimation.setFrames(sprites[3]);
		upAnimation.setDelay(walkDelay);
	}
	
	private void checkObjectCollision() {
		for(int i = 0; i < obj.size(); i++) {
			Objects o = obj.get(i);	
			inArea(this, o);
		}
	}
	
	private void getNextPosition() {

		// movement
		if(left && !right && !up && !down) {
			facingLeft = true;
			facingRight = false;
			facingUp = false;
			facingDown = false;
			dx -= moveSpeed;
			dy = 0;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if(right && !left && !up && !down) {
			facingLeft = false;
			facingRight = true;
			facingUp = false;
			facingDown = false;
			dx += moveSpeed;
			dy = 0;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		} else if(up && !left && !right && !down) {
			facingLeft = false;
			facingRight = false;
			facingUp = true;
			facingDown = false;
			dy -= moveSpeed;
			dx = 0;
			if(dy < -maxSpeed) {
				dy = -maxSpeed;
			}
		} else if(down && !left && !right && !up) {
			facingLeft = false;
			facingRight = false;
			facingUp = false;
			facingDown = true;
			dx = 0;
			dy += moveSpeed;
			if(dy > maxSpeed) {
				dy = maxSpeed;
			}
		} else {
				if(dx > 0) {
					dx = 0;
				} else if(dx < 0) {
					dx = 0;
				} else	if(dy > 0) {
					dy = 0;
				} else if(dy < 0) {
					dy = 0;
				}
		}
		
	}
	
	public void initAnimations() {
		if(currentAction == IDLE) {
			
		}
	}
	
	public void updateAnimations() {
		if(facingRight) {
			rightAnimation.update();
			leftAnimation.setFrame(0);
			downAnimation.setFrame(0);
			upAnimation.setFrame(0);
		} else
		if(facingLeft) {
			leftAnimation.update();
			rightAnimation.setFrame(0);
			downAnimation.setFrame(0);
			upAnimation.setFrame(0);
		} else
		if(facingDown) {
			downAnimation.update();
			leftAnimation.setFrame(0);
			rightAnimation.setFrame(0);
			upAnimation.setFrame(0);
		} else
		if(facingUp) {
			upAnimation.update();
			leftAnimation.setFrame(0);
			downAnimation.setFrame(0);
			rightAnimation.setFrame(0);
		}
	}
	
	public int checkOrientation() {
		if(facingRight) return 0;
		if(facingLeft) return 1;
		if(facingUp) return 2;
		if(facingDown) return 3;
		return 0;
	}
	
	public void update() {
		
		writeOut();
		
		// update position
		checkTileMapCollision();
		checkObjectCollision();

		getNextPosition();
		setPosition(xtemp, ytemp);
		
		setCasting();
		
		for(int i = 0; i< INVENTORY.slot.size(); i++) {
			if(INVENTORY.slot.get(i).isEmpty()) {
				INVENTORY.removeItem(i, this.getx(), this.gety());
			}
		}
		for(int i = 0; i < QUESTS.size(); i++) {
			QUESTS.get(i).updatePosition(x + xmap - width / 2, y + ymap - height / 2);
			QUESTS.get(i).update();
		}
		
		// add animations for casting
		// then check here
	
		for(int i = 0; i < spells.size(); i++) {
			spells.get(i).checkObjectCollision(obj);
		}
		
		for(int i = 0; i < npc.size(); i++) {
			for(int j = 0; j < spells.size(); j++) {
				if(inArea(spells.get(j), npc.get(i))) {
					System.out.println("here");
				}
			}
		}
		
		// casting
		mana += .2;
		if(mana > maxMana) mana = maxMana;
		if(!casting && releasedSpell) {
			if(mana > manaCost) {
				mana -= manaCost;
				spell = Magic.decipherSpell(0);
				spell.preloadSpell(tileMap, this, duration, false);
				spells.add(spell);
				releasedSpell = false;
				duration = 0;
				recast = 0;
				currentAction = IDLE; // stops casting since the spell was shot
			}
		}
		
		// update spell
		for(int i = 0; i < spells.size(); i++) {
			spells.get(i).update();
			
			if(spells.get(i).shouldRemove()) {
				spells.remove(i);
				i--;
			}
		}
		
		// done flinching?
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		if(dy == 0 && dx == 0) {
			currentAction = IDLE;
			// set frames
		}
		
		xpConverter();
		
		updateAnimations();
		
		if(this.load && !DialogueBox.readOut) {
			ArrayList<String> string = new ArrayList<String>();
			string.add("bolddb:I am Kyle, this game you are playing is a taste of what I can do in Java.");
			string.add("bolddb:Hello! My name is Kyle Darling, I created this game just for fun but Java is AWESOME!");
			string.add("Welcome to Realm of the Gods!");
			string.add("bolddb:I am Kyle, this game you are playing is a taste of what I can do in Java.");
			string.add("By utilizing everything I know, I was able to create my own virtual reality, pretty sweet huh?");
			string.add("Please enjoy and have fun, this place is massive.");
			DialogueBox.setupBox(string, 12, null, null, false);
			
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();

		// spells
		for(int i = 0; i < spells.size(); i++) {
			spells.get(i).draw(g);
		}
		
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.drawString("("+x + ", " + y + ")", (int)x + getMapX() - 5, (int)y + getMapY() - g.getFontMetrics().getHeight() - 5);
		
		super.draw(g);
		
	}
	
	/*
	 * Player Stuff
	 */

	public static void xpConverter() {
		if(xp >= (level * 100)) {
			xp -= (level * 100);
			level++;
			// create interface to choose what to increase
			maxHealth += 2;
			health += 2;
			if(health >= maxHealth) {
				health = maxHealth;
			}
			maxStrength++;
			strength = maxStrength;
			maxDefense++;
			defense = maxDefense;
			maxStamina++;
			stamina = maxStamina;
			maxMana++;
			mana = maxMana;
		}
	}

	public void writeOut() {
		if(save) {
			SaveState save = new SaveState(this);
			save.saveGame();
			save = null;
			System.out.println("Saved...");
			KeyHandler.keyState[KeyHandler.SAVE] = false;
		}
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public double getMana() { return mana; }
	public double getMaxMana() { return maxMana; }
	public double getStrength() { return strength; }
	public double getMaxStrength() { return maxStrength; }
	public double getDefense() { return defense; }
	public double getMaxDefense() { return maxDefense; }
	
	public void setCasting() {
		if(recast <= (ttw * 30) && currentAction != CAST) {
			recast++;
		}
		if(cast && !releasedSpell && recast >= (ttw * 30)) {
			casting = true;
			currentAction = CAST;
			duration++;
		} else
		if(KeyHandler.keyState[KeyHandler.CAST] == false && casting) {
			casting = false;
			releasedSpell = true;
		}
	}
	
	public void checkAttack(ArrayList<NPC> npc) {
		for(int i = 0; i < npc.size(); i++) {
			NPC n = npc.get(i);
			
			// spells
			for(int j = 0; j < spells.size(); j++) {
				if(spells.get(j).inArea(spells.get(j), n) && !n.isFriendly()) {
					n.hit(spells.get(j).getDamage());
					break;
				}
			}
			
			// may alter based on npc type + if attacking
			if(intersects(n) && !n.isFriendly() && n.isAttacking()) {
				hit(n.getDamage());
			}
			
			if(n.getHealth() <= 0 && !n.isFriendly()) {
				npc.remove(i);
			}
			
		}
	}
	
	public void hit(int damage) {
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void setObjects(ArrayList<Objects> obj) {
		this.obj = obj;
	}
	
	public void setNPC(ArrayList<NPC> npc) {
		this.npc = npc;
	}
	
	public int getTTW() {
		return ttw * 30;
	}
	
}
