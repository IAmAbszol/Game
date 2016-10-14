package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Handlers.ContentHandler;
import Handlers.KeyHandler;
import Main.GamePanel;
import Time.Timer;

public class DialogueBox {
	
	private HashMap<String, Color> colors;
	private static ArrayList<String[]> colorPoints;
	private static int totalLength;
	
	private static ArrayList<String> dialogueStrings;
	private static int boxPosition;
	
	private static boolean setupText;
	private static String readinText;
	private static boolean centered;
	
	private static String readoutText;
	public static boolean readOut;
	private static boolean drawingOut;
	
	private static long elapsed;
	private static long timer;
	// if true, game doesn't pause and db goes away based on time
	public static boolean doTimer;
	
	// dimensions
	private static int DialogueWidth;
	private static int DialogueHeight;
	private static int DialogueDrawX;
	private static int DialogueDrawY;
	private static int DialogueStartX;
	private static int DialogueStartY;
	private static int buffer;
	
	// fonts - Future, font will rely on programs own custom font
	private static Color dialogueOutlineColor;
	private static String dialogueTypeFont;
	private static Font dialogueFont;
	private static Color dialogueBoxColor;
	private static int dialogueSize;
	private static Color dialogueTextColor;
	private static int minFontSize = 4;
	
	// animation stuff
	private double delay;
	private static StringBuffer animationString;
	private static boolean playedOnce;
	private static int position;
	private static long startTime;
	private static boolean animate;
	
	// drawing animation stuff, courtesy of the playerscreen class I wrote
	private static boolean rotateUp;
	public static boolean rotateDown;
	private static int moveSpeed;
	private static int positionSpot;
	
	private int lineWidth = 0;
	
	public DialogueBox() {
		
		colors = new HashMap<String, Color>();
		buildColors();
		colorPoints = new ArrayList<String[]>();
		totalLength = 0;
		
		// if there's multiple parts the player has to press enter
		dialogueStrings = new ArrayList<String>();
		boxPosition = 0;
		
		setupText = false;
		readinText = "";
		centered = false;
		
		readoutText = "";
		readOut = false;
		drawingOut = false;
		
		buffer = 5;
		
		// width and height of the box
		DialogueWidth = GamePanel.WIDTH / 2;
		DialogueHeight = GamePanel.HEIGHT / 3; // may change to 4
		
		// starting position
		DialogueDrawX = DialogueWidth / 2;
		DialogueDrawY = DialogueHeight * 2 - 5;
		
		// where the text starts to get read out
		DialogueStartX = DialogueDrawX + buffer;
		DialogueStartY = DialogueDrawY + buffer;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(ContentHandler.playerFont);
		dialogueTypeFont = "Arial"; // change later
		dialogueTextColor = Color.white;
		dialogueBoxColor = new Color(0,0,0,.8f);
		dialogueSize = 10;
		dialogueFont = ContentHandler.playerFont.deriveFont((float)dialogueSize).deriveFont(Font.PLAIN);
		dialogueOutlineColor = Color.white;
		
		// animation stuff
		delay = .1;
		animationString = new StringBuffer("");
		animate = false;
		
		elapsed = 0;
		timer = 0;
		doTimer = false;
		
		rotateUp = false;
		rotateDown = false;
		moveSpeed = 10;
		positionSpot = GamePanel.HEIGHT;
		
	}
	
