package Entity.Player.Quest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;

import Entity.Player.Player;
import Entity.Player.Quests;
import TileMap.TileMap;

@SuppressWarnings("serial")
public class VentureToUlrik extends Quests implements Serializable {
	
	// timer
	private long timer;
	
	private double x;
	private double y;
	
	public VentureToUlrik(TileMap tm) {
		super(tm);

		activated = false;
		justActivated = false;
		completed = false;
		doDraw = false;
		outputText = "";
		questName = "Venture To Ulrik";
		questDescription = "Head West and Enter the Town of Ulrik";
		
		timer = 0;
		
	}
	
	public void updatePosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void update() {
		if(activated) {
			if(completed) {
				doReward();
				doDraw = true;
				activated = false;
			}
		}
		if(activated && !justActivated) {
			doDraw = true;
			justActivated = true;
			outputText = "Added Quest: Venture To Ulrik!";
		}
		if(doDraw) {
			timer++;
			if(timer >= 120) {
				doDraw = false;
				timer = 0;
			}
		}
	}
	
	public void doReward() {
		
		outputText = "Completed Quest! Venture To Ulrik";
		Player.gold += 10;
		Player.xp += 5;
		
	}

	public boolean isCompleted() {
		return completed;
	}
	
	public void setCompleted(boolean b) {
		completed = b;
	}
	
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean b) {
		activated = b;
	}
	
	public String getQuestName() {
		return questName;
	}
	public String getQuestDescription() {
		return questDescription;
	}
	public void setQuestName(String n) {
		questName = n;
	}
	public void setQuestDescription(String n) {
		questDescription = n;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		if(doDraw) {
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.PLAIN, 10));
			g.drawString(outputText, (int)x, (int)y - 5);
		}
	}


}
