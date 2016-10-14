package Time;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;

public class Timer {
	
	public boolean doTime;
	
	private long currentTime;
	private long entireTime;
	private static boolean day;
	private int phase;
	
	// dark and light emulation
	private long emulationChange;
	private float alpha; 
	private AlphaComposite alcom;
	
	public Timer() {
		doTime = true;
		currentTime = 0;
		entireTime = 20000; // Total time = 20 mins
		phase = 0;
		day = true;
		alpha = 0.1f;
		emulationChange = 30;
	}
	
	/*
	 * @param 
	 * Time to be set based on SECONDS converted into game time
	 * 60 our time (1hr) is 
	 * IRL Day 86400
	 * Game Day / 50 of irl
	 */
	public void setTime(long i) {
		currentTime = i % entireTime;
	}
	
	public long getTime() {
		return currentTime;
	}
	
	public static boolean getDay() {
		return day;
	}
	
	public long getEntireTime() {
		return entireTime;
	}
	
	public int getPhase() {
		return phase;
	}
	
	public long getEmulationTime() {
		return emulationChange;
	}
	
	public void setEmulationTime(long emulationChange) {
		this.emulationChange = emulationChange;
	}
	
	public void setTimeEmulation(boolean b) {
		doTime = b;
	}

	// controls layer manipulation
	public void lightControl() {
		if(currentTime != 0) {
			if(currentTime % emulationChange == 0) {
				if(phase < 5 && !day) {
					phase++;
					alpha = phase * 0.1f;
				} else
					if(phase > 0 && day) {
						phase--;
						alpha = phase * 0.1f;
					}
			}
		}
		if(phase > 5) phase = 5;
		if(phase < 0) phase = 0;
	}
	
	public void update() {
		if(doTime) {
			currentTime++;
			if(day && currentTime >= entireTime / 2) {
				System.out.println("here " + currentTime);
				day = false;
			} else 
				if(!day && currentTime >= entireTime) {
					day = true;
					currentTime = 0;
			}
			lightControl();
		}
	}
	
	public void draw(Graphics2D g) {
		// set alpha
		alcom = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, alpha);
		
		g.setComposite(alcom);
		
		// set color
		Color color = new Color(0,0,0);
		g.setColor(color);
		
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		g.setComposite(AlphaComposite.SrcOver);
		
	}
	
}
