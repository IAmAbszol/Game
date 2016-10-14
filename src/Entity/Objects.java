package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.Clip;

import Entity.Player.Player;
import Handlers.ContentHandler;
import TileMap.TileMap;

/*
 * Had to be careful naming the class Objects, instead of Object --> Java interference
 */
public abstract class Objects extends MapObject {

	protected Player p;
	protected int id;
	protected int audioDistance;
	protected String objectName;
	protected HashMap<String, Clip> audio;
	protected String objectDescription;
	protected int interactionDistance;
	
	// interaction
	protected boolean drawMenu;
	protected boolean listen;
	protected ArrayList<String> menuOptions;
	protected int currentSelection;
	protected Color bgColor;
	protected int buffer;

	private int drawStartX;
	private int drawStartY;
	
	public Objects(TileMap tm, Player p, double x, double y, int id) {
		super(tm);
		this.p = p;
		this.x = x;
		this.y = y;
		snapObject(); //unique
		xtemp = x;
		ytemp = y;
		buffer = 5;
	}

	public abstract void update();
	public abstract void handleInput();
	public abstract void interact();
	public abstract String getObjectName();
	public abstract String getObjectDescription();
	public abstract void setObjectName(String n);
	public abstract int getId();
	public abstract void setId(int id);
	public abstract boolean isListening();
	
	public void snapObject() {
		double xtmp = x % tileSize;
		double ytmp = y % tileSize;
		x -= xtmp;
		y -= ytmp;
	}
	
	public double getActualDistance() {
		double x = (double)Math.abs(p.getx() - this.x);
		double y = (double)Math.abs(p.gety() - this.y);
		double c = Math.sqrt((x*x) + (y*y));
		return c;
	}
	
	public int getDistanceX() {
		return (int)Math.abs(p.getMapX() - this.xmap);
	}
	public int getDistanceY() {
		return (int)Math.abs(p.getMapY() - this.ymap);
	}
	
	public void drawMenu(Graphics2D g) {
		if(drawMenu) {
			if(drawMenu && menuOptions.size() > 0) {
				int width = 0;
				int height = 0;
				for(int i = 0; i < menuOptions.size(); i++) {
					if(width < g.getFontMetrics().stringWidth(menuOptions.get(i))) {
						width = g.getFontMetrics().stringWidth(menuOptions.get(i));
					}
				}
				g.setFont(ContentHandler.playerFont.deriveFont((float)10).deriveFont(Font.PLAIN));
				
				drawStartX = getx() + getMapX() - getCOffsetX();
				drawStartY = gety() + getMapY() - getCOffsetY();
				
				for(int i = 0; i < menuOptions.size(); i++) {
					height += g.getFontMetrics().getHeight();
				}
				g.setColor(bgColor);
				g.fillRect(drawStartX, drawStartY, width + buffer, height + buffer);
				
				drawText(g);
				
				g.setColor(Color.white);
				g.drawRect(drawStartX, drawStartY, width + buffer, height + buffer);
			}
		}
		
	}
	
	public void drawText(Graphics2D g) {
		int tmp = drawStartY;
		for(int i = 0; i < menuOptions.size(); i++) {
			if(i == currentSelection) {
				g.setColor(Color.yellow);
			} else
				g.setColor(Color.gray);
			
			g.drawString(menuOptions.get(i), drawStartX + buffer, tmp += g.getFontMetrics().getHeight());
			
		}
	}
	
	public void examine(Objects o) {
		ArrayList<String> string = new ArrayList<String>();
		string.add("@red@"+o.getObjectName() + "@reset@\n" + o.getObjectDescription());
		DialogueBox.setupBox(string,
				45,
				ContentHandler.NPC_TEXT_SIZE, 
				null, 
				null, false);
		drawMenu = false;
	}
	
}
