package Handlers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Items;
import Entity.Magic;
import Entity.NPC;
import Entity.Objects;
import GameState.GameState;
import GameState.GameStateManager;
import Gif.GifDecoder;
import Items.HealthPotion;
import Items.Sword;
import Main.GamePanel;

public class ContentHandler extends GameState {

	/*
	 * Class designed to bootload everything, will create it to either force load or slowly load throughout the game.
	 */

	// fun stuff
	private Font bootfont;
	private int progressBar = 0;

	private ArrayList<String> bootlog = new ArrayList<String>();
	
	private boolean finalAnimation = false;
	
	private long wait = 0;
	
	private int bootOrder = 0;

	public static BufferedImage[][] MenuBackground; // w and h are of each square, not everything
	
	// myPlayer
	public static BufferedImage[][] PlayerSpellCast;
	public static BufferedImage[][] PlayerWalk;
	public static BufferedImage[][] PlayerHurt;
	public static BufferedImage[][] PlayerAttack;
	
	// magic
	public static BufferedImage[][] earthMagic;
	public static BufferedImage[][] darkMagic;
	public static BufferedImage[][] infernoMagic;
	public static BufferedImage[][] lightMagic;
	
	// Enemies
	public static BufferedImage[][] spider;
	
	// ai
	public static int NPC_TEXT_SIZE = 12;
	public static BufferedImage[][] questGiverWoman;
	public static BufferedImage[][] shopKeeper;
	
	// Maps
	public static BufferedImage[] grasslandsOfNeo;
	public static BufferedImage[] NEO;
	
	// items
	public static ArrayList<Items> items;
	public static BufferedImage[] potionOfHealth;
	public static BufferedImage[] sword;
	
	// Objects
	public static BufferedImage[][] fountain;
	public static BufferedImage[][] tree;
	public static BufferedImage[][] smallChest;
	public static BufferedImage[][] chest;
	public static BufferedImage[][] largeChest;
	public static BufferedImage[][] chestOfTheGods; // 4 gods, 4 different sheets
	public static BufferedImage[][] sign;
	public static BufferedImage[][] stump;

	// Fonts
	public static Font menuTitleFont;
	public static Font playerFont;
	public static Font outputFont;
	
	// Colors
	public static Color outputColor;
	
	// Audio
	
	
	public ContentHandler(GameStateManager gsm) {
		super(gsm);
		bootfont = new Font("Arial", Font.BOLD, 12);
	}
	