	public void update() {
		
		handleInput();
		
		doTextAnimation(); 
		
		if(Timer.getDay()) {
			dialogueBoxColor = new Color(0,0,0,.8f);
		} else {
			dialogueBoxColor = new Color(0,0,0, 1f);
		}
		
		if(animate) {
			if(playedOnce) {
				drawingOut = false;
			}
		} else
			drawingOut = false;
		
		if(!drawingOut && doTimer && !rotateUp && !rotateDown) {
			elapsed++;
			if(elapsed >= timer) {
				rotateDown = true;
				timer = 0;
				elapsed = 0;
				doTimer = false;
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		readToDialogueBox(g);
		
		if(readOut && !rotateUp && !rotateDown) {
			
			g.setColor(dialogueBoxColor);
			g.setFont(dialogueFont);
			g.fillRect(DialogueDrawX, DialogueDrawY, DialogueWidth, DialogueHeight);
			
			g.setColor(dialogueOutlineColor);
			g.drawRect(DialogueDrawX, DialogueDrawY, DialogueWidth, DialogueHeight);
			
			g.setColor(dialogueTextColor);
			if(readOut && !centered) {
				int tmp = DialogueStartY;
				if(!animate) {
					g.setColor(dialogueTextColor);
					for (String line : readoutText.split("\n")) {
				       adjustColor(g, line, DialogueStartX, tmp += g.getFontMetrics().getHeight());
					}
					totalLength = 0;
				} else {
					if(!animationString.equals("")) {
						g.setColor(dialogueTextColor);
						for (String line : animationString.toString().split("\n")) {
							adjustColor(g, line, DialogueStartX, tmp += g.getFontMetrics().getHeight());
						}
					       totalLength = 0;
					}
				}
			} else 
				if(readOut && centered) {
					int center = DialogueWidth / 2;
					int trueCenterX = DialogueDrawX + Math.abs(center - (g.getFontMetrics().stringWidth(readoutText) / 2));
					center = DialogueHeight / 2;
					int trueCenterY = DialogueDrawY + Math.abs(center - (g.getFontMetrics().getHeight() / 2));
					int tmp = trueCenterY;
					if(!animate) {
						g.setColor(dialogueTextColor);
						for (String line : readoutText.split("\n")) {
							adjustColor(g, line, trueCenterX, tmp += g.getFontMetrics().getHeight());
						}
						 totalLength = 0;
					} else {
						if(!animationString.equals("")) {
							g.setColor(dialogueTextColor);
							for (String line : animationString.toString().split("\n"))
								adjustColor(g, line, trueCenterX, tmp += g.getFontMetrics().getHeight());
						       totalLength = 0;
						}
					}
				}
		}
		
		if(rotateUp) {
			if(positionSpot > DialogueDrawY) {
				positionSpot += -moveSpeed;
			} else {
				positionSpot = DialogueDrawY;
				rotateUp = false;
			}
			g.setColor(dialogueBoxColor);
			g.setFont(dialogueFont);
			g.fillRect(DialogueDrawX, positionSpot, DialogueWidth, DialogueHeight);
			
			g.setColor(dialogueOutlineColor);
			g.drawRect(DialogueDrawX, positionSpot, DialogueWidth, DialogueHeight);
		}
		
		if(rotateDown) {
			readOut = false;
			if(positionSpot < GamePanel.HEIGHT) {
				positionSpot += moveSpeed;
			}
			g.setColor(dialogueBoxColor);
			g.setFont(dialogueFont);
			g.fillRect(DialogueDrawX, positionSpot, DialogueWidth, DialogueHeight);
			
			g.setColor(dialogueOutlineColor);
			g.drawRect(DialogueDrawX, positionSpot, DialogueWidth, DialogueHeight);
			
			int tmp = positionSpot;
			g.setColor(dialogueTextColor);
			for (String line : readoutText.split("\n")) {
				adjustColor(g, line, DialogueStartX, tmp += g.getFontMetrics().getHeight());
			}
			totalLength = 0;
		}
		
	}
	
	public void handleInput() {
		if(!drawingOut && !doTimer) {
			if(KeyHandler.isPressed(KeyHandler.INTERACT)) {
				boxPosition++;
				if(boxPosition < dialogueStrings.size()) {
					setupString(dialogueStrings.get(boxPosition));
				} else {
					rotateDown = true;
				}
				KeyHandler.keyState[KeyHandler.INTERACT] = false;
			}
		}
	}
	
	/*
	 * Special functions, adding and reading
	 */
	
	private void doTextAnimation() {
		if(animate && !playedOnce && !rotateUp && !rotateDown) {
			if(delay == -1) return;
			
			long elapsed = (System.nanoTime() - startTime) / 1000000;
			if(elapsed > delay) {
				animationString.append(readoutText.charAt(position));
				position++;
				startTime = System.nanoTime();
			}
			if(position >= readoutText.length()) {
				position = 0;
				playedOnce = true;
			}
		}
	}
	
	private static void setAnimation() {
		animationString = null;
		animationString = new StringBuffer("");
		position = 0;
		startTime = System.nanoTime();
		playedOnce = false;
		animate = true;
	}
	
	public static void setupCustomBox(ArrayList<String> text, int size, Color textColor, Color outlineColor, boolean animation, int x, int y, int width, int height) {
		if(x >= 0 || y >= 0) {
			DialogueDrawX = x;
			DialogueDrawY = y;
			DialogueStartX = x + buffer;
			DialogueStartY = y + buffer;
		}
		if(width >= 0 || height >= 0) {
			DialogueWidth = width;
			DialogueHeight = height;
		}
		if(size >= minFontSize) {
			dialogueSize = size;
		} else
			dialogueSize = minFontSize;
		if(textColor != null) {
			dialogueTextColor = textColor;
		}
		if(outlineColor != null) {
			dialogueOutlineColor = outlineColor;
		}
		animate = animation;
		dialogueStrings = text;
		boxPosition = 0;
		dialogueFont = ContentHandler.playerFont.deriveFont((float)dialogueSize).deriveFont(Font.PLAIN);
		doTimer = false;
		rotateUp = true;
		rotateDown = false;
		positionSpot = GamePanel.HEIGHT;
		setupString(dialogueStrings.get(boxPosition));
	}
	
	public static void setupBox(ArrayList<String> text, long time, int size, Color textColor, Color outlineColor, boolean animation) {
		if(size >= minFontSize) {
			dialogueSize = size;
		} else
			dialogueSize = minFontSize;
		if(textColor != null) {
			dialogueTextColor = textColor;
		}
		if(outlineColor != null) {
			dialogueOutlineColor = outlineColor;
		}
		animate = animation;
		dialogueStrings = text;
		boxPosition = 0;
		dialogueFont = ContentHandler.playerFont.deriveFont((float)dialogueSize).deriveFont(Font.PLAIN);
		timer = time;
		elapsed = 0;
		doTimer = true;
		rotateUp = true;
		rotateDown = false;
		positionSpot = GamePanel.HEIGHT;
		setupString(dialogueStrings.get(boxPosition));
	}
	
	public static void setupBox(ArrayList<String> text, int size, Color textColor, Color outlineColor, boolean animation) {
		if(size >= minFontSize) {
			dialogueSize = size;
		} else
			dialogueSize = minFontSize;
		if(textColor != null) {
			dialogueTextColor = textColor;
		}
		if(outlineColor != null) {
			dialogueOutlineColor = outlineColor;
		}
		animate = animation;
		dialogueStrings = text;
		boxPosition = 0;
		dialogueFont = ContentHandler.playerFont.deriveFont((float)dialogueSize).deriveFont(Font.PLAIN);
		doTimer = false;
		rotateUp = true;
		rotateDown = false;
		positionSpot = GamePanel.HEIGHT;
		setupString(dialogueStrings.get(boxPosition));
	}
	
	private static void setupString(String s) {
		if(animate) {
			setAnimation();
		}
		if(s.contains("centerdb:")) {
			centered = true;
			s = s.replace("centerdb:", "");
		} else
			centered = false;
		if(s.contains("bolddb:")) {
			dialogueFont = ContentHandler.playerFont.deriveFont((float)dialogueSize).deriveFont(Font.BOLD);
			s = s.replace("bolddb:", "");
		}
		totalLength = 0;
		colorPoints.removeAll(colorPoints); // probably will error
		setupText = true;
		readinText = s;
		readOut = true;
		drawingOut = false;
	}
	
	private void readToDialogueBox(Graphics dialogueGraphics) {
		if(setupText) {
			int width = 0;
			String finalString = "";
			String str = "";
			dialogueGraphics.setFont(dialogueFont);
			readinText = savePositions(readinText);			// remove all colors but keep the positions to occur
			for(int i = 0; i < readinText.length(); i++) {
				boolean space = false;
				String tmp = "" + readinText.charAt(i);
				char tmpc = readinText.charAt(i);
				if(tmpc == ' ') {
					space = true;
				} else
					space = false;
				str = str + readinText.charAt(i);
				width += dialogueGraphics.getFontMetrics().stringWidth(tmp);
				if(tmp.equals("\n")) width = 0;
				if(width >= DialogueWidth - buffer * 2) {
					if(space) {
						finalString = finalString + str + "\n";
						str = "";
						width = 0;
					} else {
						String tmps = rebuild(str, str.length() - 1, dialogueGraphics);
						finalString = finalString + tmps;
						str = "";
						width = lineWidth;
					}
				}
			}
			// add leftover string
			readoutText = finalString + str;
			readjustPoints(readoutText, readinText);
			readOut = true;
			drawingOut = true;
			setupText = false;
			readinText = "";
		}
	}
	
	private String rebuild(String s, int pos, Graphics g) {
		for(int i = pos; i > 0; i--) {
			char tmp = s.charAt(i);
			if(tmp == ' ') {
				// take the current string
				StringBuffer sb = new StringBuffer(s);
				StringBuffer sb2 = new StringBuffer("");
				// remove the space
				sb.replace(i, i+1, "");
				sb.insert(i, "\n");
				for(int k = pos; k > i; k--) {
					sb2.append(sb.charAt(k));
				}
				lineWidth = g.getFontMetrics().stringWidth(sb2.toString());
				return sb.toString();
			}
		}
		return "";
	}
	// new string should infact be longer due to the adjusted new line characters presented
	private static void readjustPoints(String newString, String oldString) {
		for(int i = 0; i < newString.length(); i++) {
			
			if(i >= oldString.length()) break;
			
			if(newString.charAt(i) != oldString.charAt(i) || newString.charAt(i) == '\n') {
				int position = i;
				for(int j = 0; j < colorPoints.size(); j++) {
					// may have to adjust the equals
					if(Integer.parseInt(colorPoints.get(j)[0]) > position) {
						int increment =  Integer.parseInt(colorPoints.get(j)[0]);
						increment--;
						colorPoints.get(j)[0] = "" + increment;
					}
				}
			}
			
		}
		
	}
	
	// loads color values and positions of color values for db
	// also returns the string of all color values erased from the string and stored in memory
	private static String savePositions(String s) {
		int hitPosition = 0;
		int offset = 0;							// when invoking i, the i is still tagged onto @red@ therefore, the position will be slightly altered upon reading
		String build = "";
		String color = "";
		boolean readColor = false;
		boolean doColor = false;
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == '@') {
				if(!readColor && !doColor) {
					
					hitPosition = i - offset;	// should log the position of where to insert
					color = color + s.charAt(i);
					readColor = true;
				} else
					if(readColor && !doColor) {
						color = color + s.charAt(i);
						offset += color.length();
						doColor = true;
					}
			} else
				if(!readColor) {
					build = build + s.charAt(i);
				} else {
					color = color + s.charAt(i);
				}
			
			if(readColor && doColor) {
				readColor = doColor = false;
				String[] string = { "" + hitPosition, color };
				colorPoints.add(string);
				color = "";
			}
			
		}
		
		return build;
		
	}
	/*
	 * Fix offset problem with the silly \n character
	 */
	private void adjustColor(Graphics2D g, String s, int x, int y) {
		int newX = x;
		for(int i = 0; i < s.length(); i++) {
			for(int j = 0; j < colorPoints.size(); j++) {
				if((i + totalLength) == Integer.parseInt(colorPoints.get(j)[0])) {
					if(colors.get(colorPoints.get(j)[1]) != null) {
						g.setColor(colors.get(colorPoints.get(j)[1]));
					}
				}
			}
			g.drawString("" + s.charAt(i), newX, y);
			newX += g.getFontMetrics().stringWidth(""+s.charAt(i));
		}
		totalLength += s.length();
	}
	
