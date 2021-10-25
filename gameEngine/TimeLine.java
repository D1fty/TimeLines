package gameEngine;

import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import animation.AnimatedJPanel;
import audioPlayer.Definitions;
import physics.Objects.*;
import states.States;

public class TimeLine implements KeyListener, MouseListener, MouseMotionListener
{
	Game 					mGame;
	int  					mWidth, mHeight;
	Thread 					mTLThread;
	Player					mPlayer;
	Player					mComputer;
	boolean 				mTimeLineActive;
	int						mGravity;
	LinkedList<GameObject> 	mGameObjects;
	HitBox					mLastCollidedObject;
	JFrame 					mFrame;
	AnimatedJPanel 			mPanel;
	Image 					mBackground;
	KeyEventDispatcher 		mControls;
	long 					time = 0, oldTime = 0;
	
	ArrayList<Point>		mPlayerTrail;
	Image					mPlayerTrailImage;
	ArrayList<Point>		mComputerTrail;
	Image					mComputerTrailImage;
	
	// Constructor
	public TimeLine(int width, int height, Game context, Player player, int px, int py, Image background)
	{
		// Set member data
		mWidth 				= width;
		mHeight 			= height;
		mPlayer				= player;
		mBackground			= background;
		mGame 				= context;
		mGravity 			= 300;
		mLastCollidedObject = null;
		
		// Set up timeline window
		mFrame 	= new JFrame();
		mFrame.setUndecorated				(true);
		mFrame.setSize						(width, height);
		mFrame.setLocationRelativeTo		(null);
		mFrame.setDefaultCloseOperation		(JFrame.DO_NOTHING_ON_CLOSE);
		mFrame.setVisible					(true);

		// Set up trails for panels
		mPlayerTrail 	= new ArrayList<Point>();
		mComputerTrail 	= new ArrayList<Point>();
		try
		{
			if (mPlayer.mIsMale)
			{
				mPlayerTrailImage 	= ImageIO.read(new File("src/images/sprites/Adventurer/trail.png"));
				mComputerTrailImage = ImageIO.read(new File("src/images/sprites/Warrior/trail.png"));
			}
			else
			{
				mPlayerTrailImage 	= ImageIO.read(new File("src/images/sprites/Warrior/trail.png"));
				mComputerTrailImage = ImageIO.read(new File("src/images/sprites/Adventurer/trail.png"));
			}
		} 
		catch (IOException e)
		{
			new Exception("Error loading trail. isMale: " + mPlayer.mIsMale);
		}
		
		// Set up timeline animation area
		mPanel 	= new AnimatedJPanel		(player, px, py, 0, 0, width, height, true, background);
		mPanel.setDoubleBuffered			(true);
		mPanel.addMouseListener				(this);
		mPanel.addMouseMotionListener		(this);
		mPanel.addTrail						(true, mPlayerTrail, mPlayerTrailImage);
		mPanel.addTrail						(false, mComputerTrail, mComputerTrailImage);
		mFrame.add							(mPanel);
		
		// Create the game objects
		mGameObjects 	= new LinkedList<>	();
		mGameObjects.add					(new GameObject(0,  0, 		-5, 	1	)); // Ceiling
		mGameObjects.add					(new GameObject(0,  0,   	 650, 	1	)); // Floor
		mGameObjects.add					(new GameObject(1, -5, 		 0, 	1	)); // Left Wall
		mGameObjects.add					(new GameObject(1,  width,   0, 	1	)); // Right Wall
	}
	
	// Creates and starts the time line thread
	public void createAndStartTLThread() {
		mTLThread = new TimeLineThread();
		mTLThread.start();
	}
		
