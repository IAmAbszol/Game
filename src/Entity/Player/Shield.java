package Entity.Player;

import java.awt.Color;
import java.awt.Graphics2D;

import TileMap.TileMap;

public class Shield extends Player {

	public Shield(TileMap tm) {
		super(tm);
		// TODO Auto-generated constructor stub
	}

	private void recover() {
		if(!isBlocking()) {
			if(getBlocking() < getMaxBlocking()) {
				setBlocking(getBlocking()+.2); // increment
			}
			if(getBlocking() > getMaxBlocking()) setBlocking(getMaxBlocking());
		}
	}
	
	private void doBlock() {
		if(isBlocking()) {
			if(getBlocking() > 0) {
				setBlocking(getBlocking()-.1);
			}
			if(getBlocking() < 0) setBlocking(0);
		}
	}
	
	public void update() {
		recover();
		doBlock();
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		g.fillOval(50,50,100,100);
		g.setColor(Color.white);
		g.fillArc(50, 50, 100, 100, 90, 0);
	}
	
}
