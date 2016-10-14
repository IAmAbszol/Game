package GameState;

import Audio.AudioPlayer;
import Entity.Player.Player;
import Handlers.ContentHandler;
import Main.GamePanel;
import Save.SaveState;

public class GameStateManager {
	
	public static GameState[] gameStates;
	public static int currentState;
	
	public static final int NUMGAMESTATES = 128;
	public static final int BOOT = 0;
	public static final int MENU = 1;
	public static final int NEO = 2;
	public static final int GRASSLANDS_OF_NEO = 3;
	
	// passing player
	public Player player;
	
	public static boolean load = false;
	
	public GameStateManager() {
		
		AudioPlayer.init();
		
		player = null;
		
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = BOOT;
		loadState(currentState, -1, -1);
		
	}
	
	public static void savePlayer(Player player) {
		SaveState s = new SaveState(player);
		s.saveGame();
	}
	
	public static GameState grabState() {
		return gameStates[currentState];
	}

	private void loadState(int state, int x, int y) {
		if(state == BOOT)
			gameStates[state] = new ContentHandler(this);
		else if(state == MENU)
			gameStates[state] = new Menu(this);
		else if(state == NEO) {
			gameStates[state] = new Neo(this,x,y);
		}
		else if(state == GRASSLANDS_OF_NEO) {
			gameStates[state] = new GrasslandsOfNeo(this,x,y);
		}
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state, int x, int y) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState,x,y);
	}
	
	public void update() {
		if(gameStates[currentState] != null) {
			gameStates[currentState].update();
		}
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(gameStates[currentState] != null) gameStates[currentState].draw(g);
		else {
			g.setColor(java.awt.Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}
	
}
