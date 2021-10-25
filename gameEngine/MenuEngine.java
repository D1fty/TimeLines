package gameEngine;

// Java imports
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.*;

// My imports
import animation.*;
import audioPlayer.Audio;
import audioPlayer.Definitions;
import builder.Builder;
import gameMenu.State;
import states.States;

public abstract class MenuEngine implements KeyListener, MouseListener, MouseMotionListener 
{
	// Data members
	public JFrame 				mFrame;
	int 						mWidth, mHeight;
	Animator					mAnimator;
	boolean 					initialised = false;
	Audio						mAudio;
	State						mSettings;
	Point						mScreenPos;
	Color						mBorderColor;
	ArrayList<AnimatedJPanel>	mAnimatedPanels;
	AnimatedJPanel 				mCharMaleWindow;
	Animation	 				mCharMaleWindowGraphics;
	AnimatedJPanel 				mCharFemaleWindow;
	Animation 					mCharFemaleWindowGraphics;
	AnimatedJPanel 				mCharMaleTeleportWindow;
	Animation 					mCharMaleTeleportGraphics;
	AnimatedJPanel 				mCharFemaleTeleportWindow;
	Animation 					mCharFemaleTeleportGraphics;
	AnimatedJPanel 				mCharMaleHoverWindow;
	Animation 					mCharMaleHoverGraphics;
	AnimatedJPanel				mCharFemaleHoverWindow;	
	Animation 					mCharFemaleHoverGraphics;
	MenuEngine					mSelf;
	Game						mGame;
	
