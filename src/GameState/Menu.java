package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Controller.ControllerAdapter;
import Entity.DialogueBox;
import Entity.Items;
import Entity.MapObject;
import Entity.NPC;
import Entity.Objects;
import Entity.Player.Player;
import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Main.GamePanel;
import Main.MainGame;
import Save.SaveState;
import TileMap.Background;
import TileMap.TileMap;

public class Menu extends GameState {
	
	private Background bg;
	private DialogueBox db;
	
	private Color titleColor;
	private Font titleFont;
	
	private boolean ON_MAIN_MENU = true;
	private String[] mainSelection = {
			"Start",
			"Options",
			"Help",
			"Quit"
	};
	
	private boolean ON_START_MENU = false;
	private String[] startMenu = {
		"Start New Game",
		"Load Previous Game"
	};
	
	private boolean ON_OPTIONS_MENU = false;
	private String[] optionsMenu = {
			"Enable Gamepad [  ]",
			"Mute System Audio [  ]",
			"Debug Menu [  ]"
	};
	
	private int MAIN_MENU_CHOICE = 0;
	private int START_MENU_CHOICE = 0;
	private int OPTIONS_MENU_CHOICE = 0;
		
	public Menu(GameStateManager gsm) {
		super(gsm);
		
		bg = new Background("/Backgrounds/cool.gif", 1, true, 50);
		bg.fitBackground(true); // default is false
		titleColor = new Color(128, 0, 0);
		titleFont = ContentHandler.menuTitleFont;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(ContentHandler.menuTitleFont);
		
		
		AudioPlayer.load("/SFX/MenuOption.wav", "menuoption", 0);
		AudioPlayer.load("/SFX/MenuSelect.wav", "menuselect", 0);
		AudioPlayer.load("/Music/MenuAudio.wav", "menuaudio", 0);
		AudioPlayer.loop("menuaudio");
		
		db = new DialogueBox();
		DialogueBox.setMoveSpeed(25);
		
		if(MapObject.debug) {
			optionsMenu[2] = "Debug Menu [X]";
		}
		
	}

	private void select() {
		if(ON_MAIN_MENU) {
			SELECT_MAIN_MENU();
		} else
		if(ON_OPTIONS_MENU) {
			SELECT_OPTIONS_MENU();
		} else
		if(ON_START_MENU) {
			SELECT_START_MENU();
		}
	}
	
	private void SELECT_MAIN_MENU() {
		if(ON_MAIN_MENU) {
			if(MAIN_MENU_CHOICE == 0) {
				AudioPlayer.play("menuoption");
				ON_MAIN_MENU = false;
				ON_START_MENU = true;
			}
			else if(MAIN_MENU_CHOICE == 1) {
				// options - Dialogue box opens
				AudioPlayer.play("menuoption");
				ON_MAIN_MENU = false;
				ON_OPTIONS_MENU = true;
			}
			else if(MAIN_MENU_CHOICE == 2) {
				AudioPlayer.play("menuoption");
				int buffer = 10;
				ArrayList<String> text = new ArrayList<String>();
				text.add(readoutHelpDocs("/Help/HELP1.txt"));
				DialogueBox.setupCustomBox(text, 10, null, null, false, buffer, buffer, GamePanel.WIDTH - buffer * 2, GamePanel.HEIGHT - buffer * 2);
			}
			else if(MAIN_MENU_CHOICE == 3) {
				AudioPlayer.play("menuoption");
				System.exit(0);
			}
		}
	}
	private void SELECT_START_MENU() {
		if(START_MENU_CHOICE == 0) {
			AudioPlayer.play("menuselect");
			AudioPlayer.stop("menuaudio");
			DialogueBox.resetMoveSpeed();
			gsm.setState(2, -1, -1);
		} else
		if(START_MENU_CHOICE == 1) {
			AudioPlayer.play("menuselect");
			AudioPlayer.stop("menuaudio");
			TileMap tm = new TileMap(30);
			Player p = new Player(tm);
			SaveState s = new SaveState(p);
			if(s.fileExists(s.getSaveDirectory() + s.getSaveFile())) {
				gsm.load = true;
				DialogueBox.resetMoveSpeed();
				gsm.setState((int)s.loadWorld(), -1, -1);
			} else {
				DialogueBox.resetMoveSpeed();
				gsm.load = false;
				gsm.setState(2, -1, -1);
			}
		} 
	}

	private void SELECT_OPTIONS_MENU() {
		if(OPTIONS_MENU_CHOICE == 0) {
			if(!ControllerAdapter.ENABLE_CONTROLLER) {
				optionsMenu[0] = "Enable Gamepad [X]";
				ControllerAdapter.ENABLE_CONTROLLER = true;
			} else {
				optionsMenu[0] = "Enable Gamepad [  ]";
				ControllerAdapter.ENABLE_CONTROLLER = false;
			}
		} else
		if(OPTIONS_MENU_CHOICE == 1) {
			if(AudioPlayer.mute) {
				optionsMenu[1] = "Mute System Audio [  ]";
				AudioPlayer.mute = false;
			} else {
				optionsMenu[1] = "Mute System Audio [X]";
				AudioPlayer.mute = true;
			}
		} else
			if(OPTIONS_MENU_CHOICE == 2) {
				if(MapObject.debug) {
					optionsMenu[2] = "Debug Menu [  ]";
					MapObject.debug = false;
				} else {
					optionsMenu[2] = "Debug Menu [X]";
					MapObject.debug = true;
				}
			}
	}
	
