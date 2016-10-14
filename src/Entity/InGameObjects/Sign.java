package Entity.InGameObjects;

import java.awt.image.BufferedImage;

import Entity.Objects;
import TileMap.TileMap;

public class Sign extends Objects {

	private BufferedImage[] sign;
	
	private String objectName;
	
	public Sign(TileMap tm) {
		super(tm);

		width = 32;
		height = 32;
		cwidth = 16;
		cheight = 16;
		coffsetx = 0;
		coffsety = 
	
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getObjectName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setObjectName(String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getObjectDescription() {
		return objectDescription;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isListening() {
		// TODO Auto-generated method stub
		return false;
	}

}