	/*
	private void adjustColor(Graphics2D g, String s, int x, int y) {
		
		String build = "";
		String color = "";
		boolean readColor = false;
		boolean doColor = false;
		int newX = x;
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == '@') {
				if(!readColor && !doColor) {
					color = color + s.charAt(i);
					readColor = true;
				} else
					if(readColor && !doColor) {
						color = color + s.charAt(i);
						doColor = true;
					}
			} else
				if(!readColor) {
					build = build + s.charAt(i);
				}
				else {
					color = color + s.charAt(i);
				}
			
			if(!readColor) {
				g.drawString("" + s.charAt(i), newX, y);
				newX += g.getFontMetrics().stringWidth(""+s.charAt(i));
			}
			
			if(readColor && doColor) {
				readColor = doColor = false;
				if(colors.get(color) != null) {
					g.setColor(colors.get(color));
				}
				color = "";
			}
			
		}
		
	}
	*/
	
	private void buildColors() {
		colors.put("@red@", Color.red);
		colors.put("@blue@", Color.blue);
		colors.put("@black@", Color.black);
		colors.put("@yellow@", Color.yellow);
		colors.put("@gray@", Color.gray);
		colors.put("@pink@", Color.pink);
		colors.put("@white@", Color.white);
		colors.put("@reset@", dialogueTextColor);
	}
	
	public static void setMoveSpeed(int speed) {
		moveSpeed = speed;
	}
	
	public static void resetMoveSpeed() {
		moveSpeed = 10;
	}

}
