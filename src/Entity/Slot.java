package Entity;

import java.awt.image.BufferedImage;

import Entity.Player.Player;

public abstract class Slot {
	
	protected Items item;
	protected String name;
	protected int id;
	protected boolean empty;
	
	public abstract String getName();
	public abstract String getDescription();
	public abstract BufferedImage getImage();
	public abstract boolean isEmpty();

	public void useItem(boolean b) {
		// provide valid switch case checking
		switch(item.TYPE()) {
		
		case POTION:
			Player.health += item.getHeal();
			Player.strength += item.getStrength();
			Player.mana += item.getMana();
			Player.defense += item.getDefense();
			empty = true;
			break;
		case ARMOUR:
			
			break;
		case BOOK:
			break;
		case FOOD:
			Player.health += item.getHeal();
			break;
		case OTHER:
			break;
		case WEAPON:
			break;
		default:
			break;
		
		}
	}
}
