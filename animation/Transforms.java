package animation;

import java.awt.image.BufferedImage;

public class Transforms
{
	// Required to flip images left and right for run directions
	public static BufferedImage flipBufferedImage(BufferedImage sprite)
	{
		BufferedImage img = new BufferedImage(sprite.getWidth(),sprite.getHeight(),BufferedImage.TYPE_INT_ARGB);
		for(int xx = sprite.getWidth()-1;xx>0;xx--)
		{
			for(int yy = 0;yy < sprite.getHeight();yy++)
			{
				img.setRGB(sprite.getWidth()-xx, yy, sprite.getRGB(xx, yy));
			}
		}
		return img;
	}
}
