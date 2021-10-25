package animation;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Animation
{
	JPanel 						mParent;
	public Image[]				mMainAnimation;
	protected int				mFrame;
	protected int				mAnimTime;
	private long				mAnimTimer;
	public Point 				mLocation;
	public boolean				mLooped;

	public Animation()
	{
		mLocation 	= new Point (0, 0);
	}
	
	public Animation(BufferedImage[] images, boolean looped)
	{
		mLocation 	= new Point (0, 0);
		mLooped 	= looped;
		
		// Load images
		mMainAnimation = new Image[images.length];
		int i = 0;
		for (BufferedImage img: images)
		{
			mMainAnimation[i] = img.getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_FAST);;
			i++;
		}
		mFrame		= 0;
		mAnimTime   = 1000 / mMainAnimation.length;
	}
	
	public Animation(BufferedImage[] images, int scaleX, int scaleY, boolean looped)
	{
		mLocation 	= new Point (0, 0);
		mLooped 	= looped;
		
		// Load scaled images
		mMainAnimation = new Image[images.length];
		int i = 0;
		for (BufferedImage img: images)
		{
			mMainAnimation[i] = img.getScaledInstance(img.getWidth() * scaleX, img.getHeight() * scaleY, Image.SCALE_FAST);;
			i++;
		}

		mFrame		= 0;
		mAnimTime   = 1000 / mMainAnimation.length;
	}
	
	// Get the current frame
	public Image getFrame()
	{
		return mMainAnimation[mFrame];
	}
	
	// Update the animation frame
	public void updateAnimation()
	{
		long nowTimer = System.currentTimeMillis();
		if (nowTimer - mAnimTimer > mAnimTime)
		{
			if(mFrame < mMainAnimation.length - 1)
			{
				mFrame++;

			}
			else if (mLooped)
			{
				mFrame = 0;
			}
			mAnimTimer = nowTimer;
		}
	}
	
	// Checks if animation is finished
	public boolean animFinished()
	{
		if (mFrame == mMainAnimation.length - 1)
		{
			return true;
		}
		return false;
	}
}
