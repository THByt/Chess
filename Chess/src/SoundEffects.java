import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

// Class: SoundEffects
// Written by: Ethan Frank
// Date: Dec 5, 2017
// Description: Holds a specifc sound and has a method for playing it
public enum SoundEffects {
	ERROR("sounds/error.wav");
	
	private Clip clip; //Each sound effect gets own clip.
	
	SoundEffects(String soundFileName) {
	      try {
	         // Use URL (instead of File) to read from disk and JAR.
	         URL url = this.getClass().getClassLoader().getResource(soundFileName);
	         // Set up an audio input stream piped from the sound file.
	         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
	         // Get a clip resource.
	         clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioInputStream);
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	   }
	
	   // Play or Re-play the sound effect from the beginning, by rewinding.
	   public void play() {
	         clip.setFramePosition(0); // rewind to the beginning
	         clip.start();     // Start playing
	   }
}
