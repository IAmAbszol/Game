package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import Entity.DialogueBox;
import Entity.HUD;
import Entity.Items;
import Entity.MapObject;
import Entity.NPC;
import Entity.Objects;
import Entity.Characters.Spider;
import Entity.InGameObjects.Tree;
import Entity.Player.GroundItem;
import Entity.Player.Player;
import Entity.Player.Interfaces.PlayerScreen;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Main.GamePanel;
import Save.SaveState;
import TileMap.Background;
import TileMap.TileMap;
import Time.Timer;

public class GrasslandsOfNeo extends GameState {

	private TileMap tileMap;
	private Background bg;
	
	private ArrayList<Objects> objs;
	private ArrayList<NPC> npc;

	private DialogueBox db;
	private Timer timer;
	private PlayerScreen ps;
	private HUD hud;
	
	private int lowerX;
	private int lowerY;
	private int largerX;
	private int largerY;
	
	private int startX;
	private int startY;
	
	public GrasslandsOfNeo(GameStateManager gsm, int x, int y) {
		super(gsm);
		if(x >= 0 && y >= 0) {
			startX = x;
			startY = y;
		} else {
			startX = startY = -1;
		}
		init();
	}

	@Override
	public void init() {
		KeyHandler.resetKeys();
		db = new DialogueBox();
		timer = new Timer();
		tileMap = new TileMap(30);
		tileMap.loadTiles(ContentHandler.grasslandsOfNeo, 1000);
		tileMap.loadMap("/Maps/grasslands.map");
		tileMap.setPosition(0,0);
		tileMap.setTween(1);		
		
		if(!gsm.load) {
			if(gsm.player != null) {
				this.player = gsm.player;
				player.setTileMap(tileMap);
				GameStateManager.savePlayer(player);
			} else
				player = new Player(tileMap);
			if(startX >= 0 && startY >= 0) {
				player.setPosition(startX, startY);
			} else
				player.setPosition(871,326);
		} else {
			gsm.load = false;
			System.out.println("Loaded Player...");
			player = new Player(tileMap);
			SaveState s = new SaveState(player);
			s.loadGame();
			player.setPosition(s.loadXPos(), s.loadYPos());
		}
		
		populateWorld();
		
		hud = new HUD(player, npc, objs);
		ps = new PlayerScreen(player);
		
		lowerX = 880;
		lowerY = 305;
		largerX = 900;
		largerY = 400;
		
	}
	
	public void populateWorld() {
		
		objs = new ArrayList<Objects>();
		npc = new ArrayList<NPC>();
		gi = new ArrayList<GroundItem>();
		
		Tree t3;
		Point[] treePoints = new Point[] {
			new Point(830,326),
			new Point(60, 326)
		};
		for(int i = 0; i < treePoints.length; i++) {
			t3 = new Tree(tileMap, player, treePoints[i].x,treePoints[i].y, objs.size());
			objs.add(t3);
		}
		Spider s4;
		Point[] spiderPoints = new Point[] {
			new Point(429,400),
			new Point(436,608),
			new Point(320,562),
			new Point(285,373),
			new Point(548,140),
			new Point(388,274),
			new Point(203,306),
			new Point(216,134),
		};
		for(int i = 0; i < spiderPoints.length; i++) {
			s4 = new Spider(tileMap, player, npc.size());
			s4.setPosition(spiderPoints[i].x,spiderPoints[i].y);
			s4.setAnchor(spiderPoints[i].x,spiderPoints[i].y);
			npc.add(s4);
		}
	}

	@Override
	public void update() {
		
		handleInput();
		
		if(!db.readOut) {
			ps.handleInput();
		}
		
		if(db.readOut) {
			db.update();
		}
		
		tileMap.update();
		
		for(int i = 0; i < gi.size(); i++) {
			GroundItem ground = gi.get(i);
			if(ground.getItems().size() <= 0) {
				gi.remove(i);
			} else
				ground.update();
		}
		
		if(!ps.getPause() && (!DialogueBox.readOut || DialogueBox.doTimer == true)) {
			
			for(int i = 0; i < player.spells.size(); i++) {
				player.spells.get(i).checkObjectCollision(objs);
			}
			for(int i = 0; i < player.spells.size(); i++) {
				player.spells.get(i).checkNPCCollision(npc);
			}
			
			player.setObjects(objs);
			player.setNPC(npc);

			player.update();
			player.checkAttack(npc);

			tileMap.setPosition(
					GamePanel.WIDTH / 2 - player.getx(),
					GamePanel.HEIGHT / 2 - player.gety()
				);
			
			for(int i = 0; i < objs.size(); i++) {
				Objects e = objs.get(i);
				e.update(); // updates every object in the given game.
			}
			for(int i = 0; i < npc.size(); i ++) {
				NPC n = npc.get(i);
				n.update();
				n.checkObjectCollision(objs);
				for(int j = 0; j < npc.size(); j++) {
					NPC n2 = npc.get(j);
					if(n.getId() != n2.getId()) {
						n.checkNPCCollision(n2);
					}
				}
				
			}
			if(timer.doTime) {
				timer.update();
			}
		}
		
		teleport();
		
	}