	private void moveBack() {
		if(!ON_MAIN_MENU) {
			AudioPlayer.play("menuoption");
			if(ON_START_MENU) {
				ON_START_MENU = false;
				ON_MAIN_MENU = true;
			}
			if(ON_OPTIONS_MENU) {
				ON_OPTIONS_MENU = false;
				ON_MAIN_MENU = true;
			}
		}
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		
		if(DialogueBox.readOut || DialogueBox.rotateDown) {
			db.update();
		}
		
		if(!DialogueBox.readOut) {
			handleInput();
		}
		KeyHandler.update();
		bg.update();

	}

	@Override
	public void draw(Graphics2D g) {
		
		bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(ContentHandler.menuTitleFont.deriveFont(32f).deriveFont(Font.BOLD));
		g.drawString(MainGame.GAME_NAME, 35, 70);
		
		// draw menu options
		int largestWidth = 0;
		g.setFont(ContentHandler.menuTitleFont.deriveFont(20f));
		for(int i = 0; i < mainSelection.length; i++) {
			if(i == MAIN_MENU_CHOICE) {
				g.setColor(Color.BLACK);
			}
			else {
				g.setColor(Color.RED);
			}
			g.drawString(mainSelection[i], 35, 140 + i * g.getFontMetrics().getHeight());
			if(largestWidth < g.getFontMetrics().stringWidth(mainSelection[i])) {
				largestWidth = g.getFontMetrics().stringWidth(mainSelection[i]);
			}
			
		}
		
		DRAW_START_MENU(g, largestWidth);
		DRAW_OPTIONS_MENU(g, largestWidth);
		
		if(DialogueBox.readOut || DialogueBox.rotateDown)
			db.draw(g);
		
	}

	@Override
	public void handleInput() {
		if(KeyHandler.isPressed(KeyHandler.ENTER) || KeyHandler.isPressed(KeyHandler.INTERACT)) select();
		if(KeyHandler.isPressed(KeyHandler.BACK)) moveBack();
		MAIN_MENU();
		START_MENU();
		OPTIONS_MENU();
	}
	
	private void MAIN_MENU() {
		if(ON_MAIN_MENU) {
			if(KeyHandler.isPressed(KeyHandler.UP)) {
				AudioPlayer.play("menuoption");
				MAIN_MENU_CHOICE--;
				if(MAIN_MENU_CHOICE < 0) MAIN_MENU_CHOICE = mainSelection.length - 1;
			}
			if(KeyHandler.isPressed(KeyHandler.DOWN)) {
				AudioPlayer.play("menuoption");
				MAIN_MENU_CHOICE++;
				if(MAIN_MENU_CHOICE > mainSelection.length - 1) MAIN_MENU_CHOICE = 0;
			}
		}
	}
	
	private void START_MENU() {
		if(ON_START_MENU) {
			if(KeyHandler.isPressed(KeyHandler.UP)) {
				AudioPlayer.play("menuoption");
				START_MENU_CHOICE--;
				if(START_MENU_CHOICE < 0) START_MENU_CHOICE = startMenu.length - 1;
			}
			if(KeyHandler.isPressed(KeyHandler.DOWN)) {
				AudioPlayer.play("menuoption");
				START_MENU_CHOICE++;
				if(START_MENU_CHOICE > startMenu.length - 1) START_MENU_CHOICE = 0;
			}
		}
	}
	
	private void DRAW_START_MENU(Graphics2D g, int drawPos) {
		if(ON_START_MENU) {
			for(int i = 0; i < startMenu.length; i++) {
				if(i == START_MENU_CHOICE) {
					g.setColor(Color.BLACK);
				}
				else {
					g.setColor(Color.RED);
				}
				g.drawString(startMenu[i], drawPos + 60, 140 + i * g.getFontMetrics().getHeight());
			}
		}
	}
	
	private void OPTIONS_MENU() {
		if(ON_OPTIONS_MENU) {
			if(KeyHandler.isPressed(KeyHandler.UP)) {
				AudioPlayer.play("menuoption");
				OPTIONS_MENU_CHOICE--;
				if(OPTIONS_MENU_CHOICE < 0) OPTIONS_MENU_CHOICE = optionsMenu.length - 1;
			}
			if(KeyHandler.isPressed(KeyHandler.DOWN)) {
				AudioPlayer.play("menuoption");
				OPTIONS_MENU_CHOICE++;
				if(OPTIONS_MENU_CHOICE > optionsMenu.length - 1) OPTIONS_MENU_CHOICE = 0;
			}
		}
	}
	
	private void DRAW_OPTIONS_MENU(Graphics2D g, int drawPos) {
		if(ON_OPTIONS_MENU) {
			for(int i = 0; i < optionsMenu.length; i++) {
				if(i == OPTIONS_MENU_CHOICE) {
					g.setColor(Color.BLACK);
				}
				else {
					g.setColor(Color.RED);
				}
				g.drawString(optionsMenu[i], drawPos + 60, 140 + i * g.getFontMetrics().getHeight());
			}
		}
	}
	
	private String readoutHelpDocs(String name) {
		StringBuffer sb = new StringBuffer("");
		try {
			InputStream in = getClass().getResourceAsStream(name);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			br.close();
		} catch (Exception e) { }
		return sb.toString();
	}
	
	/*
	 * Junk
	 */

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