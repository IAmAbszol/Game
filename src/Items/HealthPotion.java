package Items;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import Entity.Items;
import Handlers.ContentHandler;

public class HealthPotion extends Items implements Serializable {

	private int heal;
	private String name;
	private int price;
	private String description;
	
	public HealthPotion() {
		// map object class
		object = true;
		
		blocking = 0;
		damage = 0;
		id = 0;
		name = "Potion of Health";
		price = 10;
		heal = 5;
		description = "Great for minor healing.";
		image = ContentHandler.potionOfHealth[0];
		width = image.getWidth();
		height = image.getHeight();
	}

	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public int getStamina() {
		return 0;
	}
	
	@Override
	public int getBlock() {
		return 0;
	}

	@Override
	public int getDamage() {
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
	public ItemTypes TYPE() {
		return ItemTypes.POTION;
	}

	@Override
	public int getHeal() {
		return heal;
	}

	@Override
	public int getStrength() {
		return 0;
	}

	@Override
	public int getMana() {
		return 0;
	}

	@Override
	public int getDefense() {
		return 0;
	}

	@Override
	public void doSpecial() {
		
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

}
