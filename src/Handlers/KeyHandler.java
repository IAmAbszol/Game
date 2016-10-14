package Handlers;

import java.awt.event.KeyEvent;

public class KeyHandler {

	private static final int NUM_KEYS = 20;
	
	// KEYS
	public static int RIGHT			= 0;	 	// -> or d
	public static int LEFT 			= 1; 		// <- or a
	public static int UP 			= 2;		// up
	public static int DOWN 			= 3;		// down
	public static int INTERACT 		= 4;		// e
	public static int ENTER 		= 5; 		// return/enter
	public static int ATTACK 		= 6;		// r
	public static int CAST 			= 7;		// q
	public static int SAVE 			= 8;		// p
	public static int LOAD 			= 9;		// l
	public static int DASH 			= 10;		// shift + direction
	public static int BACK 			= 11;		// backspace
	public static int UP_K 			= 12;		// w letter
	public static int DOWN_K 		= 13;		// s letter
	public static int LEFT_K		= 14;		// a letter
	public static int RIGHT_K 		= 15;		// d letter
	public static int ESCAPE		= 16;		// esc
	public static int QUICK_USE 	= 17;		// z
	public static int SHIELD 		= 18;		// space
	
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];
	
	public static void getKeyAction(int i, boolean p) {
		if(i == KeyEvent.VK_RIGHT) {
			keyState[RIGHT] = p;
		} else
		if(i == KeyEvent.VK_LEFT) {
			keyState[LEFT] = p;
		} else
		if(i == KeyEvent.VK_UP) {
			keyState[UP] = p;
		} else
		if(i == KeyEvent.VK_DOWN) {
			keyState[DOWN] = p;
		} else
		if(i == KeyEvent.VK_E) {
			keyState[INTERACT] = p;
		} else
		if(i == KeyEvent.VK_ENTER) {
			keyState[ENTER] = p;
		} else
		if(i == KeyEvent.VK_R) {
			keyState[ATTACK] = p;
		} else
		if(i == KeyEvent.VK_Q) {
			keyState[CAST] = p;
		} else
		if(i == KeyEvent.VK_P) {
			keyState[SAVE] = p;
		} else
		if(i == KeyEvent.VK_L) {
			keyState[LOAD] = p;
		} else
		if(i == KeyEvent.VK_SHIFT) {
			keyState[DASH] = p;
		} else
		if(i == KeyEvent.VK_BACK_SPACE) {
			keyState[BACK] = p;
		} else
		if(i == KeyEvent.VK_W) {
			keyState[UP_K] = p;
		} else
		if(i == KeyEvent.VK_S) {
			keyState[DOWN_K] = p;
		} else
		if(i == KeyEvent.VK_A) {
			keyState[LEFT_K] = p;
		} else
		if(i == KeyEvent.VK_D) {
			keyState[RIGHT_K] = p;
		} else
		if(i == KeyEvent.VK_Z) {
			keyState[QUICK_USE] = p;
		} else
		if(i == KeyEvent.VK_SPACE) {
			keyState[SHIELD] = p;
		}
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) return true;
		}
		return false;
	}
	
	public static void resetKeys() {
		for(int i = 0; i < NUM_KEYS; i++) {
			keyState[i] = false;
		}
	}
	
}
