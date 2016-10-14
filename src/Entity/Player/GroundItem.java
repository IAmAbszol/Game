package Entity.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import Entity.Animation;
import Entity.DialogueBox;
import Entity.Items;
import Entity.MapObject;
import Entity.OnGroundItems;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import TileMap.TileMap;
import Time.Timer;

public class GroundItem extends OnGroundItems implements Serializable {
	
	private Color textColor;
	private BufferedImage image;
	private Color bgColor;
	private Font giFont;
	private int buffer;
	private Color selectedColor;
	private int drawStartX;
	private int drawStartY;
	private int textSize = 10;
	
	public GroundItem(TileMap tm, Player p, MapObject o, ArrayList<Items> i) {
		super(tm, p, o, i);
		double tmpx = o.getx() % tm.getTileSize();
		double tmpy = o.gety() % tm.getTileSize();
		xtemp = o.getx() - tmpx;
		ytemp = o.gety() - tmpy;
		this.setx(xtemp);
		this.sety(ytemp);
		drawMenu = false;
		animation = new Animation();
		animation.setDelay(5);
		giFont = ContentHandler.playerFont.deriveFont((float)textSize).deriveFont(Font.PLAIN);
		buffer = 5;
		textColor = Color.gray;
		selectedColor = Color.yellow;
	}
	
	public GroundItem(TileMap tm, Player p, double x, double y, ArrayList<Items> i) {
		super(tm, p, x, y, i);
		drawMenu = false;
		animation = new Animation();
		animation.setDelay(5);
		double tmpx = x % tm.getTileSize();
		double tmpy = y % tm.getTileSize();
		xtemp = x - tmpx;
		ytemp = y - tmpy;
		this.setx(xtemp);
		this.sety(ytemp);
		giFont = ContentHandler.playerFont.deriveFont((float)textSize).deriveFont(Font.PLAIN);
		buffer = 5;
		textColor = Color.gray;
		selectedColor = Color.yellow;
	}

	@Override
	public void update() {
		interact();
		setPosition(xtemp, ytemp);
		if(Timer.getDay()) {
			bgColor = new Color(0,0,0,.8f);
		} else {
			bgColor = new Color(0,0,0, 1f);
		}
	}

	public void drawObject(Graphics2D g) {
		for(int i = 0; i < getItems().size(); i++) {
			BufferedImage[] tmp = new BufferedImage[] {
				this.getItems().get(i).getImage()
			};
			animation.setFrames(tmp);
			setMapPosition();
			super.draw(g);
		}
	}
	
	public void drawMenu(Graphics2D g) {
		if(drawMenu && getItems().size() > 0) {
			int width = 0;
			int height = 0;
			for(int i = 0; i < getItems().size(); i++) {
				if(width < g.getFontMetrics().stringWidth("Examine " + getItems().get(i).getName())) {
					width = g.getFontMetrics().stringWidth("Examine " + getItems().get(i).getName());
				}
			}
			g.setFont(giFont);
			drawStartX = getx() + getMapX() + getItems().get(0).getImage().getWidth();
			drawStartY = gety() + getMapY();
			height += g.getFontMetrics().getHeight();
			for(int i = 0; i < getItems().size(); i++) {
				height += g.getFontMetrics().getHeight();
				height += g.getFontMetrics().getHeight();
			}
			g.setColor(bgColor);
			g.fillRect(drawStartX, drawStartY, width + buffer, height + buffer);
			
			drawText(g);
			
			g.setColor(Color.white);
			g.drawRect(drawStartX, drawStartY, width + buffer, height + buffer);
			
		}
	}
	
	public void drawText(Graphics2D g) {
		int tmp = drawStartY;
		for(int i = 0; i < (getItems().size() * 2) + 1; i++) {
			if(i == getCurrentSelection()) {
				g.setColor(selectedColor);
			} else
				g.setColor(textColor);
			if(i == 0) {
				g.drawString("Take All", drawStartX + buffer, tmp += g.getFontMetrics().getHeight());
				g.setColor(textColor);
			} else {
				if(i % 2 == 1) {
					g.drawString("Take " + getItems().get((i / 2)).getName(), drawStartX + buffer, tmp += g.getFontMetrics().getHeight());
				} else
					g.drawString("Examine " + getItems().get((i / 2) - 1).getName(), drawStartX + buffer, tmp += g.getFontMetrics().getHeight());
			}
		}
	}

	@Override
	public void interact() {
		if(p.withinArea(
				this.getx(), 
				this.gety(), 
				this.getx() + tileMap.getTileSize(), 
				this.gety() + tileMap.getTileSize())) {
			handleInput();
		} else {
			drawMenu = false;
			setCurrentSelection(0);
		}
	}

	@Override
	public void handleInput() {
		if(KeyHandler.isPressed(KeyHandler.INTERACT) && !drawMenu) {
			drawMenu = true;
			KeyHandler.keyState[KeyHandler.INTERACT] = false;
		}
		if(KeyHandler.isPressed(KeyHandler.BACK) && drawMenu) {
			drawMenu = false;
		}
		if(drawMenu && !DialogueBox.readOut) {
			if(KeyHandler.isPressed(KeyHandler.DOWN_K)) {
				setCurrentSelection(getCurrentSelection()+1);
				if(getCurrentSelection() > (getItems().size() * 2)) setCurrentSelection(0);
			}
			if(KeyHandler.isPressed(KeyHandler.UP_K)) {
				setCurrentSelection(getCurrentSelection()-1);
				if(getCurrentSelection() < 0) setCurrentSelection((getItems().size() * 2));
			}
			if(KeyHandler.isPressed(KeyHandler.INTERACT)) {
				handleSelection();
				KeyHandler.keyState[KeyHandler.INTERACT] = false;
			}
		}
	}

	@Override
	public void addItem(Items i) {
		this.getItems().add(i);
	}

	@Override
	public void removeItem(Items i) {
		this.getItems().remove(i);
	}

	public void handleSelection() {
		// take all
		if(getCurrentSelection() == 0) {
			while(getItems().size() > 0) {
				take(getItems().get(0));
				setCurrentSelection(0);
			}
			drawMenu = false;
		} else
		// take
		if(getCurrentSelection() % 2 == 1) {
			take(getItems().get(getCurrentSelection() / 2));
			setCurrentSelection(0);
		} else 
		// examine
		if(getCurrentSelection() % 2 == 0) {
			examine(getItems().get(getCurrentSelection() / 2 - 1));
			setCurrentSelection(0);
		}
	}
	
}
