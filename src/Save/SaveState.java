package Save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import Entity.NPC;
import Entity.Objects;
import Entity.Characters.Spider;
import Entity.Player.GroundItem;
import Entity.Player.Inventory;
import Entity.Player.Player;
import Entity.Player.Quests;
import GameState.GameStateManager;
import TileMap.TileMap;

/*
 * Class dedicated to loading everything out and into the game
 * Handles specific things to the player.
 * Mostly handles player stuff, nothing with Enemies on certain levels etc, simple
 * 
 * Work on, when load, load the world first, wait for player init, then add new stuff to player
 */
@SuppressWarnings("serial")
public class SaveState implements Serializable {
	
	private String saveFile = "savestate.txt";
	private String saveDirectory = "ROTG - Save States/";

	// player stuff
	public int health;
	public int maxHealth;
	public double mana;
	public double maxMana;
	public double strength;
	public double maxStrength;
	public double defense;
	public double maxDefense;
	
	public int gold;
	public int xp;
	public int level;
	
	public double regeneration;
	
	public int forceLevel = 0; // If you level up again with this being one, you can choose twice.
	
	public int currentSpell;
	public int manaCost;
	public int castDamage;
	
	public ArrayList<Quests> q;
	public Inventory inv;
	
	// scratch
	public int swipeDamage;
	public int swipeRange;
	
	// gamestate
	public  int currentState;
	
	public double xpos;
	public double ypos;
	
	// maps
	public ArrayList<Objects> objs;
	public ArrayList<NPC> npc;
	public ArrayList<GroundItem> gi;
	
	public SaveState(Player player) {
		
		health = Player.health;
		maxHealth = Player.maxHealth;
		mana = Player.mana;
		maxMana = Player.maxMana;
		strength = Player.strength;
		maxStrength = Player.maxStrength;
		defense = Player.defense;
		maxDefense = Player.maxDefense;
		
		gold = Player.gold;
		xp = Player.xp;
		level = Player.level;
		
		regeneration = Player.regeneration;
		
		forceLevel = Player.forceLevel;
		
		swipeRange = Player.swipeRange;
		swipeDamage = Player.swipeDamage;
		
		castDamage = Player.castDamage;
		manaCost = Player.manaCost;
		
		currentState = GameStateManager.currentState;
		
		q = Player.QUESTS;
		
		currentSpell = Player.currentSpell;
		inv = Player.INVENTORY;
		xpos = player.getx();
		ypos = player.gety();
		
		if(GameStateManager.grabState() != null) {
			npc = GameStateManager.grabState().getMapNPC();
		}
	}
	
	public boolean fileExists(String name) {
		File f = new File(findFile(name));
		return f.exists();
	}
	
	public void setupDirectories(String path) {
		new File(path).mkdir();
	}
	
	public String findFile(String name) {
		String user = System.getProperty("user.name");
		if(System.getProperty("os.name").contains("Windows")) {
			return "C:/Users/"+user+"/AppData/Roaming/"+ name;
		} else
			return System.getProperty("user.home") + "/" + name;
	}
	
	
	public void saveGame() {
		try {

			File f = new File(findFile(saveDirectory + saveFile));
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();

		} catch (Exception e) {
			setupDirectories(findFile(saveDirectory));
			saveGame();
		}
		
	}
	
	public void saveGame(String mapName) {
		try {
			
			File f = new File(findFile(saveDirectory + mapName));
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();

		} catch (Exception e) {
			setupDirectories(findFile(saveDirectory));
			saveGame(mapName);
		}
	}
	
	/*
	 * @GameStateManager, do not call when being initialized inside a constructor!
	 * else, GameStateManager.grabState() will be null
	 */
	public void loadMapEntities() {
		try {
			
			File f = new File(findFile(saveDirectory + saveFile));
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			SaveState ss = (SaveState) ois.readObject();
			if(GameStateManager.grabState() != null) {
				System.out.println("In load map entity");
				GameStateManager.grabState().setMapNPC(ss.npc);
			}
			ois.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SaveState loadGame(String mapName) {
		try {
			File f = new File(findFile(saveDirectory + saveFile));
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			return (SaveState) ois.readObject();
		} catch (Exception e) {
			setupDirectories(findFile(saveDirectory));
			saveGame();
		}
		return null;
	}
	
	public int loadState() {
		try {
			File f = new File(findFile(saveDirectory + saveFile));
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			SaveState ss = (SaveState) ois.readObject();
			ois.close();
			return ss.currentState;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void loadGame() {
		try {
			File f = new File(findFile(saveDirectory + saveFile));
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			SaveState ss = (SaveState) ois.readObject();
			
			Player.currentSpell = ss.currentSpell;
			Player.regeneration = ss.regeneration;
			Player.health = ss.health;
			Player.maxHealth = ss.maxHealth;
			Player.mana = ss.mana;
			Player.maxMana = ss.maxMana;
			Player.strength = ss.strength;
			Player.maxStrength = ss.maxStrength;
			Player.defense = ss.defense;
			Player.maxDefense = ss.maxDefense;
			Player.gold = ss.gold;
			Player.xp = ss.xp;
			Player.level = ss.level;
			Player.forceLevel = ss.forceLevel;
			Player.manaCost = ss.manaCost;
			Player.castDamage = ss.castDamage;
			Player.QUESTS = ss.q;
			Player.INVENTORY = ss.inv;
			Player.swipeDamage = ss.swipeDamage;
			Player.swipeRange = ss.swipeRange;
			GameStateManager.currentState = ss.currentState;
			ois.close();
			ss = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public double loadXPos() {
		try {
			File f = new File(findFile(saveDirectory + saveFile));
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			SaveState ss = (SaveState) ois.readObject();
			ois.close();
			return ss.xpos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public double loadYPos() {
		try {
			File f = new File(findFile(saveDirectory + saveFile));
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			SaveState ss = (SaveState) ois.readObject();
			ois.close();
			return ss.ypos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public double loadWorld() {
		try {
			File f = new File(findFile(saveDirectory + saveFile));
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			SaveState ss = (SaveState) ois.readObject();
			ois.close();
			return ss.currentState;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public String getSaveFile() {
		return saveFile;
	}
	
	public String getSaveDirectory() {
		return saveDirectory;
	}
	
}
