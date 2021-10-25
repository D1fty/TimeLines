package physics;

import java.util.ArrayList;

import physics.Objects.HitBox;

public class Engine
{
	// Data members the engine needs to be aware of 
	ArrayList<HitBox> mEntities;
	
	public Engine()
	{
		mEntities = new ArrayList<HitBox>();
	}
}
