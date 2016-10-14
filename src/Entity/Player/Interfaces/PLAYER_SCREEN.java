package Entity.Player.Interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entity.Screen;
import Entity.Player.Player;
import Main.GamePanel;

public class PLAYER_SCREEN extends Screen {

	public PLAYER_SCREEN(Player p, int edges) {
		super(p, edges);
		color = Color.green;
		screenImage = new BufferedImage(GamePanel.WIDTH - edges * 2,
				GamePanel.HEIGHT - edges * 2,
				BufferedImage.TYPE_INT_RGB);
		sg = screenImage.createGraphics();				// linked graphcs to the screenImage
		buffer = 5;
	}
	
	public void update() {
		
	}
	
	public void drawImage() {
		sg.setColor(color);
		sg.fillRect(0, 0, screenImage.getWidth(), screenImage.getHeight());
	}

	@Override
	public void controller() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BufferedImage getScreenImage() {
		drawImage();
		return screenImage;
	}
	
}
