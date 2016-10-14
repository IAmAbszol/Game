package Entity;

import java.util.ArrayList;

import Entity.Player.Player;
import Handlers.ContentHandler;
import TileMap.TileMap;

public abstract class OnGroundItems extends MapObject {
	
	protected Player p;
	protected boolean drawMenu;
	private ArrayList<Items> items;
	private int currentSelection;
	
	public OnGroundItems(TileMap tm, Player p, MapObject o, ArrayList<Items> i) {
		super(tm);
		this.p = p;
		items = i;
		object = true;
		currentSelection = 0;
	}
	
	public OnGroundItems(TileMap tm, Player p, double x, double y, ArrayList<Items> i) {
		super(tm);
		this.p = p;
		items = i;
		object = true;
		currentSelection = 0;
	}
	
	public void take(Items i) {
		Player.INVENTORY.addItem(i);
		items.remove(i);
		drawMenu = false;
	}
	public void examine(Items i) {
		ArrayList<String> string = new ArrayList<String>();
		string.add(i.getName() + "\n" + i.getDescription());
		DialogueBox.setupBox(string, 
				30,
				ContentHandler.NPC_TEXT_SIZE, 
				null, 
				null, false);
		drawMenu = false;
	}
	public ArrayList<Items> getItems() {
		return items;
	}
	public int getCurrentSelection() {
		return currentSelection;
	}
	public void setCurrentSelection(int i) {
		currentSelection = i;
	}
	
	public abstract void update();
	public abstract void interact();
	public abstract void handleInput();
	public abstract void addItem(Items i);
	public abstract void removeItem(Items i);
}
