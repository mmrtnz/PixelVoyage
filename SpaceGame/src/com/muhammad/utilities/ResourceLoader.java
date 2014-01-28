package com.muhammad.utilities;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * Loads images, sound, etc. A URL using getResource() will work in a jar file
 * whereas an InputStream using getResourceAsStream() will not.
 */
final public class ResourceLoader
{
	public static Image loadVisualGif(String path)
	{
		URL url = ResourceLoader.class.getResource(path);
		Image img = Toolkit.getDefaultToolkit().getImage(url);
		
		//Doesn't really work :( add the "/" manually.
		if(img == null)
		{
			img = Toolkit.getDefaultToolkit().getImage("/"+url);
			return img;
		}
		return img;
	}
	
	public static Image loadVisual(String path)
	{
		InputStream input = ResourceLoader.class.getResourceAsStream(path);
		if(input == null)
			input = ResourceLoader.class.getResourceAsStream("/"+path);
		try
		{
			Image img = ImageIO.read(input);
			return img;
		}catch(IOException e)
		{
			System.err.println("Unable to load "+path);
			return null;
		}
	}
	
	
	public static AudioInputStream loadAudio(String path) throws IOException, UnsupportedAudioFileException
	{
		URL input = ResourceLoader.class.getResource(path);
		if(input == null)
			input = ResourceLoader.class.getResource("/"+path);

		AudioInputStream inputStream = AudioSystem.getAudioInputStream(input);

		return inputStream;
	}
}
