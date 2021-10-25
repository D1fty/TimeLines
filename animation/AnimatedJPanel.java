package animation;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import gameEngine.GameObject;
import gameEngine.Player;

public class AnimatedJPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	ArrayList<Animation>  mPanelObjects;
	ArrayList<GameObject> mGameObjects;
	ArrayList<Point>	  mPlayerTrail;
	ArrayList<Point>	  mComputerTrail;
	Image				  mPlayerTrailImage;
	Image	              mComputerTrailImage;
	Image				  mBackground;
	
	// Super constructor
	public AnimatedJPanel()
	{
		super();
		mGameObjects 	= new ArrayList<GameObject>();
		mPanelObjects 	= new ArrayList<Animation>();
	}
	
	// Bounds constructor
	public AnimatedJPanel(Animation object, int a, int b, int c, int d, boolean visible, Image bg)
	{
		super();
		mGameObjects 		= new ArrayList<GameObject>();
		mPanelObjects 		= new ArrayList<Animation>();
		mPanelObjects.add	(object);
		setBounds			(a, b, c, d);
		setOpaque			(false);
		setVisible			(visible);
		mBackground 		= bg;
		mComputerTrail		= new ArrayList<Point>();
		mPlayerTrail		= new ArrayList<Point>();
	}
	
	// Player bounds constructor
	public AnimatedJPanel(Player player, int playerx, int playery, int a, int b, int c, int d, boolean visible, Image bg)
	{
		super();
		mPanelObjects 		= new ArrayList<Animation>();
		mGameObjects  		= new ArrayList<GameObject>();
		mPanelObjects.add	((Animation)player);
		setBounds			(a, b, c, d);
		setOpaque			(false);
		setVisible			(visible);
		mBackground 		= bg;
		mComputerTrail		= new ArrayList<Point>();
		mPlayerTrail		= new ArrayList<Point>();
	}
	
	// Add Animated Graphics
	public void addGraphic(Animation object)
	{
		mPanelObjects.add(object);
	}
	
	// Add trail
	public void addTrail(boolean player, ArrayList<Point> trail, Image trailImage)
	{
		if (player)
		{
			mPlayerTrail 		= trail;
			mPlayerTrailImage 	= trailImage;
		}
		else
		{
			mComputerTrail		= trail;
			mComputerTrailImage = trailImage;
		}
	}
	
	// Add map objects
	public void addMapObject(GameObject object)
	{
		if (mGameObjects == null)
		{
			mGameObjects = new ArrayList<GameObject>();
		}
		mGameObjects.add(object);
	}
	
	// Remove the player
	public void removePlayer(Player player)
	{
		mPanelObjects.remove(player);
	}
	
	// Add the player
	public void addPlayer(Player player)
	{
		mPanelObjects.add(player);
	}
	
	@Override
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		if (mBackground != null)
		{
			g.drawImage(mBackground, 0, 0, null);
		}	
		
		// These are not threadsafe, but neither is Swing. If I use synchronised variables Swing doesn't draw at 30FPS, it stutters.
		int i = 0;
		while (i < mPlayerTrail.size())
		{
			g.drawImage(mPlayerTrailImage, mPlayerTrail.get(i).x, mPlayerTrail.get(i).y, null);
			i++;
		}
		
		i = 0;
		while (i < mComputerTrail.size())
		{
			g.drawImage(mComputerTrailImage, mComputerTrail.get(i).x, mComputerTrail.get(i).y, null);
			i++;
		}

		i = 0;
		while (i < mPanelObjects.size())
		{
			g.drawImage(mPanelObjects.get(i).getFrame(), mPanelObjects.get(i).mLocation.x, mPanelObjects.get(i).mLocation.y, null);
			i++;
		}

		for (GameObject object : mGameObjects)
		{
			g.drawImage(object.mGraphic, object.mLocation.x, object.mLocation.y, null);
		}
	}
}