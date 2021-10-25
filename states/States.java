package states;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class States
{
	// These exist as quick access states to avoid extending Java Flex Classes
	static public class Clickable
	{
		// Clickables indicate to mouseListeners that the mouse is till within the button when released
		static public boolean theXButton;
		
		static public boolean playButton;
		
		static public boolean exitButton;
		
		static public boolean volUp;
		
		static public boolean volDown;	
		
		static public boolean swapChar;
		
		static public boolean swapPower;
		
		static public boolean titleBar;
	}
	
	static public class Running
	{
		// Running booleans indicate which process the main() thread is running
		static public boolean mainGame;	
	}
	
	static public class Visible
	{
		// Visibles are for menus
		static public boolean mainMenu;
		
		static public boolean settingsMenu;
	}
	
	static public class Audible
	{
		// Blank for now
	}
	
	static public class Commands
	{
		// Blank for now
	}
	
	static public class Colors
	{
		static public Color myPink = new Color(219, 57, 206);
		
		static public Color myGreen = new Color(57, 218, 141);
	}
	
	static public class Button
	{
		public static void entered(JLabel button)
		{
			button.setBackground(States.Colors.myPink);
			button.setForeground(Color.BLACK);
			button.setBorder	(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.BLACK));
		}
		
		public static void exited(JLabel button)
		{
			button.setBackground(Color.BLACK);
			button.setForeground(States.Colors.myPink);
			button.setBorder	(BorderFactory.createMatteBorder(0, 0, 2, 2, States.Colors.myPink));
		}
	}
	
	static public class Anim 
	{
		public static final int death        = 0;
		public static final int doubleJump 	 = 1;
		public static final int crouch 	 	 = 2;
		public static final int edgeClimb  	 = 3;
		public static final int edgeHang   	 = 4;
		public static final int fall 		 = 5;
		public static final int hover 	 	 = 6;
		public static final int hoverStart 	 = 7;
		public static final int idle 	 	 = 8;
		public static final int jump 		 = 9;
		public static final int run 		 = 10;
		public static final int slide 	 	 = 11;
		public static final int slideStand 	 = 12;
		public static final int teleOne 	 = 13;	
		public static final int teleTwo		 = 14;
	}
	
	static public class Collissions
	{
		public static final int right  = 0;
		public static final int bottom = 1;
		public static final int left   = 2;
		public static final int top    = 3;		
	}
	
	static public class Player
	{
		// this is a mix of animation and direction
		public static final int groundedIdleRight 		= 000;
		public static final int groundedIdleLeft  		= 010;
		public static final int groundedRunningRight 	= 001;
		public static final int groundedRunningLeft 	= 011;
		public static final int hoverLeft				= 002;
		public static final int hoverRight				= 012;
		public static final int jumpingRight			= 102;
		public static final int jumpingLeft				= 112;
		public static final int fallingRight			= 103;
		public static final int fallingLeft				= 113;
		public static final int clingingRight			= 104;
		public static final int clingingLeft			= 114;
		public static final int teleOneRight			= 105;
		public static final int teleTwoRight			= 106;
		public static final int teleOneLeft				= 117;
		public static final int teleTwoLeft				= 118;
	}
	
	static public class PowerUp
	{
		public static final double coolDown				= 5000;
		public static final double hoverTime			= 1000;
	}
}
