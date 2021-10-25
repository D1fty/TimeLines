package gameEngine;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import animation.Animation;
import animation.Transforms;
import audioPlayer.Audio;
import audioPlayer.Definitions;
import builder.Builder;
import physics.Objects.HitBox;
import physics.Objects.Vector;
import states.States;

public class Player extends Animation
{
	// Animations facing right
	ArrayList<Image[]> 	mRAnimations;
	
	// Animations facing left
	ArrayList<Image[]> 	mLAnimations;
	int		 			mCurrentAnim;
	
	// State values
	int 				mState;
	int 				mHBTLOffsetX;
	int					mHBTLOffsetY;
	int					mHBBROffsetX;
	int					mHBBROffsetY;
	public boolean		mIsGrounded;
	double				mClingTimer		= 0;
	public int			vx;
	public int			vy;
	
	// Properties
	boolean 			mRightFacing 	= true;
	boolean				mIsMale;
	boolean				mPowerUpIsHover;
	boolean				mOnCooldown;
	double				mPowerUpTimer;
	int					mHeight;
	int					mWidth;
	Audio				mAudio;

	public Player() {
		// Do not call this
	}
	
	public Player(boolean isMale, boolean hover, Audio audio) {
		loadAnim(isMale);
		mIsMale 		= isMale;
		setState(States.Player.fallingRight, null);
		mHeight 		= mMainAnimation[0].getHeight(null) - 2; 
		mWidth 			= mMainAnimation[0].getWidth(null);
		vx 				= 0;
		vy 				= 0;
		mPowerUpIsHover = hover;
		mOnCooldown     = false;
		mAudio			= audio;
	}
	
	// Private Methods
	private void loadAnim(boolean isMale)
	{
		// Left animations
		mRAnimations = new ArrayList<Image[]>();
		mLAnimations = new ArrayList<Image[]>();
		loadAnimations(isMale, true);
		loadAnimations(isMale, false);
	}
	
