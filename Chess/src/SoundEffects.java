import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import src.main.java.com.adonax.audiocue.AudioCue;

// Class: SoundEffects
// Written by: Ethan Frank
// Date: Dec 5, 2017
// Description: Holds a specifc sound and has a method for playing it
public enum SoundEffects {
	ERROR("sounds/error.wav");
	
	private AudioCue myAudioCue;	// Each sound effect gets it's own clip
	private int handle;
	
	//Constructor
	SoundEffects(String soundFileName){
		URL url = this.getClass().getResource(soundFileName);
		try {
			myAudioCue = AudioCue.makeStereoCue(url, 1);
			myAudioCue.open();  // see API for parameters to override "sound thread" configuration default
			handle = myAudioCue.obtainInstance();
			myAudioCue.setVolume(handle, 1);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //allows 4 concurrent
 catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Method: play
	// Description: Plays a sound effect. Restarts if sound effect already playing
	// Params: none
	// Returns: void
	public void play(){
		
		if(myAudioCue.getIsPlaying(handle)){
			myAudioCue.stop(handle);
		}
			myAudioCue.setFramePosition(handle, 0);
			myAudioCue.start(handle);
	}
}