	@Override
	public void draw(Graphics2D g) {

		tileMap.draw(g);
		
		for(int i = 0; i < gi.size(); i++) {
			GroundItem ground = gi.get(i);
			ground.drawObject(g);
		}
		
		player.draw(g);

		for(int i = 0; i < npc.size(); i++) {
			NPC n = npc.get(i);
			n.draw(g);
		}
		
		for(int i = 0; i < objs.size(); i++) {
			objs.get(i).draw(g);
		}
		
		for(int i = 0; i < objs.size(); i++) {
			objs.get(i).drawMenu(g);
		}
		
		for(int i = 0; i < gi.size(); i++) {
			GroundItem ground = gi.get(i);
			ground.drawMenu(g);
		}
		
		if(!ps.getPause()) {
			hud.draw(g);
		} else { 
			ps.draw(g);
		}
		
		timer.draw(g);
		
		if(db.readOut) {
			db.draw(g);
		}
		
		
	}

	@Override
	public void handleInput() {
		// player
		player.setUp(KeyHandler.keyState[KeyHandler.UP]);
		player.setLeft(KeyHandler.keyState[KeyHandler.LEFT]);
		player.setDown(KeyHandler.keyState[KeyHandler.DOWN]);
		player.setRight(KeyHandler.keyState[KeyHandler.RIGHT]);
		player.setAttack(KeyHandler.keyState[KeyHandler.ATTACK]);
		player.setCast(KeyHandler.keyState[KeyHandler.CAST]);
		player.setSave(KeyHandler.keyState[KeyHandler.SAVE]);
		player.setLoad(KeyHandler.keyState[KeyHandler.LOAD]);
		player.setDash(KeyHandler.keyState[KeyHandler.DASH]);
		
		// screen
		ps.setLeft(KeyHandler.keyState[KeyHandler.LEFT]);
		ps.setRight(KeyHandler.keyState[KeyHandler.RIGHT]);
		ps.setScreen(KeyHandler.keyState[KeyHandler.ENTER] || KeyHandler.keyState[KeyHandler.BACK]);
		ps.setInteract(KeyHandler.keyState[KeyHandler.INTERACT]);
		ps.setUp(KeyHandler.keyState[KeyHandler.UP]);
		ps.setDown(KeyHandler.keyState[KeyHandler.DOWN]);
		
	}

	@Override
	public void teleport() {
		if(player.withinArea(lowerX, lowerY, largerX, largerY)) {
			// add teleporting noise
			gsm.player = this.player;
			this.player = null;
			db = null;
			timer = null;
			tileMap = null;
			npc.removeAll(npc);
			objs.removeAll(objs);
			gi.removeAll(gi);
			objs = null;
			npc = null;
			hud = null;
			ps = null;
			gsm.setState(2, 223,251);
		}
		
	}
	
	@Override
	public void addItemBasedTile(Items i, double x, double y) {
		boolean continueThru = true;
		// item
		double newtmpX = x % tileMap.getTileSize();
		double newtmpY = y % tileMap.getTileSize();
		double newX = x - newtmpX;
		double newY = y - newtmpY;
		for(int j = 0; j < gi.size(); j++) {
			// gi
			double oldtmpX = gi.get(j).getx() % tileMap.getTileSize();
			double oldtmpY = gi.get(j).gety() % tileMap.getTileSize();
			double oldX = gi.get(j).getx() - oldtmpX;
			double oldY = gi.get(j).gety() - oldtmpY;
			if(oldX == newX && oldY == newY) {
				continueThru = false;
				gi.get(j).addItem(i);
			}
		}
		if(continueThru) {
			ArrayList<Items> it = new ArrayList<Items>();
			it.add(i);
			GroundItem g = new GroundItem(tileMap, player, newX, newY, it);
			gi.add(g);
		}
	}

	@Override
	public void event(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
