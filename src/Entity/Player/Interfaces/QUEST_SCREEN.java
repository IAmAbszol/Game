package Entity.Player.Interfaces;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Entity.Screen;
import Entity.Player.Player;
import Entity.Player.Quests;
import Handlers.ContentHandler;
import Main.GamePanel;

public class QUEST_SCREEN extends Screen {
	
	/*
	 * When init, run through Player.q.size to getName and print out into a text box
	 * Figure out scrolling text box, own function? Or just use JTextArea for that spot then remove.
	 * 
	 * When done with this class/player presses enter to leave again, null up everything
	 */
	
	private ArrayList<Quests> quest;
	
	// bounds
	private final int DEFAULT_Y = buffer;
	private int startY = 0;					// used to manipulate tmpy positions
	private int position;
	
	public QUEST_SCREEN(Player p, int edges) {
		super(p, edges);
		color = Color.blue;
		screenImage = new BufferedImage(GamePanel.WIDTH - edges * 2,
									GamePanel.HEIGHT - edges * 2,
									BufferedImage.TYPE_INT_RGB);
		sg = screenImage.createGraphics();				// linked graphcs to the screenImage
		buffer = 5;
		quest = Player.QUESTS;
		position = 0;
	}

	public void update() {
		
	}
	
	public void drawImage() {
		sg.setFont(ContentHandler.outputFont);
		int tmpy = DEFAULT_Y - startY + sg.getFontMetrics().getHeight();
		for(int i = 0; i < quest.size(); i++) {
			if(i == position) 
				sg.setColor(Color.white);
			else 
				sg.setColor(Color.red);
			if(quest.get(i).isCompleted())
				sg.drawString("Completed: " + quest.get(i).getQuestName(), buffer, tmpy);
			else
				sg.drawString("Active: " + quest.get(i).getQuestName(), buffer, tmpy);
			
			tmpy += sg.getFontMetrics().getHeight();
		}
	
	}

	@Override
	public void controller() {
		// handle input
	}

	@Override
	public BufferedImage getScreenImage() {
		drawImage();
		return screenImage;
	}

}
