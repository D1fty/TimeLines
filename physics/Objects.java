package physics;

public class Objects
{
	// A 2D point with operator overloads
	public static class Vector
	{
		public double x;
		public double y;
	
		public Vector()
		{
			// Null
		}
		
		public Vector(double x, double y)
		{
			this.x = x;
			this.y = y;
		}
		
		public void add(Vector b)
		{
			x += b.x;
			y += b.y;
		}
		
		public void subtract(Vector b)
		{
			x -= b.x;
			y -= b.y;
		}
		
		public void multiply(Vector b)
		{
			x *= b.x;
			y *= b.y;
		}
		
		public void devide(Vector b)
		{
			x /= b.x;
			y /= b.y;
		}
		
		public void addIndependant(double amt)
		{
			x += amt;
			y += amt;
		}
		
		public void subtractIndependant(double amt)
		{
			x -= amt;
			y -= amt;
		}
		
		public void multiplyIndependant(double amt)
		{
			x *= amt;
			y *= amt;
		}
		
		public void devideIndependant(double amt)
		{
			x /= amt;
			y /= amt;
		}
	}
	
	// A hitbox 
	public static class HitBox
	{
		public 	double mWidth;
		public 	double mHieght;
		public	Vector mTopLeft;
		public 	Vector mBotRight;
				
		public HitBox()
		{
			// Null
		}
		
		public HitBox(Vector topLeft, Vector botRight)
		{
			mTopLeft	= topLeft;
			mBotRight	= botRight;
			mWidth  	= botRight.x - topLeft.x;
			mHieght 	= botRight.y - topLeft.y;
		}
		
		public boolean collides(HitBox object)
		{
			  if(mTopLeft.x < object.mTopLeft.x || mTopLeft.x > object.mBotRight.x) 
			  {
				  return false;
			  }
			  
			  if(mTopLeft.y < object.mTopLeft.y || mTopLeft.y > object.mBotRight.y)
			  {
				  return false;
			  }
			 
			  return true;
		}
	}
}