	public static BufferedImage[][] load(String s, int w, int h, int fw, int fh, int spw, int sph) {
		// force width and height, one sprite sheet of many different things
		// start point width, where to start, same with height for these things
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(ContentHandler.class.getResourceAsStream(s));
			if(fw == 0 && fh == 0) {
				int width = spritesheet.getWidth() / w;
				int height = spritesheet.getHeight() / h;
				ret = new BufferedImage[height][width];
				for(int i = 0; i < height; i++) {
					for(int j = 0; j < width; j++) {
						ret[i][j] = spritesheet.getSubimage(spw + (j * w), sph + (i * h), w, h);
					}
				}
			} else {
				int width = fw / w;
				int height = fh / h;
				ret = new BufferedImage[height][width];
				for(int i = 0; i < height; i++) {
					for(int j = 0; j < width; j++) {
						ret[i][j] = spritesheet.getSubimage(spw + (j * w), sph + (i * h), w, h);
					}
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}

	public void loadTextures() {
		switch(bootOrder) {
			
		case 0:
			bootlog.add("Loading Player Attributes...");
			bootOrder++;
			break;
			
		case 1:
			// magic
			earthMagic = load("/Magic/magic.png", 32,32,192,128,0,0);
			darkMagic = load("/Magic/magic.png", 32,32,192,128,192,0);
			infernoMagic = load("/Magic/magic.png", 32,32,192,128,0,128);
			lightMagic = load("/Magic/magic.png", 32,32,192,128,192,128);
			PlayerWalk = load("/Entity/Player/this.png",30,40,0,0,0,0);
			Magic.preloadSpellTable();
			outputFont = new Font("Arial", Font.PLAIN, 12);
			outputColor = Color.yellow;
			bootOrder++;
			break;
			
		case 2:
			bootlog.add("Finished! Loaded Player Attributes Into Game");
			progressBar += GamePanel.WIDTH / 7;
			bootOrder++;
			break;
			
		case 3:
			bootlog.add("Loading NPC Textures...");
			bootOrder++;
			break;
			
		case 4:
			questGiverWoman = load("/Entity/NPC/Sprite_old_woman_walk.png", 32, 32, 0,0,0,0);
			spider = load("/Entity/NPC/SpiderSpriteB3.png", 30, 26,0,0,0,0);
		//	shopKeeper = load("/Entity/NPC/voice_in_the_winds_sprite_set_001_by_theratiogames-d5q4o9d", 32, 48, 128, 192, 0,0);
			bootOrder++;
			break;
			
		case 5:
			bootlog.add("Finished! Loaded NPC Textures Into Game");
			progressBar += GamePanel.WIDTH / 7;
			bootOrder++;
			break;
			
		case 6:
			bootlog.add("Loading Worlds");
			bootOrder++;
			break;
		
		case 7: 
			// load map heaven textures/sprite sheet
			grasslandsOfNeo = spliceImages("/Tilesets/grasslandsofneo.gif");
			NEO = spliceImages("/Tilesets/testtileset2.gif");
			bootOrder++;
			break;
			
		case 8: 
			bootlog.add("Finished! Loading Worlds");
			progressBar += GamePanel.WIDTH / 7;
			bootOrder++;
			break;
			
		case 9: 
			bootlog.add("Loading Font Textures");
			bootOrder++;
			break;
		
		case 10:
			 try{
			    	InputStream is = getClass().getResourceAsStream("/Fonts/Menu/IMMORTAL.ttf");
			    	menuTitleFont = Font.createFont(Font.TRUETYPE_FONT, is);
			    	is = getClass().getResourceAsStream("/Fonts/Menu/data-latin.ttf");
			    	playerFont = Font.createFont(Font.TRUETYPE_FONT, is);
			    } catch(FontFormatException e){} catch (IOException e){}
			bootOrder++;
			break;
			
		case 11:
			bootlog.add("Finished! Loading Font Textures");
			progressBar += GamePanel.WIDTH / 7;
			bootOrder++;
			break;
			
		case 12:
			bootlog.add("Setting Up Controller(s)");
			bootOrder++;
			break;
			
		case 13:
			// scan
			bootOrder++;
			break;
			
		case 14:
			bootlog.add("Finished! Setting Up Native Controllers");
			progressBar += GamePanel.WIDTH / 7;
			bootOrder++;
			break;
			
		case 15:
			bootlog.add("Loading Item and Object Textures...");
			bootOrder++;
			break;
			
		case 16:
			fountain = load("/Objects/fountain.png", 100, 100, 0, 0, 0, 0);
			smallChest = load("/Objects/chest_sheet.png", 32, 48, 0,0,0,0);
			stump = load("/Objects/stump.png", 64,85,0,0,0,0);
			tree = load("/Objects/Tree1.png", 64,85,0,0,0,0);
			sign = load("/Objects/Sign.png",32,32,0,0,0,0);
			potionOfHealth = spliceImages("/Items/PotionOfHealth.gif");
			sword = spliceImages("/Items/sword.gif");
			
			bootOrder++;
			break;
			
		case 17: 
			bootlog.add("Finished! Item and Object Textures Into Game");
			progressBar += GamePanel.WIDTH / 7;
			bootOrder++;
			break;
	
		case 18:
			bootlog.add("Loading Audio Files...");
			bootOrder++;
			break;
			
		case 19:
			// load all audio files, create a function for it once juke box is done
			bootOrder++;
			break;
			
		case 20:
			bootlog.add("Finished! Audio Files Into Game");
			progressBar += GamePanel.WIDTH / 7 + 2;
			bootOrder++;
			break;
			
		case 21:
			finalAnimation = true;
			break;
		}
	}
	
	// may also be found in Background class, though this is helpful
	// this class divides up the images into seperate sheets to become animated
	public static BufferedImage[] spliceImages(String simage) {
		BufferedImage[] imageAnimation;
		GifDecoder d = new GifDecoder();
		InputStream resourceStream = ContentHandler.class.getResourceAsStream(simage);
		InputStream buffer = new BufferedInputStream(resourceStream);
		d.read(buffer);
		int n = d.getFrameCount();
		imageAnimation = new BufferedImage[n];
		for(int i = 0; i < n; i++) {
			imageAnimation[i] = d.getFrame(i);
		}
		return imageAnimation;
	}
	
	public void init() {
	}

	public void update() {
		loadTextures();
	}

	public void draw(Graphics2D g) {
		if(!finalAnimation) {	
			// messages
			g.setBackground(Color.black);
			g.setFont(bootfont);
			g.setColor(Color.white);
			for(int i = 0; i < bootlog.size(); i++) {
				g.drawString(bootlog.get(i), 0, (i * g.getFontMetrics().getHeight())+g.getFontMetrics().getHeight());
			}
			
			// progress bar
			g.drawRect(20, GamePanel.HEIGHT - 30, GamePanel.WIDTH - 40, 20); // may remove
			g.fillRect(20, GamePanel.HEIGHT - 30, progressBar - 40, 20);
		}
		if(finalAnimation) {
			if(wait < 1) {
				wait++;
			} else
				gsm.setState(GameStateManager.MENU, -1, -1);
		}
	}
	
	/*
	 * Garbage
	 */
	
	public void handleInput() {}

	@Override
	public void teleport() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addItemBasedTile(Items i, double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addObject(Objects o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeObject(Objects o, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNPC(NPC n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeNPC(NPC n, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Objects> getMapObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<NPC> getMapNPC() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}