package Handlers;

import java.awt.event.MouseEvent;

public class MouseHandler {
	
	private static final int NUM_BUTTONS = 2;
	
	// Buttons
	private static int LEFT_CLICK = 0;		// Interact
	private static int RIGHT_CLICK = 1;	// Destroy
	
	private static boolean mouseState[] = new boolean[NUM_BUTTONS];
	private static boolean prevMouseState[] = new boolean[NUM_BUTTONS];
	
	public static void getMouseAction(int i, boolean p) {
		if(i == MouseEvent.BUTTON1) {
			mouseState[LEFT_CLICK] = p;
		}
		if(i == MouseEvent.BUTTON2) {
			mouseState[RIGHT_CLICK] = p;
		}
	}
	
	public static void update() {
		for(int i = 0; i < NUM_BUTTONS; i++) {
			prevMouseState[i] = mouseState[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return mouseState[i] && !prevMouseState[i];
	}
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_BUTTONS; i++) {
			if(mouseState[i]) return true;
		}
		return false;
	}

}
