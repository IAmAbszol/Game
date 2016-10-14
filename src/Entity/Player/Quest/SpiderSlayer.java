package Entity.Player.Quest;

import java.io.Serializable;
import java.util.ArrayList;

import Entity.DialogueBox;
import Entity.Player.Player;
import Entity.Player.Quests;
import Handlers.ContentHandler;
import TileMap.TileMap;

@SuppressWarnings("serial")
public class SpiderSlayer extends Quests implements Serializable {

	// timer
	private long timer;
		
	private double x;
	private double y;
	
	public static int spiderCounter = 0;
	
	public SpiderSlayer(TileMap tm) {
		super(tm);

		activated = false;
		justActivated = false;
		completed = false;
		doDraw = false;
		outputText = "";
		questName = "Spider Slayer";
		questDescription = "The shop keeper has instructed me to clear out all the spiders in Neo.";
		
		timer = 0;
		spiderCounter = 0;
		
	}
	
	@Override
	public boolean isActivated() {
		return activated;
	}

	@Override
	public void setActivated(boolean b) {
		activated = b;
		
	}

	@Override
	public boolean isCompleted() {
		return completed;
	}

	@Override
	public String getQuestName() {
		return questName;
	}

	@Override
	public String getQuestDescription() {
		return questDescription;
	}

	@Override
	public void setQuestName(String n) {
		questName = n;
	}

	@Override
	public void setQuestDescription(String n) {
		questDescription = n;
	}

	@Override
	public void setCompleted(boolean b) {
		completed = b;
	}

	@Override
	public void doReward() {
		Player.gold += 20;
		Player.xp += 15;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void updatePosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void update() {
		if(activated) {
			if(completed) {
				justActivated = true;
				doReward();
				doDraw = true;
				activated = false;
			}
		}
		if(activated && !justActivated && !completed) {
			doDraw = true;
			justActivated = true;
		}
		if(doDraw) {
			timer++;
			if(timer >= 120) {
				doDraw = false;
				timer = 0;
			}
		}
		if(spiderCounter >= 2 && !completed) {
			completed = true;
			ArrayList<String> string = new ArrayList<String>();
			string.add("centerdb:Completed Quest: " + questName);
			DialogueBox.setupBox(string, 
					ContentHandler.NPC_TEXT_SIZE, 
					null, 
					null, true);
		}
	}
	
}
