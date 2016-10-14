package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import Entity.Animation;
import Gif.GifDecoder;
import Main.GamePanel;

public class Background {
	
	Animation an;
	
	private BufferedImage image;
	private BufferedImage[] imageAnimation;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	private boolean animate;
	private boolean fit;
	
	String simage;
	
	public Background(String s, double ms, boolean animate, int delay) {
		
		this.animate = animate;
		an = new Animation();
		
		simage = s;
		fit = false;
		
		if(this.animate) {
			spliceImages();
			an.setFrames(imageAnimation);
			an.setDelay(delay);
		}
		
		try {
			image = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			moveScale = ms;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void spliceImages() {
		GifDecoder d = new GifDecoder();
		InputStream resourceStream = getClass().getResourceAsStream(simage);
		InputStream buffer = new BufferedInputStream(resourceStream);
		d.read(buffer); // somehow link it to resources, classpath
		int n = d.getFrameCount();
		imageAnimation = new BufferedImage[n];
		for(int i = 0; i < n; i++) {
			imageAnimation[i] = d.getFrame(i);
		}
	}
	
	public void fitBackground(boolean p) { fit = p; }
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
		if(animate) an.update();
	}
	
	public void draw(Graphics2D g) {
		
		if(animate && fit) {
			g.drawImage(imageAnimation[an.getFrame()], (int)x, (int)y, GamePanel.WIDTH, GamePanel.HEIGHT, null);
		} else
			if(animate && !fit) {
				g.drawImage(imageAnimation[an.getFrame()], (int)x, (int)y, null);
			} else
				if(!animate && fit) {
					g.drawImage(image, (int)x, (int)y, GamePanel.WIDTH, GamePanel.HEIGHT, null);
				} else
					if(!(animate && fit)) {
						g.drawImage(image, (int)x, (int)y, null);
					}
		
		if(x < 0) {
			if(animate && fit) {
				g.drawImage(imageAnimation[an.getFrame()], (int)x + GamePanel.WIDTH, (int)y, GamePanel.WIDTH, GamePanel.HEIGHT, null);
			} else
				if(animate && !fit) {
					g.drawImage(imageAnimation[an.getFrame()], (int)x + GamePanel.WIDTH, (int)y, null);
				} else
					if(!animate && fit) {
						g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, GamePanel.WIDTH, GamePanel.HEIGHT, null);
					} else
						if(!(animate && fit)) {
							g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
						}
		}
		if(x > 0) {
			if(animate && fit) {
				g.drawImage(imageAnimation[an.getFrame()], (int)x - GamePanel.WIDTH, (int)y, GamePanel.WIDTH, GamePanel.HEIGHT, null);
			} else
				if(animate && !fit) {
					g.drawImage(imageAnimation[an.getFrame()], (int)x - GamePanel.WIDTH, (int)y, null);
				} else
					if(!animate && fit) {
						g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, GamePanel.WIDTH, GamePanel.HEIGHT, null);
					} else
						if(!(animate && fit)) {
							g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
						}
		}
	}
	
}