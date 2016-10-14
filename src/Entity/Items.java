package Entity;

import java.awt.image.BufferedImage;

import Items.ItemTypes;

public abstract class Items extends MapObject {

	protected int id;
	protected int damage;
	protected int strength;
	protected int blocking;
	protected int mana;
	protected int heal;
	protected BufferedImage image;
	protected String name;
	protected String description;
	protected int price;
	
	public abstract ItemTypes TYPE();
	public abstract int getID();
	public abstract BufferedImage getImage();
	public abstract int getHeal();
	public abstract int getStrength();
	public abstract int getMana();
	public abstract int getStamina();
	public abstract int getDefense();
	public abstract int getBlock();
	public abstract int getDamage();
	public abstract String getName();
	public abstract int getPrice();
	public abstract String getDescription();
	public abstract void doSpecial();
	
}
