package Audio;

public class Volume {
	
	private double defaultVolume;
	private double currentVolume;
	private double maxVolume = 6; // Given
	
	public Volume() {
		defaultVolume = currentVolume = 0;
	}
	
	public Volume(double d) {
		defaultVolume = currentVolume = d;
	}
	
	public void setCurrentVolume(double d) {
		currentVolume = d;
	}
	
	public float getDefaultVolume() {
		return (float)defaultVolume;
	}
	
	public float getCurrentVolume() {
		return (float)currentVolume;
	}
	
	public float getMaxVolume() {
		return (float)maxVolume;
	}

}