	// Loads all the animations
	private void loadAnimations(boolean isMale, boolean rightFacing)
	{
		// Loop for all animations as described in member data section
		for (int i = 0; i < 15; i++)
		{		
			// For each animation, load an array of images
			BufferedImage bImages[] = null;
			try
			{
				bImages = Builder.loadAnimation(i, isMale);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			// Handle unimplemented animations
			if (bImages == null)
			{
				if(rightFacing)
				{
					mRAnimations.add(new Image[1]);
				}
				else
				{
					mLAnimations.add(new Image[1]);
				}
			}
			else
			{
				// Load them to the correct facing array
				Image images[] = new Image[bImages.length];
				int j = 0;
				if (rightFacing)
				{
					for (BufferedImage img : bImages)
					{
						images[j]  = img.getScaledInstance(img.getWidth() * 2, img.getHeight() * 2, Image.SCALE_FAST);
						j++;
					}
					mRAnimations.add(images);
				}
				else
				{
					for (BufferedImage img : bImages)
					{
						images[j] = Transforms.flipBufferedImage(img).getScaledInstance(img.getWidth() * 2, img.getHeight() * 2, Image.SCALE_FAST);
						j++;
					}
					mLAnimations.add(images);
				}	
			}
		}			
	}
	
	// Sets the animation
	private void setAnimation(int anim, boolean rightFacing, boolean looped, int frameRate)
	{
		if (mCurrentAnim != anim || mRightFacing != rightFacing) 
		{
			mCurrentAnim = anim;
			mRightFacing = rightFacing;
			mLooped  	 = looped;
			if (mRightFacing)
			{
				mMainAnimation = mRAnimations.get(anim);
			}
			else
			{
				mMainAnimation = mLAnimations.get(anim);
			}

			if (frameRate == -1)
			{
				mAnimTime   = 1000 / mMainAnimation.length;
			}
			else
			{
				mAnimTime   = 1000 / frameRate;
			}
			mFrame 		= 0;
		}
	}
	
	// Apply gravity to the hitbox
	public void applyGravity(double dt, double gravity)
	{
		if (vy < 200) 
		{
			vy += gravity * dt;
		}
	}
	
	// Collission detection for jumping left state
	public boolean jumpCollission(HitBox objectHitBox, HitBox playerHitBox, boolean leftJump)
	{
		// Check landed on top of something
		switch (bangsIntoObject(objectHitBox, playerHitBox))
		{
			case 1: 
				bangHeadOn(objectHitBox.mBotRight.y);
				break;
			
			case 2:
				setPositionOnFloor(objectHitBox.mTopLeft.y);
				return true;
				
			default:
				if (leftJump)
				{
					clingToObjectsSide(objectHitBox.mBotRight.x, true);
				}
				else
				{
					clingToObjectsSide(objectHitBox.mTopLeft.x, false);
				}
				break;
		}
		return false;
	}
	
	private int bangsIntoObject(HitBox objectHitBox, HitBox playerHitBox)
	{
		if ((playerHitBox.mTopLeft.y <= objectHitBox.mBotRight.y) && (playerHitBox.mBotRight.y > objectHitBox.mBotRight.y))
		{
			// Stop vert motion
			return 1;
		}
		
		if ((playerHitBox.mTopLeft.y < objectHitBox.mTopLeft.y) && (playerHitBox.mBotRight.y >= objectHitBox.mTopLeft.y)) 
		{
			// Get set on the roof
			return 2;
		}
		
		// Default behaviour is to cling to the wall for a wall jump
		return 0;	
	}
	
	// Bang head on jumping into something
	private void bangHeadOn(double ceiling)
	{
		vy = 0;
		mLocation.y = (int)ceiling + 1 - mHBTLOffsetY;
		
		if (mRightFacing)
		{
			setState(States.Player.fallingRight, null);
		}
		else
		{
			setState(States.Player.fallingLeft, null);
		}
	}
	
	// Falling collisison detection
	public boolean fallingCollission(HitBox objectHitBox, HitBox playerHitBox, boolean fallingLeft)
	{
		// Land on its roof
		if ((playerHitBox.mTopLeft.y < objectHitBox.mTopLeft.y)&&(playerHitBox.mBotRight.y >= objectHitBox.mTopLeft.y))
		{
			setPositionOnFloor(objectHitBox.mTopLeft.y);
			return true;
		}	

		// Cling to its right side
		if (fallingLeft)
		{
			clingToObjectsSide(objectHitBox.mBotRight.x, true);
			return false;
		}

		// Cling to its left side
		clingToObjectsSide(objectHitBox.mTopLeft.x, false);
		return false;
	}

	// Cling to side of wall for walljump
	private void clingToObjectsSide(double wall, boolean rightSideOfObject)
	{
		if (rightSideOfObject)
		{
			setState(States.Player.clingingLeft, null);
			mLocation.x = (int)wall + 1 - mHBTLOffsetX;
		}
		else
		{
			setState(States.Player.clingingRight, null);
			mLocation.x = (int)wall - 1 - mHBBROffsetX;//- mWidth + mWidth - mHBBROffsetX;
		}
	}
	
	// Generic Actions for jump or fall colliding left 
	public void collidesLeft(HitBox objectHitBox, HitBox playerHitBox, int newState)
	{
		setState(newState, null);
		mLocation.x = (int) objectHitBox.mBotRight.x + 1 - mHBTLOffsetX;
		vy = 0;
		vx = 0;
	}
	
	// Generic Actions for jump or fall colliding left 
	public void collidesRight(HitBox objectHitBox, HitBox playerHitBox,  int newState)
	{
		setState(newState, null);
		mLocation.x = (int) objectHitBox.mTopLeft.x - 1 - mHBBROffsetX; //mWidth + (mWidth - mHBBROffsetX));
		vy = 0;
		vx = 0;
	}
	
	// Running collission
	public void runningCollission(HitBox objectHitBox, HitBox playerHitBox, boolean runningLeft)
	{
		if (runningLeft)
		{		
			setState(States.Player.groundedIdleLeft, null);
			mLocation.x = (int) objectHitBox.mBotRight.x + 1 - mHBTLOffsetX;
		}
		else 
		{
			setState(States.Player.groundedIdleRight, null);
			mLocation.x = (int) objectHitBox.mTopLeft.x - 1 - mHBBROffsetX; //mWidth + (mWidth - mHBBROffsetX);
		}
		vy = 0;
		vx = 0;
	}
	
	// Running state change collission for female
	public void clippingCollission(HitBox playerHitBox, HitBox objectHitBox, boolean leftRunning)
	{
		// I need this to fix the clipping on the female because free assets are useless
		if (objectHitBox != null)
		{
			if (leftRunning)
			{
				if ((playerHitBox.mBotRight.x >= objectHitBox.mTopLeft.x) && (playerHitBox.mTopLeft.x < objectHitBox.mTopLeft.x))
				{
					mLocation.x = (int) objectHitBox.mTopLeft.x - 1 - mHBBROffsetX; //mWidth + (mWidth - mHBBROffsetX);
				}
			}
			else
			{
				if ((playerHitBox.mTopLeft.x <= objectHitBox.mBotRight.x) && (playerHitBox.mBotRight.x > objectHitBox.mBotRight.x))
				{
					mLocation.x = (int) objectHitBox.mBotRight.x + 1 - mHBTLOffsetX;
				}
			}
		}
	}
	
	// Set position of player onto the floor
	public void setPositionOnFloor(double floorHeight)
	{
		vy 			= 0;
		mLocation.y = (int)floorHeight - mHeight - 1;
		if (mRightFacing)
		{
			setState(States.Player.groundedIdleRight, null);
		}
		else
		{
			setState(States.Player.groundedIdleLeft, null);
		}
	}
	
	// Generates and returns a hitbox
	// Required because storing the hit box causes placement issues in swing
	public HitBox hitbox() 
	{
		
		return new HitBox(new Vector(mLocation.x + mHBTLOffsetX, mLocation.y + mHBTLOffsetY), new Vector(mLocation.x + mHBBROffsetX, mLocation.y + mHBBROffsetY));
	}
	
	private void setHBOffsets(int tlx, int tly, int brx, int bry)
	{
		mHBTLOffsetX = tlx;
		mHBTLOffsetY = tly;
		mHBBROffsetX = brx;
		mHBBROffsetY = bry;
	}
	
	// Sets current state
	public void setState(int state, HitBox lastObject)
	{
		switch(state)
		{
			case States.Player.groundedIdleRight:
				setAnimation(States.Anim.idle, true, true, -1);
				if (mIsMale)
				{
					setHBOffsets(28, 14, 65, 71);
				}
				else
				{
					setHBOffsets(36, 20, 71, 85);
				}
				mIsGrounded = true;
				vx = 0;
				vy = 0;
				break;
				
			case States.Player.groundedIdleLeft:
				setAnimation(States.Anim.idle, false, true, -1);
				if (mIsMale)
				{
					setHBOffsets(34, 14, 71, 71);
				}
				else
				{
					setHBOffsets(56, 20, 91, 85);
				}
				mIsGrounded = true;
				vx = 0;
				vy = 0;
				break;
				
			case States.Player.groundedRunningRight: 
				setAnimation(States.Anim.run, true, true, -1);
				if (mIsMale)
				{
					setHBOffsets(34, 16, 73, 71);
				}
				else
				{
					setHBOffsets(24, 28, 73, 85);
				}
				mIsGrounded = true;
				vy = 0;
				break;
				
			case States.Player.groundedRunningLeft:
				setAnimation(States.Anim.run, false, true, -1);
				if (mIsMale)
				{
					setHBOffsets(26, 16, 69, 71);
				}
				else
				{
					setHBOffsets(54, 28, 103, 85);
				}
				mIsGrounded = true;
				vy = 0;
				break;
				
			case States.Player.jumpingRight:
				setAnimation(States.Anim.jump, true, false, 100);
				if (mIsMale)
				{
					setHBOffsets(30, 24, 69, 71);
				}
				else
				{
					setHBOffsets(38, 16, 77, 81);
				}
				clippingCollission(hitbox(), lastObject, false);
				break;
				
			case States.Player.jumpingLeft:
				setAnimation(States.Anim.jump, false, false, 100);
				if (mIsMale)
				{
					setHBOffsets(30, 24, 69, 71);
				}
				else
				{
					setHBOffsets(50, 16, 89, 81 );
				}
				clippingCollission(hitbox(), lastObject, true);
				break;
				
			case States.Player.fallingRight:
				mIsGrounded = false;
				setAnimation(States.Anim.fall, true, true, -1);
				if (mIsMale)
				{
					setHBOffsets(46, 12, 69, 63);
				}
				else
				{
					setHBOffsets(36, 18, 77, 85);
				}
				if (vy == 0) vy = 50;
				clippingCollission(hitbox(), lastObject, false);
				break;
				
			case States.Player.fallingLeft:
				mIsGrounded = false;
				setAnimation(States.Anim.fall, false, true, -1);
				if (mIsMale)
				{
					setHBOffsets(30, 12, 53, 63);
				}
				else
				{
					setHBOffsets(50, 18, 91, 85);
				}
				if (vy == 0) vy = 50;
				clippingCollission(hitbox(), lastObject, true);
				break;
				
			case States.Player.clingingLeft:
				setAnimation(States.Anim.edgeHang, false, true, -1);
				if (mIsMale)
				{
					setHBOffsets(30, 6, 55, 0);
				}
				else
				{
					setHBOffsets(73, 4, 109, 63);
				}
				mClingTimer = System.currentTimeMillis();
				mIsGrounded = true; // Allows jump
				vy = 0;
				vx = 0;
				break;
				
			case States.Player.clingingRight:
				setAnimation(States.Anim.edgeHang, true, true, -1);
				if (mIsMale)
				{
					setHBOffsets(44, 6, 69, 0);
				}
				else
				{
					setHBOffsets(28, 4, 65, 63);
				}
				mClingTimer = System.currentTimeMillis();
				mIsGrounded = true; // Allows jump
				vy = 0;
				vx = 0;
				break;
				
			case States.Player.hoverLeft:
				setAnimation(States.Anim.hover, false, true, -1);
				if (mIsMale)
				{
					setHBOffsets(26, 16, 65, 0);
				}
				else
				{
					setHBOffsets(54, 28, 103, 0);
				}
				
				mOnCooldown   = true;
				mIsGrounded	  = true;
				vy = 0;
				break;
				
			case States.Player.hoverRight:
				setAnimation(States.Anim.hover, true, true, -1);
				if (mIsMale)
				{
					setHBOffsets(34, 16, 73, 0);
				}
				else
				{
					setHBOffsets(24, 28, 73, 0);
				}
				mOnCooldown   = true;
				mIsGrounded	  = true;
				vy = 0;
				break;
				
			case States.Player.teleOneLeft:
				setAnimation(States.Anim.teleOne, false, false, 100);
				setHBOffsets(0, 0, 0, 0);
				mOnCooldown   = true;
				break;	
				
			case States.Player.teleOneRight:
				setAnimation(States.Anim.teleOne, true, false, 100);
				setHBOffsets(0, 0, 0, 0);
				mOnCooldown   = true;
				break;	
				
			case States.Player.teleTwoLeft:
				setAnimation(States.Anim.teleTwo, false, false, 100);
				break;	
				
			case States.Player.teleTwoRight:
				setAnimation(States.Anim.teleTwo, true, false, 100);
				break;
		}
		mState = state;
	}
	public void move(double dt) 
	{
		mLocation.x += vx * dt;
		mLocation.y += vy * dt;
	}
	
	public void stateUpdate(double time, HitBox lastObject)
	{
		// Take powerup off cooldown
		if ((mOnCooldown) && (time - mPowerUpTimer > States.PowerUp.coolDown))
		{
			mOnCooldown = false;
			if (mIsMale)
			{
				mAudio.playOnce(Definitions.readyMan);
			}
			else
			{
				mAudio.playOnce(Definitions.readyFem);
			}
		}
		
		// State updates
		switch(mState)
		{
			case States.Player.jumpingRight:
				// Swap to falling animation
				if (vy >= 0)
				{
					setState(States.Player.fallingRight, null);
					clippingCollission(hitbox(), lastObject, false);
				}
				break;
				
			case States.Player.jumpingLeft:
				// Swap to falling animation
				if (vy >= 0)
				{
					setState(States.Player.fallingLeft, null);
					clippingCollission(hitbox(), lastObject, false);
				}
				break;
			
			case States.Player.clingingLeft:
				if (time - mClingTimer >= 500)
				{
					vx = 100;
					setState(States.Player.fallingRight, null);
					clippingCollission(hitbox(), lastObject, false);
					mClingTimer = time;
				}
				break;
				
			case States.Player.clingingRight:
				if (time - mClingTimer >= 500)
				{
					vx = -75;
					setState(States.Player.fallingLeft, null);
					clippingCollission(hitbox(), lastObject, true);
					mClingTimer = time;
				}
				break;
			
			case States.Player.hoverLeft:
				if (time - mPowerUpTimer > States.PowerUp.hoverTime)
				{
					setState(States.Player.fallingLeft, null);
				}
				break;
				
			case States.Player.hoverRight:
				if (time - mPowerUpTimer > States.PowerUp.hoverTime)
				{
					setState(States.Player.fallingRight, null);
				}
				break;
				
			case States.Player.teleOneLeft:
				if (animFinished())
				{
					mLocation.x += -125;
					setState(States.Player.teleTwoLeft, null);		
				}
				break;	
				
			case States.Player.teleOneRight:
				if (animFinished())
				{
					mLocation.x += 175;
					setState(States.Player.teleTwoRight, null);		
				}
				break;	
				
			case States.Player.teleTwoLeft:
				if (animFinished())
				{
					setState(States.Player.fallingLeft, null);
				}
				break;	
				
			case States.Player.teleTwoRight:
				if (animFinished())
				{
					setState(States.Player.fallingRight, null);
				}
				break;
				
			default:
				//do nothing
				break;
		}
	}
}
