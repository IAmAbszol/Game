package Audio;

import java.net.URL;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class AudioPlayer {
	
	private static HashMap<String, Clip> clips;
	private static HashMap<Clip, Volume> volumeControl;
	private static int gap;
	public static boolean mute = false;
	
	private static int volumeDistance = 50;
	
	public static void init() {
		clips = new HashMap<String, Clip>();
		volumeControl = new HashMap<Clip, Volume>();
		gap = 0;
	}
	
	public static void mute(String s, boolean doMute) {
		Clip clip = clips.get(s);
		BooleanControl muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
		muteControl.setValue(doMute);
	}
	
	// function that increases or decreases the volume based on player distance away
	// basically, closer it increases but the farther is decreases
	public static void adjustDistanceVolume(HashMap<String, Clip> audio, String s, double farthestDistance, double currentDistance) {
		Clip clip;
		double recordingDistance = 0;
		
		clip = audio.get(s);
		
		BooleanControl muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);

		if(currentDistance <= farthestDistance) {
			
			// unmute
			muteControl.setValue(false);
			
			// restore audio
			volumeControl.get(clip).setCurrentVolume(volumeControl.get(clip).getDefaultVolume());
			
			// possible solution
			if(currentDistance <= volumeDistance) {
				recordingDistance = 0;
			} else
				recordingDistance = Math.abs(currentDistance);
			
			double noiseController = recordingDistance * .1;
			if(noiseController < -80) noiseController = -80; 
			
			// Audio controller now works, simply write the code that ups it just a little bit
			// Also mute all objects on boot, that annoying clip is playing still
			// finally, may make it so the audio cuts out after 200 but sounds like it simply died out from hearing it
			
			double temp = volumeControl.get(clip).getDefaultVolume() - noiseController;
			
			if(temp > volumeControl.get(clip).getMaxVolume()) {
				volumeControl.get(clip).setCurrentVolume(volumeControl.get(clip).getMaxVolume());
			} else
				volumeControl.get(clip).setCurrentVolume(temp);
			
			adjustVolume(audio, s, (float)volumeControl.get(clip).getCurrentVolume());
			
		} else
			muteControl.setValue(true);
		
	}
	
	public static void resetVolume(String s) {
		Clip clip;
		clip = clips.get(s);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volumeControl.get(clip).getDefaultVolume());
	}
	
	// function that adjusts volume of clip - 0 is no gain or loss, negative is reduce, positive is increased
	public static void adjustVolume(String s, float i) {
		Clip clip;
		clip = clips.get(s);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(i);
	}
	
	// function that adjusts the volume of the entire game, adjustVolume(param, param) can counter this
	// Audio has to be init and loaded inside Content Handler before Menu for this to work though.
	public static void adjustSystemVolume(float i) {
		Clip clip;
		for(int k = 0; k < clips.size(); k++) {
			clip = clips.get(k);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(i);
		}
	}
	
	public static void load(String s, String n, float volume) {
		if(clips.get(n) != null) return;
		Clip clip;
		try {			
			URL url = AudioPlayer.class.getResource(s);
	        AudioInputStream ais;
	        ais = AudioSystem.getAudioInputStream(url);
			AudioFormat baseFormat = ais.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class,  baseFormat);
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(ais);
			clips.put(n, clip);
			Volume vol = new Volume(volume);
			volumeControl.put(clip, vol);
			adjustVolume(n, volumeControl.get(clip).getDefaultVolume());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void play(String s) {
		play(s, gap);
	}
	
	public static void play(String s, int i) {
		if(mute) return;
		Clip c = clips.get(s);
		if(c == null) return;
		if(c.isRunning()) c.stop();
		c.setFramePosition(i);
		while(!c.isRunning()) c.start();
	}
	
	public static void stop(String s) {
		if(clips.get(s) == null) return;
		if(clips.get(s).isRunning()) clips.get(s).stop();
	}
	
	public static void resume(String s) {
		if(mute) return;
		if(clips.get(s).isRunning()) return;
		clips.get(s).start();
	}
	
	public static void loop(String s) {
		loop(s, gap, gap, clips.get(s).getFrameLength() - 1);
	}
	
	public static void loop(String s, int frame) {
		loop(s, frame, gap, clips.get(s).getFrameLength() - 1);
	}
	
	public static void loop(String s, int start, int end) {
		loop(s, gap, start, end);
	}
	
	public static void loop(String s, int frame, int start, int end) {
		stop(s);
		if(mute) return;
		clips.get(s).setLoopPoints(start, end);
		clips.get(s).setFramePosition(frame);
		clips.get(s).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void setPosition(String s, int frame) {
		clips.get(s).setFramePosition(frame);
	}
	
	public static int getFrames(String s) { return clips.get(s).getFrameLength(); }
	public static int getPosition(String s) { return clips.get(s).getFramePosition(); }
	
	public static void close(String s) {
		stop(s);
		clips.get(s).close();
	}
	
	/*
	 * Personalized audio
	 */
	public static void resetVolume(HashMap<String, Clip> audio, String s) {
		Clip clip;
		clip = audio.get(s);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volumeControl.get(clip).getDefaultVolume());
	}
	public static void mute(HashMap<String, Clip> audio, String s, boolean doMute) {
		Clip clip = audio.get(s);
		BooleanControl muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
		muteControl.setValue(doMute);
	}
	
	public static void adjustVolume(HashMap<String, Clip> audio, String s, float i) {
		Clip clip;
		clip = audio.get(s);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		if(i < -80) i = -80;
		gainControl.setValue(i);
	}
	
	public static void load(HashMap<String, Clip> audio, String s, String n, float volume) {
		if(audio.get(n) != null) return;
		Clip clip;
		try {			
			URL url = AudioPlayer.class.getResource(s);
	        AudioInputStream ais;
	        ais = AudioSystem.getAudioInputStream(url);
			AudioFormat baseFormat = ais.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class,  baseFormat);
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(ais);
			audio.put(n, clip);
			Volume vol = new Volume(volume);
			volumeControl.put(clip, vol);
			adjustVolume(audio, n, volumeControl.get(clip).getDefaultVolume());
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void play(HashMap<String, Clip> audio, String s) {
		play(s, gap);
	}
	
	public static void play(HashMap<String, Clip> audio, String s, int i) {
		if(mute) return;
		Clip c = clips.get(s);
		if(c == null) return;
		if(c.isRunning()) c.stop();
		c.setFramePosition(i);
		while(!c.isRunning()) c.start();
	}
	
	public static void stop(HashMap<String, Clip> audio, String s) {
		if(audio.get(s) == null) return;
		if(audio.get(s).isRunning()) clips.get(s).stop();
	}
	
	public static void resume(HashMap<String, Clip> audio, String s) {
		if(mute) return;
		if(audio.get(s).isRunning()) return;
		audio.get(s).start();
	}
	
	public static void loop(HashMap<String, Clip> audio, String s) {
		loop(audio, s, gap, gap, audio.get(s).getFrameLength() - 1);
	}
	
	public static void loop(HashMap<String, Clip> audio, String s, int frame) {
		loop(audio, s, frame, gap, audio.get(s).getFrameLength() - 1);
	}
	
	public static void loop(HashMap<String, Clip> audio, String s, int start, int end) {
		loop(audio, s, gap, start, end);
	}
	
	public static void loop(HashMap<String, Clip> audio, String s, int frame, int start, int end) {
		stop(s);
		if(mute) return;
		audio.get(s).setLoopPoints(start, end);
		audio.get(s).setFramePosition(frame);
		audio.get(s).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void setPosition(HashMap<String, Clip> audio, String s, int frame) {
		audio.get(s).setFramePosition(frame);
	}
	
	public static int getFrames(HashMap<String, Clip> audio, String s) { return audio.get(s).getFrameLength(); }
	public static int getPosition(HashMap<String, Clip> audio, String s) { return audio.get(s).getFramePosition(); }
	
	public static void close(HashMap<String, Clip> audio, String s) {
		stop(s);
		audio.get(s).close();
	}
	
}
