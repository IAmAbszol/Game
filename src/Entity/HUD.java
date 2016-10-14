package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Entity.Player.Player;
import Entity.Player.Quests;
import Entity.Player.Shield;
import Handlers.ContentHandler;
import Handlers.KeyHandler;

public class HUD {
	
	private Player p;
	private Shield s;
	
	private ArrayList<Quests> q;
	private ArrayList<Objects> objs;
	private ArrayList<NPC> npc;
	private BufferedImage image;
	private Font f;
	
	private String currentObject;
	private int objectId;
	private String currentNPC;
	private int npcId;
	private int drawDistance = 10;
	
	private long timer = 0;
	private boolean readSpell = false;
	private int offset = 20;
	
	public HUD(Player p, ArrayList<NPC> npc, ArrayList<Objects> objs) {
		this.p = p;
		this.objs = objs;
		this.q = Player.QUESTS;
		this.npc = npc;
		
		currentObject = "";
		objectId = 0;
		currentNPC = "";
		npcId = 0;
		
		// load hud image
	}
	
	private void drawNames(Graphics2D g) {
		for(int i = 0; i < npc.size(); i++) {
			if(p.mayInteract(npc.get(i), drawDistance)) {
				if(currentNPC.equals("")) {
					currentNPC = npc.get(i).getNPCName();
					npcId = i;
				}
			}
			if(!currentNPC.equals("")) {
				g.drawString(npc.get(npcId).getNPCName(), 0, g.getFontMetrics().getHeight());
			}
		}
		if(currentNPC.equals("")) {
			for(int i = 0; i < objs.size(); i++) {
				if(p.mayInteract(objs.get(i), drawDistance)) {
					if(currentObject.equals("")) {
						currentObject = objs.get(i).getObjectName();
						objectId = i;
					}
				} else {
					currentObject = "";
					objectId = 0;
				}
				if(!currentObject.equals("") && currentNPC.equals("")) {
					g.drawString(objs.get(objectId).getObjectName(), 0, g.getFontMetrics().getHeight());
				}
			}
		}
		for(int i = 0; i < npc.size(); i++) {
			if(npc.get(npcId).getNPCName().equals(currentNPC)) {
				if(p.mayInteract(npc.get(npcId), drawDistance)) {
					currentNPC = "";
					npcId = 0;
				}
			}
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.PLAIN, 14));
		drawNames(g);
		
		g.setColor(ContentHandler.outputColor);
		g.setFont(ContentHandler.outputFont);
		if(p.spell != null) {
			readSpell = true;
		}
		if(timer < p.getTTW() && readSpell) {
			timer++;
			g.drawString(p.spell.getSpellOutburst(), (p.getx() + p.getMapX()) - g.getFontMetrics().stringWidth(p.spell.getSpellOutburst()) / 2, p.gety() + p.getMapY() - offset);
		} else {
			readSpell = false;
			timer = 0;
			p.spell = null;
		}
	}

}
