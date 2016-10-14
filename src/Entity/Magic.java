package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import Entity.Player.Player;
import Spells.EarthStrike;
import Spells.InfernoStrike;
import TileMap.TileMap;

public abstract class Magic extends MapObject {
	
	protected Player player;
	
	protected boolean hit;
	protected boolean remove;
	protected int damage;
	protected int manaCost;
	protected int held;				// special cases this will charge the spell
	protected int delay;
	protected int spellReleaseTime;	// integer of time that it takes to fully release the spell 
	protected String spellName;
	protected String spellOutburst;	// Character screams - KAMEHAMEHA!
	protected boolean onlySelected;
	
	protected BufferedImage[][] sprite;
	protected BufferedImage[][] hitSprite;
	
	private static HashMap<Integer, Magic> spell;

	public Magic() {
	}
	
	public abstract void update();
	public abstract int getDamage();
	public abstract String getSpellName();
	public abstract String getSpellOutburst();
	
	public double getMoveSpeed() {
		return moveSpeed;
	}
	
	public void preloadSpell(TileMap tm, Player p, int duration, boolean selectedSpell) {
		reset();
		player = p;
		held = computeDuration(duration, 30) / 10;
		setOrientation(moveSpeed);		// sets the orientation by what was passed into the class
		setPosition(player.getx(), player.gety());
		tileMap = tm;
		tileSize = tm.getTileSize();
		setChargeAnimation(sprite);	
		remove = false;
		hit = false;
	}
	
	public void reset() {
		remove = false;
		hit = false;
		facingRight = false;
		facingLeft = false;
		facingDown = false;
		facingUp = false;
	}
	
	// simply computes the max value
	// duration is 10, max is 7, output is 7
	public int computeDuration(int duration, int max) {
		if(duration <= max) 
			return duration;
		else
			return max;
	}
	
	public int getSpellReleaseTime() {
		return spellReleaseTime;
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		// later have hit sprites
		rightAnimation = null;
		leftAnimation = null;
		upAnimation = null;
		downAnimation = null;
		dx = 0;
		dy = 0;
	}
	
	public void setPlayerAnimation() {
		// set casting to true and have the player frames updated here
	}
	
	public void setAnimation(BufferedImage[] b) {
		if(facingRight) {
			rightAnimation = new Animation();
			rightAnimation.setFrames(b);
			rightAnimation.setDelay(delay);
		} else
		if(facingLeft) {
			leftAnimation = new Animation();
			leftAnimation.setFrames(b);
			leftAnimation.setDelay(delay);
		} else
		if(facingUp) {
			upAnimation = new Animation();
			upAnimation.setFrames(b);
			upAnimation.setDelay(delay);
		} else
		if(facingDown) {
			downAnimation = new Animation();
			downAnimation.setFrames(b);
			downAnimation.setDelay(delay);
		}
	}
	
	public Animation getAnimation() {
		if(facingRight) {
			return rightAnimation;
		} else
		if(facingLeft) {
			return leftAnimation;
		} else
		if(facingUp) {
			return upAnimation;
		} else
		if(facingDown) {
			return downAnimation;
		}
		return null;
	}
	
	public void setChargeAnimation(BufferedImage[][] b) {
		if(facingRight) {
			rightAnimation = new Animation();
			rightAnimation.setFrames(b[held]);
			rightAnimation.setDelay(delay);
		} else
		if(facingLeft) {
			leftAnimation = new Animation();
			leftAnimation.setFrames(b[held]);
			leftAnimation.setDelay(delay);
		} else
		if(facingUp) {
			upAnimation = new Animation();
			upAnimation.setFrames(b[held]);
			upAnimation.setDelay(delay);
		} else
		if(facingDown) {
			downAnimation = new Animation();
			downAnimation.setFrames(b[held]);
			downAnimation.setDelay(delay);
		}
	}
	
	public void setOrientation(double speed) {
		if(player.getFacingRight()) {
			facingRight = true;
			dx = speed;
			dy = 0;
		}
		if(player.getFacingLeft()) {
			facingLeft = true;
			dx = -speed;
			dy = 0;
		}
		if(player.getFacingUp()) {
			facingUp = true;
			dx = 0;
			dy = -speed;
		}
		if(player.getFacingDown()) {
			facingDown = true;
			dy = speed;
		}
	}
	
	public void updateAnimation() {
		if(hit == false && remove == false) {
			if(facingRight) {
				rightAnimation.update();
			} else
			if(facingLeft) {
				leftAnimation.update();
			} else
			if(facingUp) {
				upAnimation.update();
			} else
			if(facingDown) {
				downAnimation.update();
			}
		}
	}

	public void updateRemove() {
		if(facingRight) {
			if(hit) {
				remove = true;
			}
		} else
		if(facingLeft) {
			if(hit) {
				remove = true;
			}
		} else
		if(facingUp) {
			if(hit) {
				remove = true;
			}
		} else
		if(facingDown) {
			if(hit) {
				remove = true;
			}
		}
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void checkObjectCollision(ArrayList<Objects> ob) {
		for(int i = 0; i < ob.size(); i++) {
			Objects o = ob.get(i);	
			if(inArea(this, o)) {
				setHit();
			}
		}
	}
	
	public void checkNPCCollision(ArrayList<NPC> npc) {
		for(int i = 0; i < npc.size(); i++) {
			NPC n = npc.get(i);	
			if(inArea(this, n)) {
				setHit();
			}
		}
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
	// boot loader
	public static void preloadSpellTable() {
		spell = new HashMap<Integer, Magic>();
		spell.put(0, new EarthStrike());
		spell.put(1, new InfernoStrike());
	}
	
	// deciphers particular spells from the table
	public static Magic decipherSpell(int i) {
		return spell.get(i);
	}
	
}
