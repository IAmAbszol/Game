package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Entity.Player.Player;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Time.Timer;

public class MenuList {

	private static MapObject o;
	private static Player p;
	
	private static ArrayList<String> options;
	private static boolean drawMenu;
	private static int currentSelection;
	private static boolean selected;
	private Color bgColor;
	private int buffer;
	private static int interactionDistance;

	private int drawStartX;
	private int drawStartY;
	
	public MenuList() {
		options = new ArrayList<String>();
		drawMenu = false;
		currentSelection = 0;
		selected = false;
		buffer = 5;
		interactionDistance = 0;
	}
	
	public static void setupMenuList(MapObject ob, Player p, ArrayList<String> opt, int interactionDistance) {
		o = ob;
		options = opt;
		currentSelection = 0;
		selected = false;
		drawMenu = true;
		MenuList.interactionDistance = interactionDistance;
		MenuList.p = p;
	}
	
	public static int getSelection() {
		return currentSelection;
	}
	
	public static boolean amSelected() {
		 return selected;
	}
	
	public static boolean drawMenu() {
		return drawMenu;
	}
	
	public void update() {
		if(Timer.getDay()) {
			bgColor = new Color(0,0,0,.8f);
		} else {
			bgColor = new Color(0,0,0, 1f);
		}
		handleInput();
	}
	
	public void handleInput() {
		if(o != null && p != null) {
			if(p.mayInteract(o, interactionDistance)) {
				if(KeyHandler.isPressed(KeyHandler.BACK) && drawMenu) {
					drawMenu = false;
					selected = true;
					options = null;	// memory leak?
					o = null;
					p = null;
				}
				if(drawMenu) {
					if(KeyHandler.isPressed(KeyHandler.DOWN_K)) {
						currentSelection++;
						if(currentSelection > options.size() - 1) {
							currentSelection = 0;
						}
					}
					if(KeyHandler.isPressed(KeyHandler.UP_K)) {
						currentSelection--;
						if(currentSelection < 0) {
							currentSelection = options.size() - 1;
						}
					}
					
					if(KeyHandler.isPressed(KeyHandler.INTERACT)) {
						drawMenu = false;
						selected = true;
						options = null;	// memory leak?
						o = null;
						p = null;
						KeyHandler.keyState[KeyHandler.INTERACT] = false;
					}
				}
			} else {
				drawMenu = false;
				selected = true;
				options = null;	// memory leak?
				o = null;
				p = null;
			}
		}
	}
	
	public void drawMenu(Graphics2D g) {
		if(drawMenu && options != null) {
			if(drawMenu && options.size() > 0) {
				int width = 0;
				int height = 0;
				for(int i = 0; i < options.size(); i++) {
					if(width < g.getFontMetrics().stringWidth(options.get(i))) {
						width = g.getFontMetrics().stringWidth(options.get(i));
					}
				}
				g.setFont(ContentHandler.playerFont.deriveFont((float)10).deriveFont(Font.PLAIN));
				
				drawStartX = o.getx() + o.getMapX() - o.getCOffsetX();
				drawStartY = o.gety() + o.getMapY() - o.getCOffsetY();
				
				for(int i = 0; i < options.size(); i++) {
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
	
	private void drawText(Graphics2D g) {
		int tmp = drawStartY;
		for(int i = 0; i < options.size(); i++) {
			if(i == currentSelection) {
				g.setColor(Color.yellow);
			} else
				g.setColor(Color.gray);
			
			g.drawString(options.get(i), drawStartX + buffer, tmp += g.getFontMetrics().getHeight());
			
		}
	}
	
	public boolean mayWeDraw() {
		return drawMenu;
	}
	
}
