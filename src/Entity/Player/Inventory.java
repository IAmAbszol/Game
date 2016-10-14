package Entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

import Entity.Items;
import GameState.GameStateManager;

@SuppressWarnings("serial")
public class Inventory implements Serializable {

	public ArrayList<Slots> slot;
	
	public Inventory() {
		slot = new ArrayList<Slots>();
	}
	
	public void addItem(Items i) {
		Slots s = new Slots(i);
		slot.add(s);
	}
	
	// drop
	public void removeItem(int pos, int x, int y) {
		GameStateManager.grabState().addItemBasedTile(slot.get(pos).getItem(), x, y);
		slot.remove(pos);
	}
	
}
