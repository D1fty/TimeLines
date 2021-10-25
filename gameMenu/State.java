package gameMenu;
import 	java.io.*;
import java.time.Duration;

public class State 
{
	// Actual settings as serializable object
	private SaveInfo 	     mSettings;
	private File 			 mSaveFile;
	
	// Constructor
	public State()
	{
		// Check for serialized folder
		File userDir 	= new File(System.getProperty("user.home"));
		File storageDir = new File(userDir, ".TimeLines");
		if (!storageDir.exists())
		{
		    storageDir.mkdir();
		}

		// Save file in storage dir
		mSaveFile = new File(storageDir, "SaveInfo.ser");
		if (mSaveFile.exists())	
		{
			// Load settings from file
			try 
			{
				FileInputStream 	fileIn 		= new FileInputStream(mSaveFile);
				ObjectInputStream 	in 			= new ObjectInputStream(fileIn);
									mSettings 	= (SaveInfo)in.readObject();
				in.close();
				fileIn.close();
			}
			catch (IOException i)
			{
				i.printStackTrace();
			}
			catch (ClassNotFoundException c)
			{
				System.out.println("SaveInfo class not found");
		        c.printStackTrace();
			}	
				
		}
		else
		{			
			mSettings 				= new SaveInfo();
			mSettings.audioVolume 	= (float)50;
			mSettings.femHighScore	= null;
			mSettings.femHighScorer	= null;
			mSettings.maleHighScore	= null;
			mSettings.maleHighScorer= null;
			mSettings.character		= 0;	// 0 Female 1 Male
			mSettings.power			= 0;
		}	
	}
	
	// Serialize the settings (save to file)
	public void save()
	{
		try
		{
			FileOutputStream 	fileOut 	= new FileOutputStream(mSaveFile);
			ObjectOutputStream 	out 		= new ObjectOutputStream(fileOut);
								out.writeObject(mSettings);
			out.close();
			fileOut.close();
		}
		catch (IOException i)
		{
			i.printStackTrace();
		}
	}
	
	// Highscore functions
	public void setHighScore(Duration score)
	{
		if (mSettings.character == 0)
		{
			mSettings.maleHighScore = score;
		}
		else
		{
			mSettings.femHighScore = score;
		}
	}
	
	public void setHighScorer(String name)
	{
		if (mSettings.character == 0)
		{
			mSettings.maleHighScorer = name;
		}
		else
		{
			mSettings.femHighScorer = name;
		}
	}
	
	public Duration getHighScore()
	{
		if (mSettings.character == 0)
		{
			return mSettings.maleHighScore;
		}
		else
		{
			return mSettings.femHighScore;
		}
	}
	
	public String getHighScorer()
	{
		if (mSettings.character == 0)
		{
			return mSettings.maleHighScorer;
		}
		else
		{
			return mSettings.femHighScorer;
		}
	}
	
	// Audio functions
	public float getAudioVolume() 
	{
		return mSettings.audioVolume;
	}
	
	public void incrimentVolume()
	{
		if (mSettings.audioVolume < 98)
		{
			mSettings.audioVolume += 2;
		}
	}
	
	public void decrementVolume()
	{
		if (mSettings.audioVolume > 1)
		{
			mSettings.audioVolume -= 2;
		}
	}
	
	// Character functions
	public boolean isMale()
	{
		if (mSettings.character > 0)
		{
			return true;
		}
		return false;
	}
	
	public void swapCharacter()
	{
		mSettings.character ^= 1;
	}
	
	// Power functions
	
	public int getPower()
	{
		return mSettings.power;
	}
	
	public void swapPower()
	{
		mSettings.power ^= 1;
	}
}