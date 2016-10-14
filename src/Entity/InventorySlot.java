package Entity;

import java.awt.image.BufferedImage;

public abstract class InventorySlot {

	public boolean empty;
	
	public abstract String getName();
	public abstract String getDescription();
	public abstract Items getItem();
	public abstract void useItem(boolean b);
	public abstract BufferedImage getImage();
	public abstract boolean isEmpty();
	
}
