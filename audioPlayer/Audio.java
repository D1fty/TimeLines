package audioPlayer;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public class Audio 
{
	// Data Members
	private Clip[] mMusicClips;
	// 00 Intro
	// 01 TimeLine One
	// 02 TimeLine Two
	// 03 TimeLine Three
	// 04 Menu
	// 05 Ready
	// 06 Go
	// 07 Lose
	// 08 Win
	// 09 Male ready
	// 10 Female ready
	
	private float 	mMusicGain;
	private float	mVoiceGain;
	private float	mRange;
	private float	mRMax;
	private float	mRMin;
	
	// Constructor
	public Audio()
	{
		// Load the music
		loadAudio();
		
		// Set the range
		FloatControl control 	= (FloatControl)mMusicClips[0].getControl(FloatControl.Type.MASTER_GAIN);
		mRMax 					= control.getMaximum();
		mRMin					= control.getMinimum();
		mRange 					= mRMax - mRMin;
	}

	// Public methods
	// Set volume of all clips
	public void setVolume(float volume) 
	{	
		// Set gain to volume
		try 
		{
			mMusicGain = (mRange * (volume / 100)) + mRMin;
			mVoiceGain = (mRange * ((volume  + 30 )/ 100)) + mRMin;
			
			// Set volumes
			for (int i = 0; i < Definitions.musicCount; i++)
			{
				if ((i == Definitions.readyMan) || (i == Definitions.readyFem) || (i == Definitions.win) || (i == Definitions.lose) || (i == Definitions.ready) || (i == Definitions.go)
				 || (i == Definitions.maleJump) || (i == Definitions.femaleJump) || (i == Definitions.skill) || (i == Definitions.tlChange))
				{
					((FloatControl)mMusicClips[i].getControl(FloatControl.Type.MASTER_GAIN)).setValue(mVoiceGain);
				}
				else
				{
					((FloatControl)mMusicClips[i].getControl(FloatControl.Type.MASTER_GAIN)).setValue(mMusicGain);
				}
			}
		}
		catch (Exception e)
		{
			// Do nothing
		}
	}
	
	// Private methods
	// Load the audio
	private void loadAudio()
	{
		// Load all clips
		mMusicClips = new Clip[Definitions.musicCount];
		
		// Intro music
		// Before all else we need the intro music
		AudioClip aClip = loadAudio("src/audioPlayer/intro.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.intro] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.intro].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
			
			// Set looper
			mMusicClips[Definitions.intro].addLineListener (event -> 
			{
				if (event.getType() == LineEvent.Type.STOP) {
					playMusic(Definitions.menu);
				}
			});	
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading Intro Audio Clip\n");		
		}
		
		// Menu clip
		aClip = loadAudio("src/audioPlayer/menu.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.menu] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.menu].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading Menu Audio Clip\n");		
		}
		
		// TimeLine One Background clip
		aClip = loadAudio("src/audioPlayer/tl1.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.tL1] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.tL1].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading TimeLine One Audio Clip\n");		
		}

		// TimeLine Two Background clip
		aClip = loadAudio("src/audioPlayer/tl2.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.tL2] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.tL2].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading TimeLine Two Audio Clip\n");		
		}
		
		// TimeLine Three Background clip
		aClip = loadAudio("src/audioPlayer/tl3.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.tL3] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.tL3].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading TimeLine Three Audio Clip\n");		
		}
		
		// Go voice
		aClip = loadAudio("src/audioPlayer/GameVoices/go.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.go] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.go].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading go voice Audio Clip\n");		
		}
		
		// Ready Voice
		aClip = loadAudio("src/audioPlayer/GameVoices/ready.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.ready] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.ready].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading Ready Audio Clip\n");		
		}
		
		// Set exit behaviour
		mMusicClips[Definitions.ready].addLineListener(event -> 
		{
			if (event.getType() == LineEvent.Type.STOP) 
			{
				playOnce(Definitions.go);
			}
		});	
				
		// You lose voice
		aClip = loadAudio("src/audioPlayer/GameVoices/you_lose.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.lose] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.lose].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading lose voice Audio Clip\n");		
		}
		
		// You win voice
		aClip = loadAudio("src/audioPlayer/GameVoices/you_win.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.win] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.win].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading win voice Audio Clip\n");		
		}
		
		// Male ready voice
		aClip = loadAudio("src/audioPlayer/maleReady.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.readyMan] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.readyMan].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading male ready voice Audio Clip\n");		
		}
		
		// Female voice
		aClip = loadAudio("src/audioPlayer/femaleReady.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.readyFem] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.readyFem].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading femaleReady voice Audio Clip\n");		
		}
		
		// Skill
		aClip = loadAudio("src/audioPlayer/skill.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.skill] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.skill].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading skill Audio Clip\n");		
		}
		
		// Female jump
		aClip = loadAudio("src/audioPlayer/femaleJump.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.femaleJump] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.femaleJump].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading female jump Audio Clip\n");		
		}
		
		// Male jump
		aClip = loadAudio("src/audioPlayer/maleJump.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.maleJump] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.maleJump].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading maleJump voice Clip\n");		
		}
		
		// TL change
		aClip = loadAudio("src/audioPlayer/tlChange.wav");
		try
		{
			// Get a clip to use
			mMusicClips[Definitions.tlChange] = AudioSystem.getClip();
			
			// Load Data into clip
			mMusicClips[Definitions.tlChange].open	(aClip.getAudioFormat(),aClip.getData(), 0, (int)aClip.getBufferSize());
		}
		catch(Exception exception) 
		{
			// Display Error Message
			System.out.println("Error loading tlChange Clip\n");		
		}
	}
	
	// Plays a loop track on loop
	public void playMusic(int clip) 
	{
		// Set Frame Position to 0
		mMusicClips[clip].setFramePosition(0);

		// Set Clip to Loop
		mMusicClips[clip].loop(Clip.LOOP_CONTINUOUSLY);
	}

	// Plays a track once on loop
	public void playOnce(int clip) 
	{
		// Set Frame Position to 0
		mMusicClips[clip].setFramePosition(0);

		// Set Clip to Loop
		mMusicClips[clip].start();
	}
	
	// Stops the loop track
	public void stopMusic(int clip) 
	{			
		// Stop Clip
		mMusicClips[clip].stop();
	}
	
	// Class used to store an audio clip
		private class AudioClip 
		{
			// Format
			AudioFormat mFormat;

			// Audio Data
			byte[] mData;

			// Buffer Length
			long mLength;

			// Loop Clip
			Clip mLoopClip;

			@SuppressWarnings("unused")
			public Clip getLoopClip() 
			{
				// return mLoopClip
				return mLoopClip;
			}

			@SuppressWarnings("unused")
			public void setLoopClip(Clip clip) 
			{
				// Set mLoopClip to clip
				mLoopClip = clip;
			}

			public AudioFormat getAudioFormat() 
			{
				// Return mFormat
				return mFormat;
			}

			public byte[] getData() 
			{
				// Return mData
				return mData;
			}

			public long getBufferSize() 
			{
				// Return mLength
				return mLength;
			}

			public AudioClip(AudioInputStream stream) 
			{
				// Get Format
				mFormat = stream.getFormat();

				// Get length (in Frames)
				mLength = stream.getFrameLength() * mFormat.getFrameSize();

				// Allocate Buffer Data
				mData = new byte[(int)mLength];

				try 
				{
					// Read data
					stream.read(mData);
				} 
				catch(Exception exception) 
				{
					// Print Error
					System.out.println("Error reading Audio File\n");

					// Exit
					System.exit(1);
				}

				// Set LoopClip to null
				mLoopClip = null;
			}
		}

		
		// Loads the AudioClip stored in the file specified by filename
		private AudioClip loadAudio(String filename) 
		{
			try 
			{
				// Open File
				File file = new File(filename);

				// Open Audio Input Stream
				AudioInputStream audio = AudioSystem.getAudioInputStream(file);

				// Create Audio Clip
				AudioClip clip = new AudioClip(audio);

				// Return Audio Clip
				return clip;
			} 
			catch(Exception e) 
			{
				// Catch Exception
				System.out.println("Error: cannot open Audio File " + filename + "\n");
			}

			// Return Null
			return null;
		}
	
}
