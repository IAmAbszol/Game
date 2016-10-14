package Entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.sound.sampled.Clip;

import Entity.Player.Player;
import TileMap.TileMap;

public abstract class NPC extends MapObject {

	protected HashMap<String, Clip> audio;
	protected String npcName;
	public boolean doDraw;
	
	// protected
	protected Player p;
	protected int audioDistance;
	protected Random rand;
	protected boolean questNPC;
	protected String npcDescription;
	protected int id;
	
	protected BufferedImage[][] sprites;
	
	// walking
	protected double speed;
	protected boolean doWalk;
	protected int wait;
	protected int idleDelay;
	protected long timer;
	
	// attributes
	protected int health;
	protected int damage;
	protected int xp;
	
	// animations
	protected int walkDelay;
	
	protected int interactionDistance;
	
	public NPC() {}
	public NPC(TileMap tm, Player p, int id) {
		super(tm);
		// TODO Auto-generated constructor stub
	}
	
	public abstract boolean isFriendly();
	public abstract int getHealth();
	public abstract int getDamage();
	public abstract void hit(int d);
	public abstract void update();
	public abstract void handleInput();
	public abstract void interact();
	public abstract String getNPCName();
	public abstract int getId();
	public abstract AnimationStates getAnimation();
	public abstract boolean isAttacking();
	public abstract String getDescription();
	
	public void setId(int id) {
		this.id = id;
	}
	
	// useful npc tools
	public void setAnchor(int x, int y) {
		anchoredX = x;
		anchoredY = y;
	}
	
	public void checkObjectCollision(ArrayList<Objects> ob) {
		for(int i = 0; i < ob.size(); i++) {
			Objects o = ob.get(i);	
			if(inArea(this, o)) {
				timer = wait;
				stopWalk();
			}
		}
	}
	
	public void checkNPCCollision(NPC npc) {
		if(inArea(this, npc)) {
			timer = wait;
			stopWalk();
		}
	}
	
	public void returnToArea() {
		if(!withinBounds()) {
			doWalk = true;
			if(x > anchoredX) {
				facingLeft = true;
				facingRight = false;
				facingDown = false;
				facingUp = false;
				dx = -moveSpeed;
			} else
			if(x < anchoredX) {
				facingLeft = false;
				facingRight = true;
				facingDown = false;
				facingUp = false;
				dx = moveSpeed;
			} else
			if(y > anchoredY) {
				facingLeft = false;
				facingRight = false;
				facingDown = true;
				facingUp = false;
				dy = -moveSpeed;
			} else
			if(y < anchoredY) {
				facingLeft = false;
				facingRight = false;
				facingDown = false;
				facingUp = true;
				dy = moveSpeed;
			}
		}
	}

	public void stopWalk() {
		if(doWalk && timer >= wait && withinBounds()) {
			dy = 0;
			dx = 0;
			doWalk = false;
			wait = 0;
		}
	}
	
	public void walk() {
		if(!doWalk && timer >= idleDelay) {
			if(withinBounds()) {
				rand = new Random();
				int direction = rand.nextInt(100);
				if(direction >= 0 && direction < 4) {
					wait = rand.nextInt(180) + 1;
				}
				if(direction == 0) {
					// right
					facingLeft = false;
					facingUp = false;
					facingDown = false;
					facingRight = true;
					doWalk = true;
					timer = 0;
					
					dx = moveSpeed;
				} else
				if(direction == 1) {
					// left
					facingRight = false;
					facingUp = false;
					facingDown = false;
					facingLeft = true;
					doWalk = true;
					timer = 0;
					
					dx = -moveSpeed;
				} else
				if(direction == 2) {
					// up
					facingLeft = false;
					facingRight = false;
					facingDown = false;
					facingUp = true;
					doWalk = true;
					timer = 0;
					
					dy = -moveSpeed;
				} else 
				if(direction == 3) {
					// down
					facingLeft = false;
					facingRight = false;
					facingUp = false;
					facingDown = true;
					doWalk = true;
					timer = 0;
					
					dy = moveSpeed;
				}
			}
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
	
	// mathematical calculations
	public int getDistanceX() {
		return (int)Math.abs(p.getMapX() - this.xmap);
	}
	
	public int getDistanceY() {
		return (int)Math.abs(p.getMapY() - this.ymap);
	}
	
	public double getActualDistance() {
		double x = (double)Math.abs(p.getx() - this.x);
		double y = (double)Math.abs(p.gety() - this.y);
		double c = Math.sqrt((x*x) + (y*y));
		return c;
	}
	
	public boolean isQuestMonster() {
		return questNPC;
	}

}
