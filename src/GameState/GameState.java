package GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import Entity.Items;
import Entity.NPC;
import Entity.Objects;
import Entity.Player.GroundItem;
import Entity.Player.Player;
import Main.GamePanel;

public abstract class GameState {
	
	protected GameStateManager gsm;
	protected Player player;
	
	protected ArrayList<GroundItem> gi;
	protected ArrayList<Objects> objs;
	protected ArrayList<NPC> npc;
	
	protected boolean AREA_EVENT;
	protected boolean EVENT_STARTED;
	protected boolean EVENT_COMPLETED;
	private int EVENT_POSITION;
	
	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
		EVENT_POSITION = 0;
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void handleInput();
	public abstract void teleport();
	public abstract void event(Graphics2D g);
	
	public abstract void addItemBasedTile(Items i, double x, double y);
	
	public void enteringEvent(Graphics2D g, Player p, boolean outro) {
		int EVENT_SPEED = 20;
		int BUFFER = 10;
		if(outro) {
			// fill with black
			g.setColor(Color.black);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			// do half-transparent oval
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setComposite(
					AlphaComposite.getInstance(AlphaComposite.CLEAR, .5f)
					);
			g.fillOval(p.getx(), p.gety(), 
					10, 10
					);
			// full oval transparency
//			g.setComposite(
//					AlphaComposite.getInstance(AlphaComposite.CLEAR, 1.0f)
//					);
//			g.fillOval(p.getx(), p.gety(), 
//					(GamePanel.WIDTH / 2) - (EVENT_SPEED * EVENT_POSITION) - BUFFER, 
//					(GamePanel.HEIGHT / 2) - (EVENT_SPEED * EVENT_POSITION) - BUFFER
//					);
		} else {
			
		}
		System.out.println("Here");
		if(EVENT_POSITION < 10) {
			EVENT_POSITION++;
		} else {
			EVENT_STARTED = false;
			EVENT_COMPLETED = true;
		}
	}
	
	public void addObject(Objects o) {
		objs.add(o);
		objs.get(objs.size() - 1).setId(objs.size() - 1);
	}

	public void removeObject(Objects o, int id) {
		for(int i = 0; i < objs.size(); i++) {
			if(objs.get(i).getId() == id) {
				objs.remove(i);
			}
		}
		for(int j = 0; j < objs.size(); j++) {
			objs.get(j).setId(j);
		}
		
	}

	public void addNPC(NPC o) {
		npc.add(o);
		npc.get(npc.size() - 1).setId(npc.size() - 1);
	}

	public void removeNPC(NPC o, int id) {
		for(int i = 0; i < npc.size(); i++) {
			if(npc.get(i).getId() == id) {
				npc.remove(i);
			}
		}
		for(int j = 0; j < npc.size(); j++) {
			npc.get(j).setId(j);
		}
		
	}

	public ArrayList<Objects> getMapObjects() {
		return objs;
	}

	public ArrayList<NPC> getMapNPC() {
		return npc;
	}
	
	public ArrayList<GroundItem> getMapGroundItem() {
		return gi;
	}
	
	public void setMapNPC(ArrayList<NPC> npc) {
		this.npc = npc;
	}
	
	public void setMapObjects(ArrayList<Objects> objs) {
		this.objs = objs;
	}
	
	public void getMapGroundItem(ArrayList<GroundItem> gi) {
		this.gi = gi;
	}
	
}
