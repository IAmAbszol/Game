package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entity.Player.Player;
import Entity.Player.Interfaces.PlayerScreen;

public abstract class Screen extends PlayerScreen {

	protected BufferedImage screenImage;
	protected Graphics2D sg;
	protected Color color;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected int buffer;
	
	public Screen(Player p, int edges) {
		super(p);
	}
	
	public abstract void update();
	public abstract void controller();
	public abstract BufferedImage getScreenImage();
	
	public Color getColor() {
		return color;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int i) {
		x = i;
	}
	
	public void setY(int i) {
		y = i;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int i) {
		width = i;
	}
	
	public void setHeight(int i) {
		height = i;
	}
	
}
