package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import Entity.DialogueBox;
import Entity.HUD;
import Entity.Items;
import Entity.MenuList;
import Entity.NPC;
import Entity.Objects;
import Entity.Characters.NeoShopKeeper;
import Entity.Characters.Spider;
import Entity.InGameObjects.Fountain;
import Entity.InGameObjects.Tree;
import Entity.Player.GroundItem;
import Entity.Player.Player;
import Entity.Player.Interfaces.PlayerScreen;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Items.HealthPotion;
import Main.GamePanel;
import Save.SaveState;
import TileMap.Background;
import TileMap.TileMap;
import Time.Timer;

public class Neo extends GameState {

	private TileMap tileMap;
	private Background bg;

	private DialogueBox db;
	private MenuList ml;
	private Timer timer;
	private PlayerScreen ps;
	private HUD hud;
	
	private int lowerX;
	private int lowerY;
	private int largerX;
	private int largerY;
	
	private int startX;
	private int startY;
	
	private boolean loadEntity = false;
	private boolean boot = true;
	
	public Neo(GameStateManager gsm, int x, int y) {
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
		ml = new MenuList();
		timer = new Timer();
		tileMap = new TileMap(30);
		tileMap.loadTiles(ContentHandler.NEO, 1000);
		tileMap.loadMap("/Maps/funWorld");
		tileMap.setPosition(0,0);
		tileMap.setTween(1);		
		
		if(!gsm.load) {
			// load player --> Only works when boot isn't on
			if(gsm.player != null) {
				this.player = gsm.player;
				player.setTileMap(tileMap);
				GameStateManager.savePlayer(player);
			} else
				player = new Player(tileMap);
			
			if(startX >= 0 && startY >= 0) {
				player.setPosition(startX, startY);
			} else
				player.setPosition(223,251);
			
		} else {
			gsm.load = false;
			loadEntity = true;
			System.out.println("Loaded Player...");
			player = new Player(tileMap);
			SaveState s = new SaveState(player);
			s.loadGame();
			player.setPosition(s.loadXPos(), s.loadYPos());
		}
		
		populateWorld();
		
		hud = new HUD(player, npc, objs);
		ps = new PlayerScreen(player);
		
		lowerX = 40;
		lowerY = 45;
		largerX = 100;
		largerY = 120;
		
		AREA_EVENT = false;
		EVENT_STARTED = false;
		EVENT_COMPLETED = false;
	}
	
	public void populateWorld() {
		
		objs = new ArrayList<Objects>();
		npc = new ArrayList<NPC>();
		gi = new ArrayList<GroundItem>();
		
		HealthPotion hp = new HealthPotion();
		addItemBasedTile(hp, player.getx(), player.gety());
		addItemBasedTile(hp, player.getx() + 20, player.gety());
		addItemBasedTile(hp, player.getx() + 10, player.gety());
		addItemBasedTile(hp, player.getx() + 30, player.gety());
		addItemBasedTile(hp, player.getx() + 40, player.gety());
		
		Spider s = new Spider(tileMap, player, npc.size());
		s.setPosition(player.getx() + 50, player.gety());
		s.setAnchor(player.getx() + 50, player.gety());
		npc.add(s);
		
		NeoShopKeeper nsk = new NeoShopKeeper(tileMap, player, npc.size());
		nsk.setPosition(200, 327);
		nsk.setAnchor(200, 327);
		npc.add(nsk);
		
		Fountain f = new Fountain(tileMap, player, 223, 400, objs.size());
		objs.add(f);
		
		Tree t3;
		Point[] treePoints = new Point[] {
			new Point(210,290),
		//	new Point(250, 290),
		//	new Point(270, 500),
		//	new Point(700,251)
		};
		for(int i = 0; i < treePoints.length; i++) {
			t3 = new Tree(tileMap, player, treePoints[i].x, treePoints[i].y, objs.size());
			objs.add(t3);
		}
		Spider s4;
		Point[] spiderPoints = new Point[] {
			
		};
		for(int i = 0; i < spiderPoints.length; i++) {
			s4 = new Spider(tileMap, player,npc.size());
			s4.setPosition(spiderPoints[i].x,spiderPoints[i].y);
			s4.setAnchor(spiderPoints[i].x,spiderPoints[i].y);
			npc.add(s4);
		}
	
	}

	@Override
	public void update() {
		
		if(loadEntity && boot) {
			loadEntity = false;
			boot = false;
			SaveState ss = new SaveState(new Player(tileMap));
			ss.loadMapEntities();
			ss = null;
		} 
		
		handleInput();
		
		if(!DialogueBox.readOut) {
			ps.handleInput();
		}
		
		if(DialogueBox.readOut) {
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
		
		// events pause the game
		if(!AREA_EVENT) {
		
			if(!ps.getPause() && (!DialogueBox.readOut || DialogueBox.doTimer == true)) {
				
				player.setObjects(objs);
				player.setNPC(npc);
				player.update();
				player.checkAttack(npc);
				
				ml.update();
	
				tileMap.setPosition(
						GamePanel.WIDTH / 2 - player.getx(),
						GamePanel.HEIGHT / 2 - player.gety()
					);
				
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
				
				for(int i = 0; i < objs.size(); i++) {
					Objects e = objs.get(i);
					e.update(); // updates every object in the given game.
				}
				
				if(timer.doTime) {
					timer.update();
				}
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
		
		for(int i = 0; i < npc.size(); i++) {
			NPC n = npc.get(i);
			n.draw(g);
		}
		
		player.draw(g);
		
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
		
		ml.drawMenu(g);
		
		if(!ps.getPause()) {
			hud.draw(g);
		} else { 
			ps.draw(g);
		}
		
		timer.draw(g);
		
		if(DialogueBox.readOut || DialogueBox.rotateDown) {
			db.draw(g);
		}
		
		// events
		event(g);
		
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
	//		if(!EVENT_COMPLETED) {
				EVENT_STARTED = true;
				AREA_EVENT = true;
	//		}
	//		if(AREA_EVENT && EVENT_COMPLETED) {
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
				AREA_EVENT = false;
				EVENT_STARTED = false;
				EVENT_COMPLETED = false;
				gsm.setState(3, 871,326);
		//		}
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

	public void event(Graphics2D g) {
		// function developed to achieve specific map events
		if(AREA_EVENT && EVENT_STARTED) {
			enteringEvent(g, player, true);
		}
	}
	
}
