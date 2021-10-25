package gameEngine;

import java.awt.Image;
import java.awt.Point;
import java.io.File;

import javax.imageio.ImageIO;

import physics.Objects.HitBox;
import physics.Objects.Vector;

public class GameObject
{
	public Point 		mLocation;
	public Point		mHitBoxBR;
	public Image 		mGraphic;
	public int			mWidth;
	public int 			mHeight;
	
	public GameObject(int object, int posX, int posY, int tl)
	{
		mGraphic	=  null;
		mLocation 	= new Point(posX, posY);
		switch(object)
		{
			// Map floor or ceiling
			case 0: 
				mHitBoxBR = new Point(mLocation.x + 1500, mLocation.y + 5);
				mWidth    = 1500;
				mHeight	  = 5;
				break;
				
			// Map left or right wall
			case 1: 
				mHitBoxBR = new Point(mLocation.x + 5, mLocation.y + 650);
				mWidth    = 5;
				mHeight	  = 650;
				break;
			
			// Big Middle Wall
			case 2:
				try
				{
					mGraphic = ImageIO.read(new File("src/images/timelines/" + tl + "/wall.png"));
				}
				catch (Exception e)
				{
					// Do nothing
				}
				mHitBoxBR = new Point(mLocation.x + 34, mLocation.y + 650);
				mWidth    = 34;
				mHeight	  = 650;
				break;
		
			// Biggest platform
			case 3:
				try
				{
					mGraphic = ImageIO.read(new File("src/images/timelines/" + tl + "/platform.png"));
				}
				catch (Exception e)
				{
					// Do nothing
				}
				mHitBoxBR = new Point(mLocation.x + 600, mLocation.y + 30);
				mWidth    = 600;
				mHeight	  = 30;
				break;
					
			// Small platform
			case 4:
				try
				{
					mGraphic = ImageIO.read(new File("src/images/timelines/" + tl + "/platformsmall.png"));
				}
				catch (Exception e)
				{
					// Do nothing
				}
				mHitBoxBR = new Point(mLocation.x + 300, mLocation.y + 30);
				mWidth    = 300;
				mHeight	  = 30;
				break;
				
			// Tiny platform
			case 5:
				try
				{
					mGraphic = ImageIO.read(new File("src/images/timelines/" + tl + "/platformtiny.png"));
				}
				catch (Exception e)
				{
					// Do nothing
				}
				mHitBoxBR = new Point(mLocation.x + 160, mLocation.y + 17);
				mWidth    = 160;
				mHeight	  = 17;
				break;
		}
	}
	
	public HitBox hitbox()
	{
		return new HitBox(new Vector(mLocation.x, mLocation.y), new Vector(mHitBoxBR.x, mHitBoxBR.y));
	}
}
