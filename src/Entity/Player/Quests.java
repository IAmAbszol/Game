package Entity.Player;

import java.io.Serializable;

import Entity.MapObject;
import TileMap.TileMap;

/*
 * Class that handles quests Hierarchy: MapObject -> Player -> This --> Handler (Contains Quests) --> Branch of Quests
 */
@SuppressWarnings("serial")
public abstract class Quests extends MapObject implements Serializable {

	protected boolean doDraw;
	protected boolean activated;
	protected boolean justActivated;
	protected boolean completed;
	protected String outputText; // When it becomes active and completed... Working on the draw right now
	public int count = 0;
	
	public String questName;
	public String questDescription;
	
	public Quests() {
		
	}
	
	public Quests(TileMap tm) {
		super(tm);
		
	}

	public abstract boolean isActivated();
	public abstract void setActivated(boolean b);
	public abstract boolean isCompleted();
	public abstract String getQuestName();
	public abstract String getQuestDescription();
	public abstract void setQuestName(String n);
	public abstract void setQuestDescription(String n);
	public abstract void setCompleted(boolean b);
	public abstract void doReward();
	public abstract void init();
	public abstract void updatePosition(double x, double y);
	public abstract void update();

}
