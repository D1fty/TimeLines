package gameMenu;

import java.io.Serializable;
import java.time.Duration;

public class SaveInfo implements Serializable 
{
	public 				 float 		audioVolume;
	public  			 Duration 	femHighScore;
	public  			 String 	femHighScorer;
	public  			 Duration 	maleHighScore;
	public  			 String 	maleHighScorer;	
	public				 int		character;				// 0 Male 		1 Female
	public				 int    	power;					// 0 Teleport 	1 Hover
	private static final long 		serialVersionUID = 1L;
}