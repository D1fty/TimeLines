package physics;

import physics.Objects.*;

public class Functions
{
	// Collission detection between two hitboxes
	public static boolean collission(HitBox A, HitBox B)
	{
		  if(A.mBotRight.x <= B.mTopLeft.x || A.mBotRight.y <= B.mTopLeft.y) 
		  {
			  return false;
		  }
		  
		  if(A.mTopLeft.x >= B.mBotRight.x || A.mTopLeft.y >= B.mBotRight.y)
		  {
			  return false;
		  }			 
		  return true;
	}
	
	public static boolean isFloor(HitBox A, HitBox B, int disparity)
	{
		if (A.mBotRight.x < B.mTopLeft.x || A.mTopLeft.x > B.mBotRight.x)
		{
			return false;
		}
		else if (A.mBotRight.y > B.mTopLeft.y) 
		{
			return false;
		}
		else if (B.mBotRight.y - A.mBotRight.y > disparity)
		{
			return false;
		}
		return true;
	}
}
