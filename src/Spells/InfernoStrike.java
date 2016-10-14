package Spells;

import java.awt.Graphics2D;
import java.io.Serializable;

import Entity.Magic;
import Entity.Player.Player;
import Handlers.ContentHandler;
import Main.GamePanel;
import TileMap.TileMap;

public class InfernoStrike extends Magic implements Serializable {

	public InfernoStrike() {

		spellName = "Inferno Strike";
		spellOutburst = "Kame-hami-ha!";
		
		sprite = ContentHandler.infernoMagic;
		moveSpeed = 8;
		
		width = 32;
		height = 32;
		cwidth = 15;
		cheight = 15;
		coffsetx = 0;
		coffsety = 0;
		
		delay = 50;
		
		damage = 3;
		spellReleaseTime = 10;
		
	}

	public boolean onScreen() {
		return Math.abs(player.getx() - this.x) < GamePanel.WIDTH &&
			   Math.abs(player.gety() - this.y) < GamePanel.HEIGHT;
	}

	@Override
	public void update() {
		
		if(!onScreen()) {
			setHit();
		}
		
		if(!onlySelected) {
			checkTileMapCollision();
			setPosition(xtemp, ytemp);
			
			if(dx == 0 && dy == 0 && !hit) {
				setHit();
			}
			
			updateAnimation();
			
			updateRemove();
		}
		
	}
	
	public void draw(Graphics2D g) {
		if(!onlySelected) {
			setMapPosition();
			super.draw(g);
		}

	}

	@Override
	public int getDamage() {
		return damage * (held + 1);
	}

	@Override
	public String getSpellName() {
		return spellName;
	}

	@Override
	public String getSpellOutburst() {
		return spellOutburst;
	}
	
}
