package gameEngine;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import animation.Animator;
import audioPlayer.Audio;
import audioPlayer.Definitions;
import builder.Builder;
import states.States;

public class Game
{
	MenuEngine 			mMenu;
	Audio				mAudio;
	boolean	 			mIsMale;
	TimeLine[]			mTimeLines; 			
	int					mTimeLineCount;
	int					mCurrentTimeLine;
	boolean				mGameOver;
	Player 				mPlayer;
	Player				mComputer;
	Animator			mGameAnimator;
	Image[]				mTLBackgrounds;
	int					mTLWidth;
	int					mTLHeight;
	boolean				mEscape;
	double				mEscapeTimer;  
	Timer				mTimer;
	
	public Game(boolean isMale, MenuEngine context, Audio audio, boolean powerUpIsHover)
	{
		// Set menu context and audio objects
		mMenu 			= context;
		mAudio 			= audio;
		
		// Context things
		mTimeLineCount 		= 0;
		mCurrentTimeLine 	= 0;
		mEscape 			= false;
		mEscapeTimer 		= 0;
		
		// Create player
		mPlayer 			= new Player(isMale, powerUpIsHover, mAudio);
	
		// Create computer
		mComputer 			= new Player(!isMale, true, mAudio);
		mComputer.setState(States.Player.fallingLeft, null);
		
		// Create game animator
		mGameAnimator 		= new Animator(mPlayer, mComputer);
		
		// Initialise timeline array
		mTimeLines 			= new TimeLine[4];
		for (int i = 0; i < 3; i++)
		{
			mTimeLines[i] = null;
		}
		
		// Load backgrounds 
		try 
		{
			mTLWidth 		  = 1500;
			mTLHeight 		  = 650;
			mTLBackgrounds 	  = new Image[4];
			mTLBackgrounds[1] = (ImageIO.read(new File("src/images/timelines/tl01.png")).getScaledInstance(mTLWidth, mTLHeight, Image.SCALE_DEFAULT));
			mTLBackgrounds[2] = (ImageIO.read(new File("src/images/timelines/tl02.png")).getScaledInstance(mTLWidth, mTLHeight, Image.SCALE_DEFAULT));
			mTLBackgrounds[3] = (ImageIO.read(new File("src/images/timelines/tl03.png")).getScaledInstance(mTLWidth, mTLHeight, Image.SCALE_DEFAULT));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			privateGameOver(null);
		}
		
		// Start timeline one
		mGameOver = false;
		newTimeLine(mTLBackgrounds, 5, 5);
		mTimeLines[mCurrentTimeLine].addComputerToTimeLine(mComputer, 1000, 500);
		
		// Play timeline music
		audio.playMusic(mCurrentTimeLine);
		
		// Create a game timer at 30fps
		mTimer = new Timer(30, new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				update();
			}
		});
		
		// Set timer to repeat
		mTimer.setRepeats(true);
		
		// Start timer
		mTimer.start();
	}
	
	// Swap timelines
	public void swapTimeLines()
	{
		// Stop current timeline music
		mAudio.stopMusic(mCurrentTimeLine);
		
		// Hide current timelined
		mTimeLines[mCurrentTimeLine].hideTimeLine();
		
		// Create or swap to the next timeline
		if (mTimeLineCount == 3)
		{	
			// Swap to the next timeline
			if (mCurrentTimeLine == 3)
			{
				mCurrentTimeLine = 0;
			}
			mCurrentTimeLine++;
			mTimeLines[mCurrentTimeLine].showTimeLine(mPlayer);
		}
		else
		{	
			// Create a new timeline
			newTimeLine(mTLBackgrounds, mPlayer.mLocation.x, mPlayer.mLocation.y);
		}
						
		// Play timeline music
		mAudio.playMusic(mCurrentTimeLine);
	}
	
	// Call this from the timelines
	public void gameOver(int whoWon)
	{
		JFrame splashPanel = null;
		switch (whoWon)
		{
			case 1:
				splashPanel = new Builder.endPanel(mPlayer.mIsMale, true, mMenu);
				mAudio.playOnce(Definitions.win);
				break;
				
			case 2:
				splashPanel = new Builder.endPanel(mPlayer.mIsMale, false, mMenu);
				mAudio.playOnce(Definitions.lose);
				break;
		}
		privateGameOver(splashPanel);
	}
	
	// Private stuff	
	// Check on game over variable
	private void update()
	{
		// Check game over
		if (mGameOver) 
		{
			privateGameOver(null);
		}
		
		// Check escape
		// Escape function
		checkEscapeFromGm(System.currentTimeMillis());

		// Repaint
		if (mTimeLines[mCurrentTimeLine] != null)
		{
			mTimeLines[mCurrentTimeLine].repaintPanel();
		}
	}
	
	// Both game timer and timeline controls call these so we synchronize on it
	// Check escape timer from Game Timer
	public synchronized void checkEscapeFromGm(double time)
	{
		if (time - mEscapeTimer > 500)
		{
			mEscape = false;
			mEscapeTimer = time;
		}
	}
	
	// Check escape timer from Timeline Controls
	public synchronized void checkEscapeFromTL(double time)
	{
		if (!mEscape)
		{
			mEscape = true;
		}
		else if (time - mEscapeTimer<= 500)
		{
			privateGameOver(null);
		}
		mEscapeTimer = time;
	}
	
	
	// Game over method
	private void privateGameOver(JFrame splashPanel)
	{	
		// A foreach loop here doesn't seem to check if they're null :/
		for (int i = 0; i < 3; i++)
		{
			if (mTimeLines[i] != null) {
				mTimeLines[i].killTLThread();
				mTimeLines[i].close();
				mTimeLines[i] = null;
			}
		}
		
		// Stop game timer
		mTimer.stop();
		
		// Stop the game animator thread
		mGameAnimator.killWorkerThread();
		
		// Stop music
		mAudio.stopMusic(mCurrentTimeLine);
		
		// Delay to read splash panel
		if (splashPanel != null)
		{
			// Show splash panel
			splashPanel.setVisible(true);
			splashPanel.addMouseListener((MouseListener) new MouseListener() {

			    @Override
			    public void mouseClicked(MouseEvent e) {

					if(SwingUtilities.isLeftMouseButton(e))
					{
						if (mMenu != null)
						{							
							mMenu.gameOver();
							splashPanel.dispose();
						}
					}
			    }

			    @Override
			    public void mousePressed(MouseEvent e) {

			    }

			    @Override
			    public void mouseReleased(MouseEvent e) {

			    }

			    @Override
			    public void mouseEntered(MouseEvent e) {
			    }

			    @Override
			    public void mouseExited(MouseEvent e) {

			    }
			});
			
			splashPanel.requestFocus();
		}
		else
		{
			mMenu.gameOver();
		}
	}
	
	// Create a new timeline
	private void newTimeLine(Image[] backgrounds, int playerX, int playerY)
	{
		mTimeLineCount++;
		mCurrentTimeLine++;
		mTimeLines[mTimeLineCount] = new TimeLine			(mTLWidth, mTLHeight, this, mPlayer, playerX, playerY, backgrounds[mTimeLineCount]);
		Builder.createItems									(mTimeLines[mCurrentTimeLine].mGameObjects, mTimeLines[mCurrentTimeLine].mPanel, mCurrentTimeLine);
		mTimeLines[mTimeLineCount].createAndStartTLThread	();
		mTimeLines[mTimeLineCount].createControls			();
	}
}
