package Save;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Entity.Magic;
import Entity.Player.Inventory;
import Entity.Player.Quests;

/*
 * Class that houses players saving abilities between maps/buildings
 */
public class PlayerSave {

	// player stuff
	public static int health;
	public static int maxHealth;
	public static double mana;
	public static double maxMana;
	public static double strength;
	public static double maxStrength;
	public static double defense;
	public static double maxDefense;
	
	public static int gold;
	public static int xp;
	public static int level;
	
	public static int forceLevel = 0; // If you level up again with this being one, you can choose twice.
	
	public int world;
	
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	// spells
	private long recast = 0; // recast rate, time to wait
	private int ttw = 1; // in seconds
	private int duration = 0;
	private long timer = 0;
	private boolean casting;
	private boolean releasedSpell;
	public static int currentSpell;
	public static int manaCost;
	public static int castDamage;
	public ArrayList<Magic> spells;
		// core player components
	public static ArrayList<Quests> q;
	public static ArrayList<Inventory> inv;
	
	// scratch
	private boolean swiping;
	public static int swipeDamage;
	public static int swipeRange;
	
	private double dashSpeed;
	
	// animations
	public static BufferedImage[][] sprites;

	public int currentAction;
	
}