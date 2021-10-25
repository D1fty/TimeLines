import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import gameMenu.*;
import audioPlayer.*;

public class Main {

	/*
	 * Damien DeCourcy
	 * 
	 * This is my second ever Java program, and I made it in a three week deadline. So the code got really sloppy. 
	 * 
	 */
	public static void main(String[] args)
	{		
		// Draw loading screen	
		final JFrame loadSplash = new JFrame("Loading...");
		loadSplash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try 
		{
			loadSplash.setContentPane	(new JLabel(new ImageIcon(ImageIO.read(new File("src/images/menu/loadingSplash.png")))));
        } 
		catch (IOException e) 
		{
            e.printStackTrace();
        }
		loadSplash.setUndecorated		(true);
		loadSplash.pack					();
		loadSplash.setLocationRelativeTo(null);
		loadSplash.setVisible			(true);
		loadSplash.setIconImage			((new ImageIcon("src/images/menu/icon.png")).getImage());
	
		// Load game settings
		State gameState = new State();
		
		// Load audio machine
		Audio audio = new Audio();
		audio.setVolume(gameState.getAudioVolume());

		audio.playOnce(Definitions.intro);
			
		// Load and draw Menu
		try
		{
			@SuppressWarnings("unused")
			GameMenu menu = new GameMenu(gameState, audio);
			TimeUnit.SECONDS.sleep(2);
			loadSplash.dispose();	
			menu.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
