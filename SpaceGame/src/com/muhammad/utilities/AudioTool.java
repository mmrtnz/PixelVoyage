package com.muhammad.utilities;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;

public final class AudioTool 
{
	public static Clip prevSong;
	public static boolean musicOn = true;
	public static boolean sfxOn = true;
	
	public static void playSFX(String type, String event)
	{
		if(sfxOn)
		{
			String parent = "/Audio/Gameplay/";
			try {
				Clip sound = AudioSystem.getClip();
				sound.open(ResourceLoader.loadAudio(parent+type+"-"+event+".au"));
				sound.start();
			} catch (Exception e) {
				System.err.println("Could not find/play" + type + "-" + event);
			}
		}
	}
	
	//Specific
	public static void playSFX(String s)
	{
		if(sfxOn)
		{
			try {
				Clip sound = AudioSystem.getClip();
				sound.open(ResourceLoader.loadAudio(s));
				sound.start();
			} catch (Exception e) {
				System.err.println("Could not find/play" + s);
			}
		}
	}
	
	public static void playSong(String s)
	{
		String parent = "/Audio/Music/";
		
/*		String bip = "bip.mp3";
		Media hit = new Media(bip);
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
*/		
		if(musicOn)
		{
			if(prevSong != null)
				prevSong.stop();
			try {
				Clip song = AudioSystem.getClip();
				song.open(ResourceLoader.loadAudio(parent+s+".au"));
				song.loop(Clip.LOOP_CONTINUOUSLY);
				prevSong = song;
			} catch (Exception e) {
				System.err.println("Could not find/play" + s);
			}
		}
	}
}
