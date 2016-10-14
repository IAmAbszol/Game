package Items;

import java.awt.image.BufferedImage;

import Entity.Items;
import Handlers.ContentHandler;

public class Sword extends Items {
	
	public Sword() {
		object = true;
		
		blocking = 0;
		damage = 5;
		id = 1;
		name = "Sword";
		price = 10;
		heal = 0;
		description = "Dull but useful.";
		image = ContentHandler.sword[0];
		width = image.getWidth();
		height = image.getHeight();
	}
	
	@Override
	public ItemTypes TYPE() {
		return ItemTypes.WEAPON;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public int getHeal() {
		return heal;
	}

	@Override
	public int getStrength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMana() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStamina() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDefense() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlock() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void doSpecial() {
		// TODO Auto-generated method stub
		
	}

}
