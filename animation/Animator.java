package animation;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Animator implements Runnable
{
	// Data members
	private LinkedList<Animation> 		mAnimations;
	private boolean						mAnimating;
	private Thread						mThreadWorker;
	private boolean						mWorkerRequired;

	// Constructors
	public Animator()
	{
		mAnimations = new LinkedList<Animation> ();
	}
	
	public Animator(Animation animation)
	{
		mAnimations = new LinkedList<Animation> ();
		mAnimations.addFirst						(animation);
		animate										();
	}
	
	public Animator(Animation animationOne, Animation animationTwo)
	{
		mAnimations = new LinkedList<Animation> ();
		mAnimations.addFirst						(animationOne);
		mAnimations.add								(animationTwo);
		animate										();
	}
	
	// Public methods
	public void animate()
	{
		// Let the animation thread know its time to work
		mAnimating 		= true;
		mWorkerRequired = true;
		
		// Check the worker thread exists
		if (mThreadWorker == null)
		{
			newWorkerThread();
		}
	}
	
	// Stop animator thread from animating
	public void stopimate()
	{
		// Let the animation thread know its time to sleep
		mAnimating = false;
	}
	
	// Start animator thread animating
	public void startimate()
	{
		// Let the worker thread know its time to animate
		mAnimating = true;
	}
	
	// Stop and kill worker thread
	public void killWorkerThread()
	{
		// Stop animating
		stopimate();
		
		// Destroy the animator
		mWorkerRequired = false;
	}
	

	// Create and start worker thread
	public void newWorkerThread()
	{
		// Create a worker thread for animating
		mWorkerRequired = true;
		mAnimating		= true;
		mThreadWorker 	= new Thread(this);
		mThreadWorker.start();
	}
	
	// Add an animationdd
	public void add(Animation animation)
	{
		// Add an animation and, if it's not animating, let the worker thread know it's time to work
		mAnimations.addLast(animation);
		if (!mAnimating)
		{
			animate();
		}
	}
	
	// Remove an animation
	public void remove(Animation animation)
	{
		mAnimations.remove(animation);
	}
	
	// Thread behavior
	@Override
	public void run()
	{
		// Loop until someone turns it off
		while (mWorkerRequired)
		{
			// Loop while animating
			while (mAnimating)
			{
				// Animate all the panels
				for (Animation animation : mAnimations)
				{
					animation.updateAnimation();
				}
				
				// Sleep for 10ms
				try
				{
					TimeUnit.MILLISECONDS.sleep(20);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}						
			}
			
			// Sleep on the main loop
			try
			{
				TimeUnit.MILLISECONDS.sleep(100);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		mThreadWorker 	= null;
	}
}