	// Thread that updates the timeline
	private class TimeLineThread extends Thread implements Runnable
	{
		@Override
		public void run()
		{				
			// Gameloop wihtin timeline
			mTimeLineActive = true;
			while (mTimeLineActive)
			{
				double passedTime 	= measureTime();
				double dt 			= passedTime / 1000.;
					
				// Player in timeline
				if (mPlayer != null)
				{				
					// Move the player
					if (mPlayerTrail.size() == 100)
					{
						mPlayerTrail.remove(0);
					}
					mPlayerTrail.add(new Point(mPlayer.mLocation.x + (mPlayer.mWidth / 2) - 5, mPlayer.mLocation.y + (mPlayer.mHeight / 2) - 5));
					mPlayer.move(dt);
					
					// Apply gravity
					if (!mPlayer.mIsGrounded)
					{
						mPlayer.applyGravity(dt, mGravity);
					}
					
					// State update
					mPlayer.stateUpdate(getTime(), mLastCollidedObject);
					
					// Check death
					checkDeath(mPlayer, false);
					
					// Check collissions
					checkCollissions(mPlayer);
				}	
				
				// Computer in the timeline
				if (mComputer != null)
				{				
					// Move the computer
					if (mComputerTrail.size() == 100)
					{
						mComputerTrail.remove(0);
					}
					mComputerTrail.add(new Point(mComputer.mLocation.x + (mWidth / 2) - 5, mComputer.mLocation.y + (mHeight / 2) - 5));
					mComputer.move(dt);
					
					// Apply gravity
					if (!mComputer.mIsGrounded)
					{
						mComputer.applyGravity(dt, mGravity);
					}
					
					// State update
					mComputer.stateUpdate(getTime(), mLastCollidedObject);
					
					// Check death
					checkDeath(mComputer, true);
					
					// Check collissions
					checkCollissions(mComputer);
				}	
				
				// Sleep for 10ms
				try
				{
					TimeUnit.MILLISECONDS.sleep(10);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	// Kills the timeline thread
	public void killTLThread() {
		mTimeLineActive = false;
	}
	
	// Closes the timeline
	public void close()
	{
		killTLThread();
		mFrame.dispose();
	}
	
	// Hide a timeline
	public void hideTimeLine()
	{
		removeControls();
		mPanel.removePlayer(mPlayer);
		mPlayer = null;
		mFrame.setVisible(false);
	}
	
	// Show a timeline
	public void showTimeLine(Player player)
	{
			mPlayer = player;
			mPanel.addPlayer(mPlayer);
			mFrame.setVisible(true);
			createControls();
	}
	
	// Add computer to TimeLine
	public void addComputerToTimeLine(Player computer, int x, int y)
	{
		mComputer = computer;
		mComputer.mLocation.x = x;
		mComputer.mLocation.y = y;
		mPanel.addPlayer(computer);
	}
	
	// Remove coputer from TimeLine
	public void removeComputerFromTimeLine()
	{
		mPanel.removePlayer(mComputer);
		mComputer = null;
	}
	
	// Check death
	private void checkDeath(Player player, boolean playerWins)
	{
		if (playerWins)
		{
			for (Point pt : mPlayerTrail)
			{
				if (physics.Functions.collission(player.hitbox(), new HitBox(new Vector(pt.x, pt.y), new Vector(pt.x + 10, pt.y + 10))))
				{
					mGame.gameOver(1);
				}
			}
		}
		else
		{
			for (Point pt : mComputerTrail)
			{
				if (physics.Functions.collission(player.hitbox(), new HitBox(new Vector(pt.x, pt.y), new Vector(pt.x + 10, pt.y + 10))))
				{
					mGame.gameOver(2);
				}
			}
		}
	}
	
	// Check collissions
	public void checkCollissions(Player player) 
	{
		HitBox playerHitbox = player.hitbox();
		boolean hasFloor    = false;
		for (GameObject gObject : mGameObjects) 
		{
			// Check collission
			HitBox objectHitBox = gObject.hitbox();
			if (physics.Functions.collission(playerHitbox, objectHitBox)) 
			{
				mLastCollidedObject = objectHitBox;
				
				// If collission, react based on state
				switch (player.mState)
				{				
					case States.Player.groundedRunningLeft:
						player.runningCollission(objectHitBox, playerHitbox, true);
						break;
						
					case States.Player.groundedRunningRight:
						player.runningCollission(objectHitBox, playerHitbox, false);
						break;	
						
					case States.Player.hoverLeft:
						player.runningCollission(objectHitBox, playerHitbox, true);
						break;
						
					case States.Player.hoverRight:
						player.runningCollission(objectHitBox, playerHitbox, false);
						break;
						
					case States.Player.jumpingLeft:
						if (player.jumpCollission(objectHitBox, playerHitbox, true))
						{
							hasFloor = true;
						}
						break;
						
					case States.Player.jumpingRight:
						if (player.jumpCollission(objectHitBox, playerHitbox, false))
						{
							hasFloor = true;
						}
						break;
						
					case States.Player.fallingLeft:
						if (player.fallingCollission(objectHitBox, playerHitbox, true))
						{
							hasFloor = true;
						}
						break;
						
					case States.Player.fallingRight:
						if (player.fallingCollission(objectHitBox, playerHitbox, false))
						{
							hasFloor = true;
						}
						break;
				}
			}
			
			// Check if its a floor
			if (!hasFloor && physics.Functions.isFloor(playerHitbox, gObject.hitbox(), gObject.mHeight + 2))
			{
				hasFloor = true;
			}
		}
		
		// Checks grounding
		if ((!hasFloor) 
		 && (mPlayer.mState != States.Player.clingingLeft) 
		 && (mPlayer.mState != States.Player.clingingRight) 
		 && (mPlayer.mState != States.Player.hoverRight) 
		 && (mPlayer.mState != States.Player.hoverLeft)
		 && (mPlayer.mState != States.Player.teleOneLeft) 
		 && (mPlayer.mState != States.Player.teleOneRight)
		 && (mPlayer.mState != States.Player.teleTwoLeft) 
		 && (mPlayer.mState != States.Player.teleTwoRight))
		{
			if (mPlayer.mRightFacing)
			{
				mPlayer.setState(States.Player.fallingRight, null);
			}
			else
			{
				mPlayer.setState(States.Player.fallingLeft, null);
			}
		}
	}
	
	// Controls and utility functions below	
	// Create controls for the timeline
	public void createControls()
	{
		// Runs in a thread to let Swing catch up
		new Thread()
		{
		    public void run() {

		    	// This sleep lets swing catch up
		    	while (mPlayer == null)
		    	{
		    		try
					{
						sleep(50);
					} catch (InterruptedException e)
					{
						// Do nothing
					}
		    	}
		    	
				// Create a controller
				mControls = (KeyEventDispatcher) new KeyEventDispatcher() {
					@Override
					public boolean dispatchKeyEvent(KeyEvent e) {
						switch (e.getID()) {
						case KeyEvent.KEY_PRESSED:
							keyPressed(e);
							return false;
						case KeyEvent.KEY_RELEASED:
							keyReleased(e);
							return false;
						case KeyEvent.KEY_TYPED:
							keyTyped(e);
							return false;
						default:
							return false; // do not consume the event
						}
					}};

				// Add it to the game
				KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(mControls);
		    }
		}.start();
	}
	
	// Remove controls for the timeline
	public void removeControls()
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(mControls);
	}
	
	
	// Click actions
	@Override
	public void mouseClicked(MouseEvent event) 
	{
		if(SwingUtilities.isLeftMouseButton(event))
		{
			// Powerup
			if (!mPlayer.mOnCooldown)
			{
				mGame.mAudio.playOnce(Definitions.skill);
				mPlayer.mPowerUpTimer = System.currentTimeMillis();
				if (mPlayer.mPowerUpIsHover)
				{
					// Hover
					if (mPlayer.mRightFacing)
					{
						mPlayer.setState(States.Player.hoverRight, null);
					}
					else
					{
						mPlayer.setState(States.Player.hoverLeft, null);
					}
				}
				else
				{
					// Teleport
					if (mPlayer.mRightFacing)
					{
						mPlayer.setState(States.Player.teleOneRight, null);
					}
					else
					{
						mPlayer.setState(States.Player.teleOneLeft, null);
					}
				}
			}
		}
		else if (SwingUtilities.isRightMouseButton(event))
		{
			// Swap timelines
			mGame.mAudio.playOnce	(Definitions.tlChange);
			removeControls			();
			mGame.swapTimeLines		();
		}
	}
	
	// Called whenever a key is pressed
	@Override
	public void keyPressed(KeyEvent event) 
	{
		switch(event.getKeyCode())
		{
			// D Moves right
			case KeyEvent.VK_D:
				switch(mPlayer.mState)
				{
					case States.Player.groundedIdleRight:
						mPlayer.setState(States.Player.groundedRunningRight, null);
						if (!mPlayer.mIsMale)
						{
							mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, false);
							mLastCollidedObject = null;
						}
						mPlayer.vx = 200;
						break;
						
					case States.Player.groundedIdleLeft:
						mPlayer.setState(States.Player.groundedRunningRight, null);
						if (!mPlayer.mIsMale)
						{
							mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, false);
							mLastCollidedObject = null;
						}
						mPlayer.vx = 200;
						break;
						
					case States.Player.groundedRunningRight:
						mPlayer.setState(States.Player.groundedRunningRight, null);
						mPlayer.vx = 200;
						break;
						
					case States.Player.groundedRunningLeft:
						mPlayer.setState(States.Player.groundedRunningRight, null);
						mPlayer.vx = 200;
						break;
						
					case States.Player.hoverLeft:
						mPlayer.setState(States.Player.hoverRight, mLastCollidedObject);
						mPlayer.vx = 200;
						break;
						
					case States.Player.hoverRight:
						mPlayer.setState(States.Player.hoverRight, mLastCollidedObject);
						mPlayer.vx = 200;
						break;
						
					case States.Player.fallingLeft:
						mPlayer.setState(States.Player.fallingRight, null);
						if (!mPlayer.mIsMale)
						{
							mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, false);
							mLastCollidedObject = null;
						}
						mPlayer.vx = 200;
						break;
						
					case States.Player.fallingRight:
						mPlayer.setState(States.Player.fallingRight, null);
						if (!mPlayer.mIsMale)
						{
							mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, true);
							mLastCollidedObject = null;
						}
						mPlayer.vx = 200;
						break;
						
					case States.Player.jumpingLeft:
						mPlayer.setState(States.Player.jumpingRight, null);
						mPlayer.vx = 200;
						break;
						
					case States.Player.jumpingRight:
						mPlayer.setState(States.Player.jumpingRight, null);
						mPlayer.vx = 200;
						break;
						
					case States.Player.clingingLeft:
						mPlayer.setState(States.Player.fallingRight, null);
						mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, false);
						mPlayer.vx = 200;
						break;
						
