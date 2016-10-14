package Entity.Player;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import Entity.InventorySlot;
import Entity.Items;

@SuppressWarnings("serial")
public class Slots extends InventorySlot implements Serializable {

	private Items i;
	
	public Slots(Items i) {
		this.i = i;
		empty = false;
	}
	
	@Override
	public String getName() {
		return i.getName();
	}

	@Override
	public String getDescription() {
		return i.getDescription();
	}
	
	@Override
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Items getItem() {
		return i;
	}

	@Override
	public boolean isEmpty() {
		return empty;
	}
	
	public void useItem(boolean b) {
		// provide valid switch case checking
		switch(i.TYPE()) {
		
		case POTION:
			Player.health += i.getHeal();
			Player.strength += i.getStrength();
			Player.mana += i.getMana();
			Player.defense += i.getDefense();
			empty = true;
			break;
		case ARMOUR:
			
			break;
		case BOOK:
			break;
		case FOOD:
			Player.health += i.getHeal();
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