	// Thread safe timer to animate at 30fps
	Timer timer = new Timer(30, new ActionListener() 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// Tell the Game to draw
			for (AnimatedJPanel panel : mAnimatedPanels)
			{
				panel.repaint();
			}
		}
	});
	
	// GameEngine Constructor
	public MenuEngine(int width, int height, Audio audio, State settings) {
		// Create graphics transform stack
		mSettings   = settings;
		mAudio		= audio;
		mAnimator   = new Animator();
		mSelf		= this;
		
		// Create window
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create the window	
				setupWindow(width, height);

				// Set animator timer to repeat
				timer.setRepeats(true);
				
				// Start animator
				timer.start();
			}
		});
	}

	// Function to create the window and display it
	private void setupWindow(int width, int height) 
	{
		mFrame 	= new JFrame();
		mWidth 	= width;
		mHeight = height;
		mAnimatedPanels = new ArrayList<AnimatedJPanel>();
		
		// Resize the window (insets are just the boarders that the Operating System puts on the board)
		Insets insets = mFrame.getInsets();
		mFrame.setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
		
		// Create menu
		mFrame.setTitle					("TimeLines");
		mFrame.setResizable				(false);
		mFrame.setUndecorated			(true);
		mFrame.setLayout				(null);
		mFrame.setLocationRelativeTo	(null);
		
		// Add Icon
		mFrame.setIconImage((new ImageIcon("src/images/menu/icon.png")).getImage());
		
		// Add background
		try 
		{
			mFrame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/images/menu/background.png")))));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		 
		// Set border color based on character
		if (mSettings.isMale())
		{
			mBorderColor = States.Colors.myPink;
		}
		else
		{
			mBorderColor = States.Colors.myGreen;
		}
		
		// Set inner borders
		JPanel charBorder = new JPanel		();
		charBorder.setOpaque				(false);
		charBorder.setBounds				(210, 110, 100, 100);
		charBorder.setBorder				(BorderFactory.createMatteBorder(0, 0, 2, 0, mBorderColor));
		mFrame.add							(charBorder);
		
		JPanel pUpBorderLeft = new JPanel	();
		pUpBorderLeft.setOpaque				(false);
		pUpBorderLeft.setBounds				(157, 393, 49, 5);
		pUpBorderLeft.setBorder				(BorderFactory.createMatteBorder(0, 0, 2, 0, mBorderColor));
		mFrame.add							(pUpBorderLeft);
		
		JPanel pUpBorderRight = new JPanel	();
		pUpBorderRight.setOpaque			(false);
		pUpBorderRight.setBounds			(305, 393, 49, 5);
		pUpBorderRight.setBorder			(BorderFactory.createMatteBorder(0, 0, 2, 0, mBorderColor));
		mFrame.add							(pUpBorderRight);
		
		// Title bar	
		final JLabel titleBar = Builder.getMenuButton ("TIMELINES", 185, 33, 20);
		titleBar.setBounds(0, 5, 185, 33);
		
		// Title bar click and drag behavior
		mScreenPos = mFrame.getLocation();
		titleBar.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				States.Clickable.titleBar = true;
				States.Button.entered(titleBar);
			}
			
			public void mouseExited(MouseEvent e)
			{
				States.Clickable.titleBar = false;
				States.Button.exited(titleBar);
			}
			
            @Override
            public void mousePressed(final MouseEvent e) 
            {
				if (e.getButton() == MouseEvent.BUTTON1 )
				{
					titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.WHITE));
            		mScreenPos.x = e.getX();
            	    mScreenPos.y = e.getY();
				}
            }
            
            @Override
            public void mouseReleased(final MouseEvent e) 
            {
				if (e.getButton() == MouseEvent.BUTTON1 )
				{
					titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.BLACK));
				}
            }
		});
		titleBar.addMouseMotionListener(new MouseAdapter()
		{
		     public void mouseDragged(MouseEvent evt)
		     {
				//sets frame position when mouse dragged			
		    	 mFrame.setLocation(evt.getXOnScreen()-mScreenPos.x,evt.getYOnScreen()-mScreenPos.y);
							
		     }
		});
		mFrame.add							(titleBar);
		
		// the X button
		JLabel theX = Builder.getMenuButton ("X", 35, 35, 20);
		theX.setBounds						(460, 5, 35, 35);
		
		// Listener for the X
		theX.addMouseListener		(new MouseAdapter() 
		{
			public void mouseEntered(MouseEvent e)
			{
				States.Clickable.theXButton = true;
				States.Button.entered(theX);
			}
			
			public void mouseExited(MouseEvent e)
			{
				States.Clickable.theXButton = false;
				States.Button.exited(theX);
			}
			
            @Override
            public void mousePressed(final MouseEvent e) 
            {
				if (e.getButton() == MouseEvent.BUTTON1 )
				{
					theX.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.WHITE));
				}
            }

            @Override
            public void mouseReleased(final MouseEvent e) 
            {
            	if (States.Clickable.theXButton)
            	{
            		theX.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, States.Colors.myPink));
            		mSettings.save();
            		System.exit(0);
            	}
            }
        });
		mFrame.add(theX);
		
		// Add swap Power button
		JLabel swapPower = Builder.getMenuButton ("Swap Power", 80, 75, 20);
		swapPower.setBounds						 (188, 425, 140, 30);
		
		// Listener for the swap power button
		swapPower.addMouseListener		(new MouseAdapter() 
		{
			public void mouseEntered(MouseEvent e)
			{
				States.Clickable.swapPower = true;
				States.Button.entered(swapPower);
			}
			
			public void mouseExited(MouseEvent e)
			{
				States.Clickable.swapPower = false;
				States.Button.exited(swapPower);
			}
			
            @Override
            public void mousePressed(final MouseEvent e) 
            {
				if (e.getButton() == MouseEvent.BUTTON1 )
				{
					swapPower.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.WHITE));
				}
            }

            @Override
            public void mouseReleased(final MouseEvent e) 
            {
            	if (States.Clickable.swapPower)
            	{
            		swapPower.setBorder	(BorderFactory.createMatteBorder(0, 0, 2, 2, States.Colors.myPink));
            		if (mSettings.isMale()) 		
            		{
            			// Male Selected
            			if (mSettings.getPower() == 0)
            			{
            				// Swap to hover	
            				mCharMaleTeleportWindow.setVisible	(false);
            				mCharMaleHoverWindow.setVisible		(true);
            			}
            			else
            			{
            				// Swap to teleport     	
            				mCharMaleHoverWindow.setVisible		(false);
            				mCharMaleTeleportWindow.setVisible	(true);
            			}
            		}
            		else
            		{
            			if (mSettings.getPower() == 0)
            			{
            				// Swap to hover	
            				mCharFemaleTeleportWindow.setVisible	(false);
            				mCharFemaleHoverWindow.setVisible		(true);
            			}
            			else
            			{
            				// Swap to teleport     	
            				mCharFemaleHoverWindow.setVisible		(false);
            				mCharFemaleTeleportWindow.setVisible	(true);
            			}
            		}
            		mSettings.swapPower	();	
            	}
            }
        });
		mFrame.add(swapPower);
		
		// Add swap character button
		JLabel swapChar = Builder.getMenuButton ("Swap Hero", 80, 75, 20);
		swapChar.setBounds						(188, 230, 140, 30);
		
		// Listener for the swap character button
		swapChar.addMouseListener		(new MouseAdapter() 
		{
			public void mouseEntered(MouseEvent e)
			{
				States.Clickable.swapChar = true;
				States.Button.entered(swapChar);
			}
			
			public void mouseExited(MouseEvent e)
			{
				States.Clickable.swapChar = false;
				States.Button.exited(swapChar);
			}
			
            @Override
            public void mousePressed(final MouseEvent e) 
            {
				if (e.getButton() == MouseEvent.BUTTON1 )
				{
					swapChar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.WHITE));
				}
            }

            @Override
            public void mouseReleased(final MouseEvent e) 
            {
            	if (States.Clickable.swapChar)
            	{
            		swapChar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, States.Colors.myPink));
            		if (mSettings.isMale())
            		{
            			// Swap to female
            			mCharMaleWindow.setVisible				(false);
            			mCharFemaleWindow.setVisible			(true);
            			mBorderColor = States.Colors.myGreen;
            			
            			// Powerup Window
            			if (mSettings.getPower() == 0)
            			{
	            			// Teleport Animation
                			mCharMaleTeleportWindow.setVisible	(false);
                			mCharFemaleTeleportWindow.setVisible(true);
            			}
            			else
            			{
            				// Hover Animation
            				mCharMaleHoverWindow.setVisible		(false);
            				mCharFemaleHoverWindow.setVisible	(true);
            			}
            		}
            		else
            		{
            			// Swap to male
            			mCharFemaleWindow.setVisible			(false);
            			mCharMaleWindow.setVisible				(true);
            			mBorderColor = States.Colors.myPink;
            			
            			// Powerup Window
            			if (mSettings.getPower() == 0)
            			{
	            			// Teleport Animation
            				mCharFemaleTeleportWindow.setVisible(false);
            				mCharMaleTeleportWindow.setVisible	(true);
            			}
            			else
            			{
            				// Hover Animation
                			mCharFemaleHoverWindow.setVisible	(false);
                			mCharMaleHoverWindow.setVisible		(true);
            			}
            		}
            		mSettings.swapCharacter	();
            		charBorder.setBorder	(BorderFactory.createMatteBorder(0, 0, 2, 0, mBorderColor));
            		pUpBorderRight.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, mBorderColor));
            		pUpBorderLeft.setBorder	(BorderFactory.createMatteBorder(0, 0, 2, 0, mBorderColor));
            	}
            }
        });
		mFrame.add(swapChar);		
		
		// Add play button
		JLabel playButton = Builder.getMenuButton ("Play", 125, 80, 40);
		playButton.setBounds					  (43, 585, 200, 60);
		
		// Listener for the swap character button
		playButton.addMouseListener		(new MouseAdapter() 
		{
			public void mouseEntered(MouseEvent e)
			{
				States.Clickable.playButton = true;
				States.Button.entered(playButton);
			}
			
			public void mouseExited(MouseEvent e)
			{
				States.Clickable.playButton = false;
				States.Button.exited(playButton);
			}
			
            @Override
            public void mousePressed(final MouseEvent e) 
            {
				if (e.getButton() == MouseEvent.BUTTON1 )
				{
					playButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.WHITE));
				}
            }

            @Override
            public void mouseReleased(final MouseEvent e) 
            {
            	if (States.Clickable.playButton)
            	{
            		playButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, States.Colors.myPink));
            		mSettings.save();
            		mAudio.stopMusic(Definitions.intro);
            		mAudio.stopMusic(Definitions.menu);
            		mAudio.playOnce(Definitions.ready);
            		hide();
            		mAudio.stopMusic(Definitions.menu);
            		if (mSettings.getPower() == 1)
            		{
            			mGame = new Game(mSettings.isMale(), mSelf, mAudio, true);
            		}
            		else
            		{
            			mGame = new Game(mSettings.isMale(), mSelf, mAudio, false);
            		}
            	}
            }
        });
		mFrame.add(playButton);
		
		// Add volume buttons
		// Up
		JLabel volUp = Builder.getMenuButton ("Louder", 80, 75, 20);
		volUp.setBounds					  	 (300, 615, 75, 30);
		
		// Listener for the swap character button
		volUp.addMouseListener		(new MouseAdapter() 
		{
			public void mouseEntered(MouseEvent e)
			{
				States.Clickable.volUp = true;
				States.Button.entered(volUp);
			}
			
			public void mouseExited(MouseEvent e)
			{
				States.Clickable.volUp = false;
				States.Button.exited(volUp);
			}
			
            @Override
            public void mousePressed(final MouseEvent e) 
            {
				if (e.getButton() == MouseEvent.BUTTON1 )
				{
					volUp.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.WHITE));
				}
            }

            @Override
            public void mouseReleased(final MouseEvent e) 
            {
            	if (States.Clickable.volUp)
            	{
            		volUp.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, States.Colors.myPink));
            		mSettings.incrimentVolume();   
            		mAudio.setVolume(mSettings.getAudioVolume());
            	}
            }
        });
		mFrame.add(volUp);
		
		// Down
		JLabel volDown = Builder.getMenuButton ("Softer", 80, 75, 20);
		volDown.setBounds					   (400, 615, 75, 30);
		
		// Listener for the swap character button
		volDown.addMouseListener		(new MouseAdapter() 
		{
			public void mouseEntered(MouseEvent e)
			{
				States.Clickable.volDown = true;
				States.Button.entered(volDown);
			}
			
			public void mouseExited(MouseEvent e)
			{
				States.Clickable.volDown = false;
				States.Button.exited(volDown);
			}
			
            @Override
            public void mousePressed(final MouseEvent e) 
            {
				if (e.getButton() == MouseEvent.BUTTON1 )
				{
					volDown.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.WHITE));
				}
            }

            @Override
            public void mouseReleased(final MouseEvent e) 
            {
            	if (States.Clickable.volDown)
            	{
            		volDown.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, States.Colors.myPink));
            		mSettings.decrementVolume();  
            		mAudio.setVolume(mSettings.getAudioVolume());
            	}
            }
        });
		mFrame.add(volDown);
		
		// Add animations
		// Add male 
		try 
		{
			BufferedImage[] images 		= new BufferedImage[4];
							images[0]	= ImageIO.read(new File("src/images/sprites/Adventurer/Idle/adventurer-idle-00.png"));
							images[1]	= ImageIO.read(new File("src/images/sprites/Adventurer/Idle/adventurer-idle-01.png"));
							images[2]	= ImageIO.read(new File("src/images/sprites/Adventurer/Idle/adventurer-idle-02.png"));
							images[3]	= ImageIO.read(new File("src/images/sprites/Adventurer/Idle/adventurer-idle-03.png"));
	
			mCharMaleWindowGraphics	 	= new Animation			(images, 2, 2, true);
			mCharMaleWindow 			= new AnimatedJPanel	(mCharMaleWindowGraphics, 210, 136, images[0].getWidth()*2, images[0].getHeight()*2, false, null);
			mFrame.add											(mCharMaleWindow);
			mAnimatedPanels.add									(mCharMaleWindow);
			mAnimator.add										(mCharMaleWindowGraphics);
			
			// Add female 
			images 		= new BufferedImage[6];
			images[0]	= Transforms.flipBufferedImage(ImageIO.read(new File("src/images/sprites/Warrior/idle/Warrior_Idle_1.png")));
			images[1]	= Transforms.flipBufferedImage(ImageIO.read(new File("src/images/sprites/Warrior/idle/Warrior_Idle_2.png")));
			images[2]	= Transforms.flipBufferedImage(ImageIO.read(new File("src/images/sprites/Warrior/idle/Warrior_Idle_3.png")));
			images[3]	= Transforms.flipBufferedImage(ImageIO.read(new File("src/images/sprites/Warrior/idle/Warrior_Idle_4.png")));
			images[4]	= Transforms.flipBufferedImage(ImageIO.read(new File("src/images/sprites/Warrior/idle/Warrior_Idle_5.png")));
			images[5]	= Transforms.flipBufferedImage(ImageIO.read(new File("src/images/sprites/Warrior/idle/Warrior_Idle_6.png")));
	
			mCharFemaleWindowGraphics = new Animation 			(images, 2, 2, true);
			mCharFemaleWindow		  = new AnimatedJPanel		(mCharFemaleWindowGraphics, 180, 122, images[0].getWidth()*2, images[0].getHeight()*2, false, null);
			mFrame.add								 			(mCharFemaleWindow);
			mAnimatedPanels.add									(mCharFemaleWindow);
			mAnimator.add										(mCharFemaleWindowGraphics);
			
			// Add powerup animations
			// Add male teleport
			images 		= new BufferedImage[11];
			images[0]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-01.png"));
			images[1]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-02.png"));
			images[2]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-03.png"));
			images[3]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-04.png"));
			images[4]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-05.png"));
			images[5]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-06.png"));
			images[6]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-07.png"));
			images[7]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-08.png"));
			images[8]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-09.png"));
			images[9]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-10.png"));
			images[10]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuTele/adventurer-mtele-11.png"));
			
			mCharMaleTeleportGraphics = new Animation			(images, true);
			mCharMaleTeleportWindow   = new AnimatedJPanel		(mCharMaleTeleportGraphics, 157, 350, 196, 44, false, null);
			mFrame.add								   			(mCharMaleTeleportWindow);
			mAnimatedPanels.add									(mCharMaleTeleportWindow);
			mAnimator.add							   			(mCharMaleTeleportGraphics);
	
			// Add male hover
			images 		= new BufferedImage[11];
			images[0]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover01.png"));
			images[1]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover02.png"));
			images[2]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover03.png"));
			images[3]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover04.png"));
			images[4]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover05.png"));
			images[5]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover06.png"));
			images[6]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover07.png"));
			images[7]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover08.png"));
			images[8]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover09.png"));
			images[9]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover10.png"));
			images[10]	= ImageIO.read(new File("src/images/sprites/Adventurer/MenuHover/menuHover11.png"));
			
			mCharMaleHoverGraphics = new Animation			(images, true);
			mCharMaleHoverWindow   = new AnimatedJPanel		(mCharMaleHoverGraphics, 157, 350, 196, 44, false, null);
			mFrame.add								   		(mCharMaleHoverWindow);
			mAnimatedPanels.add								(mCharMaleHoverWindow);
			mAnimator.add							   		(mCharMaleHoverGraphics);
			
			// Add female teleport
			images 		= new BufferedImage[11];
			images[0]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_1.png" ));
			images[1]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_2.png" ));
			images[2]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_3.png" ));
			images[3]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_4.png" ));
			images[4]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_5.png" ));
			images[5]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_6.png" ));
			images[6]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_7.png" ));
			images[7]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_8.png" ));
			images[8]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_9.png" ));
			images[9]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_10.png"));
			images[10]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuTeleport/Warrior_Attack_11.png"));
			
			mCharFemaleTeleportGraphics = new Animation		 	(images, true);
			mCharFemaleTeleportWindow 	= new AnimatedJPanel	(mCharFemaleTeleportGraphics, 157, 350, 196, 44, false, null);
			mFrame.add									 		(mCharFemaleTeleportWindow);
			mAnimatedPanels.add									(mCharFemaleTeleportWindow);
			mAnimator.add								 		(mCharFemaleTeleportGraphics);
			
			// Add female hover
			images 		= new BufferedImage[11];
			images[0]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover1.png" ));
			images[1]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover2.png" ));
			images[2]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover3.png" ));
			images[3]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover4.png" ));
			images[4]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover5.png" ));
			images[5]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover6.png" ));
			images[6]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover7.png" ));
			images[7]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover8.png" ));
			images[8]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover9.png" ));
			images[9]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover10.png"));
			images[10]	= ImageIO.read(new File("src/images/sprites/Warrior/MenuHover/menuHover11.png"));
			
			mCharFemaleHoverGraphics 	= new Animation		 	(images, true);
			mCharFemaleHoverWindow 		= new AnimatedJPanel	(mCharFemaleHoverGraphics, 157, 350, 196, 44, false, null);
			mFrame.add									 		(mCharFemaleHoverWindow);
			mAnimatedPanels.add									(mCharFemaleHoverWindow);
			mAnimator.add								 		(mCharFemaleHoverGraphics);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JFrame error = new JFrame("Error loading images");
			error.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		// Select first visible animations based on saved settings
		if (mSettings.isMale())
		{
			// Male
			mCharMaleWindow.setVisible			(true);
			mCharMaleTeleportWindow.setVisible	(true);
		}
		else
		{
			// Female
			mCharFemaleWindow.setVisible		(true);
			mCharFemaleTeleportWindow.setVisible(true);
		}
	}

	// Show the menu
	public void show() {
		mAnimator.newWorkerThread();
		mFrame.setVisible(true);
	}
	
	// Hide the menu
	public void hide() {
		mAnimator.killWorkerThread();
		mFrame.setVisible(false);
	}
	
	// Game over function
	public void gameOver()
	{
		mGame = null;
		mAudio.playMusic(Definitions.menu);
		show();
	}
}