					default:
						break;
				}
				break;
				
			// A Moves left
			case KeyEvent.VK_A:
				switch(mPlayer.mState)
				{
					case States.Player.groundedIdleRight:
						mPlayer.setState(States.Player.groundedRunningLeft, null);
						if (!mPlayer.mIsMale)
						{
							mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, true);
							mLastCollidedObject = null;
						}
						mPlayer.vx = -175;
						break;
						
					case States.Player.groundedIdleLeft:
						mPlayer.setState(States.Player.groundedRunningLeft, null);
						if (!mPlayer.mIsMale)
						{
							mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, true);
							mLastCollidedObject = null;
						}
						mPlayer.vx = -175;
						break;
						
					case States.Player.groundedRunningRight:
						mPlayer.setState(States.Player.groundedRunningLeft, null);
						mPlayer.vx = -175;
						break;
						
					case States.Player.groundedRunningLeft:
						mPlayer.setState(States.Player.groundedRunningLeft, null);
						mPlayer.vx = -175;
						break;
						
					case States.Player.hoverRight:
						mPlayer.setState(States.Player.hoverLeft, mLastCollidedObject);
						mPlayer.vx = -175;
						break;
						
					case States.Player.hoverLeft:
						mPlayer.setState(States.Player.hoverLeft, mLastCollidedObject);
						mPlayer.vx = -175;
						break;
						
					case States.Player.fallingLeft:
						mPlayer.setState(States.Player.fallingLeft, null);
						if (!mPlayer.mIsMale)
						{
							mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, true);
							mLastCollidedObject = null;
						}
						mPlayer.vx = -175;
						break;
						
					case States.Player.fallingRight:
						mPlayer.setState(States.Player.fallingLeft, null);
						if (!mPlayer.mIsMale)
						{
							mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, false);
							mLastCollidedObject = null;
						}
						mPlayer.vx = -175;
						break;
						
					case States.Player.jumpingLeft:
						mPlayer.setState(States.Player.jumpingLeft, null);
						mPlayer.vx = -175;
						break;
						
					case States.Player.jumpingRight:
						mPlayer.setState(States.Player.jumpingLeft, null);
						mPlayer.vx = -175;
						break;
						
					case States.Player.clingingRight:
						mPlayer.setState(States.Player.fallingLeft, null);
						mPlayer.clippingCollission(mPlayer.hitbox(), mLastCollidedObject, true);
						mPlayer.vx = -175;
						break;
						
					default:
						break;
				}
				break;
				
			// Space Jump
			case KeyEvent.VK_SPACE:
				if (mPlayer.mIsGrounded)
				{
					mPlayer.vy 			= -200;
					mPlayer.mIsGrounded = false;
					
					// Play sound for gender
					if (mPlayer.mIsMale)
					{
						mGame.mAudio.playOnce(Definitions.maleJump);
					}
					else
					{
						mGame.mAudio.playOnce(Definitions.femaleJump);
					}
					
					// Set state
					if (mPlayer.mState == States.Player.clingingLeft)
					{
						mPlayer.vx = 200;
						mPlayer.setState(States.Player.jumpingRight, mLastCollidedObject);
					}
					else if (mPlayer.mState == States.Player.clingingRight)
					{
						mPlayer.vx = -175;
						mPlayer.setState(States.Player.jumpingLeft, mLastCollidedObject);
					}
					else if (mPlayer.mRightFacing)
					{
						mPlayer.setState(States.Player.jumpingRight, null);
					}
					else
					{
						mPlayer.setState(States.Player.jumpingLeft, null);
					}
				}
				break;
				
			// Exit game on double tap escape
			case KeyEvent.VK_ESCAPE:
				mGame.checkEscapeFromTL(getTime());
				break;
		}
	}
	
	// Called whenever a key is released
	@Override
	public void keyReleased(KeyEvent event) 
	{
		switch(event.getKeyCode())
		{
			case KeyEvent.VK_D:
				switch(mPlayer.mState)
				{					
					case States.Player.groundedRunningRight:
						mPlayer.setState(States.Player.groundedIdleRight, null);
						break;
					
					case States.Player.hoverRight:
						mPlayer.setState(States.Player.fallingRight, mLastCollidedObject);
						break;
						
					case States.Player.fallingRight:
						mPlayer.vx = 0;
						break;
						
					default:
						break;
				}
				break;
				
			case KeyEvent.VK_A:
				switch(mPlayer.mState)
				{					
					case States.Player.groundedRunningLeft:
						mPlayer.setState(States.Player.groundedIdleLeft, null);
						break;
						
					case States.Player.hoverLeft:
						mPlayer.setState(States.Player.fallingLeft, mLastCollidedObject);
						break;
						
					case States.Player.fallingLeft:
						mPlayer.vx = 0;
						break;
						
					default:
						break;
				}
		}
	}
	
	// Utility functions below
	// Called from game timer
	public void repaintPanel()
	{
		if (mPanel != null)
		{
			mPanel.repaint();
		}
	}
	
	// Returns the time in milliseconds
	public long getTime()
	{
		// Get the current time from the system
		return System.currentTimeMillis();
	}

	// Waits for ms milliseconds
	public void sleep(double ms)
	{
		try
		{
			// Sleep
			Thread.sleep((long) ms);
		} 
		catch (Exception e)
		{
			// Do Nothing
		}
	}
	
	// Returns the time passed since this function was last called.
	public long measureTime()
	{
		time = getTime();
		if (oldTime == 0)
		{
			oldTime = time;
		}
		long passed = time - oldTime;
		oldTime = time;
		return passed;
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}

