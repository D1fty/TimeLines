package builder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import animation.AnimatedJPanel;
import gameEngine.GameObject;
import gameEngine.MenuEngine;
import states.*;

public class Builder
{
	// Private methods to clean up repeated code
	// Menu UI Methods
	public static JLabel getMenuButton(String text, int width, int height, int fontSize)
	{
		JLabel button = new JLabel		(text);
		button.setForeground			(States.Colors.myPink);
		button.setBackground			(Color.BLACK);
		button.setMinimumSize			(new Dimension(width, height));
		button.setMaximumSize			(new Dimension(width, height));
		button.setPreferredSize			(new Dimension(width, height));
		button.setFont					(new Font("Arial", Font.BOLD, fontSize));
		button.setBorder				(BorderFactory.createMatteBorder(0, 0, 2, 2, States.Colors.myPink));
		button.setVerticalAlignment		(JLabel.CENTER);
		button.setHorizontalAlignment	(JLabel.CENTER);
		button.setOpaque				(true);
		return button;
	}
	
	public static JPanel getSettingsPane(JPanel top, JPanel bottom, Dimension panelSize)
	{
		JPanel pane = new JPanel	();
		pane.setLayout				(new BoxLayout(pane, BoxLayout.X_AXIS));
		pane.setBackground			(Color.BLACK);
		pane.setBackground			(Color.BLACK);
		pane.setMinimumSize			(panelSize);
		pane.setMaximumSize			(panelSize);
		pane.setPreferredSize		(panelSize);
		pane.setOpaque				(true);
		pane.setLayout				(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add					(Box.createVerticalGlue());
		pane.add					(Box.createVerticalGlue());
		pane.add					(top);
		pane.add					(Box.createVerticalGlue());
		pane.add					(bottom);
		pane.add					(Box.createVerticalGlue());
		pane.add					(Box.createVerticalGlue());
		return pane;
	}
	
	public static JPanel getTitlePane(JLabel title)
	{
		JPanel pane = new JPanel	();
		pane.setLayout				(new BoxLayout(pane, BoxLayout.X_AXIS));
		pane.setBackground			(Color.BLACK);
		pane.add					(Box.createHorizontalGlue());
		pane.add					(title);
		pane.add					(Box.createHorizontalGlue());
		return pane;
	}
	
	public static JLabel getTitleLabel(String text)
	{
		JLabel label = new JLabel	();
		label.setText				(text);
		label.setMaximumSize		(new Dimension(100, 20));
		label.setFont				(new Font("Arial", Font.BOLD, 20));
		label.setForeground			(Color.WHITE);
		return label;
	}
	
	public static JLabel getSettingsDash(String text)
	{
		JLabel dash = new JLabel	();
		dash.setHorizontalAlignment	(JLabel.CENTER);
		dash.setFont				(new Font("Arial", Font.BOLD, 20));
		dash.setForeground			(Color.WHITE);
		dash.setMaximumSize			(new Dimension(30,30));
		dash.setMinimumSize			(new Dimension(30,30));
		dash.setPreferredSize		(new Dimension(30,30));
		dash.setText				(text);
		return dash;
	}
	
	public static JPanel getButtonsPane(JLabel left, JLabel mid, JLabel right)
	{
		JPanel panel = new JPanel	();
		panel.setLayout				(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBackground			(Color.BLACK);
		panel.add					(Box.createHorizontalGlue());
		panel.add					(left);
		panel.add					(Box.createRigidArea(new Dimension(10,20)));
		panel.add 					(mid);
		panel.add					(Box.createRigidArea(new Dimension(10,20)));
		panel.add					(right);
		panel.add					(Box.createHorizontalGlue());
		return panel;
	}
	
	// Male animation methods
	public static BufferedImage[] loadAnimation(int anim, boolean isMale) throws Exception
	{
		BufferedImage[] images = null;
		switch (anim)
		{
			// Death animation
			case 0:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[7];
						for (int i = 0; i < 7; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/Die/adventurer-die-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[10];
						for (int i = 1; i < 11; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/Death-Effect/Warrior_Death_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading death animation. isMale: " + isMale);
				}
				
			// Double jump
			case 1:
				try 
				{
					// Not enough time to implement
					return null;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading double jump animation. isMale: " + isMale);
				}
			
			// Crouch
			case 2:
				try 
				{
					// Not enough time to implement
					return null;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading crouch animation. isMale: " + isMale);
				}
				
			// Edge climb
			case 3:
				try 
				{
					// Not enough time to implement
					return null;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading edge climb animation. isMale: " + isMale);
				}
			
			// Edge Hang
			case 4:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[4];
						for (int i = 0; i < 4; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/EdgeHang/adventurer-crnr-grb-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[5];
						for (int i = 1; i < 6; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/EdgeGrab/Warrior_Edge-Grab_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading edge hang animation. isMale: " + isMale);
				}
				
			// Fall
			case 5:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[4];
						for (int i = 0; i < 4; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/Fall/adventurer-fall-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[3];
						for (int i = 1; i < 4; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/Fall/Warrior_Fall_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading fall animation. isMale: " + isMale);
				}
				
			// Hover
			case 6:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[6];
						for (int i = 0; i < 6; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/Hover/adventurer-run-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[8];
						for (int i = 1; i < 9; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/Hover/Warrior_Run_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading Hover animation. isMale: " + isMale);
				}
			
			// Hover start
			case 7:
				try 
				{
					// Not enough time to impliment
					return null;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading Hover Start animation. isMale: " + isMale);
				}
				
			// 	Idle
			case 8:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[4];
						for (int i = 0; i < 4; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/Idle/adventurer-idle-0" + i + ".png"));
						
						}
					}
					else
					{
						images = new BufferedImage[6];
						for (int i = 1; i < 7; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/Idle/Warrior_Idle_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading Idle animation. isMale: " + isMale);
				}
			
			// Jump
			case 9:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[4];
						for (int i = 0; i < 4; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/Jump/adventurer-jump-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[3];
						for (int i = 1; i < 4; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/Jump/Warrior_Jump_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading Jump animation");
				}
				
			// Run
			case 10:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[6];
						for (int i = 0; i < 6; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/Run/adventurer-run-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[8];
						for (int i = 1; i < 9; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/Run/Warrior_Run_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading Run animation");
				}
				
			// Slide
			case 11:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[4];
						for (int i = 0; i < 4; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/Slide/adventurer-slide-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[3];
						for (int i = 1; i < 4; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/SlideNoEffect/Warrior-SlideNoEffect_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading Slide animation. isMale: " + isMale);
				}
			
			// Slide to stand
			case 12:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[3];
						for (int i = 0; i < 3; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/SlideStand/adventurer-stand-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[5];
						for (int i = 1; i < 6; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/Slide/Warrior-Slide_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading Slide Stand animation. isMale: " + isMale);
				}
				
			// Teleport start
			case 13:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[5];
						for (int i = 0; i < 5; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/Tele/adventurer-mtele-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[8];
						for (int i = 1; i < 9; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/Tele/Warrior_Attack_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading Teleport animation. isMale: " + isMale);
				}
			
			// Tele fin
			case 14:
				try 
				{
					if (isMale)
					{
						images = new BufferedImage[4];
						for (int i = 0; i < 4; i++)
						{
							images[i] = ImageIO.read(new File("src/images/sprites/Adventurer/EndTele/adventurer-mtele-0" + i + ".png"));
						}
					}
					else
					{
						images = new BufferedImage[3];
						for (int i = 1; i < 4; i++)
						{
							images[i - 1] = ImageIO.read(new File("src/images/sprites/Warrior/EndTele/Warrior_Attack_" + i + ".png"));
						}					
					}
					return images;
				}
				catch (Exception e)
				{
					throw new Exception("Error loading Teleport animation. isMale: " + isMale);
				}
			
			default:
			{
				return null;
			}
		}
	}
	
	// Creates the timeline functions
	public static void createItems(LinkedList<GameObject> gameObjects, AnimatedJPanel panel, int tl)
	{
		GameObject object = null;
		switch (tl)
		{
			case 1:
				// Middle wall
				object = new GameObject(2,  733, 0, 1);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Left lower left platform
				object = new GameObject(5, -60, 480, 1);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Left lower right platform
				object = new GameObject(5, 500, 550/*480*/, 1);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Left upper platform
				object = new GameObject(4, 100, 300, 1);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Right lower left platform
				object = new GameObject(5, 1000, 480, 1);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Right lower right platform
				object = new GameObject(5, 1440, 480, 1);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Right upper platform
				object = new GameObject(4, 900, 300, 1);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				break;
				
			case 2:
				// Right~ish wall
				object = new GameObject(2,  1000, 300, 2);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Upper big platform
				object = new GameObject(3,  800, 270, 2);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Right intersect big platform
				object = new GameObject(3,  650, 470, 2);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Right lower stepup platform
				object = new GameObject(5, 1340, 560, 2);
				gameObjects.add(object);
				panel.addMapObject(object);
					
				// Right upper stepup platform
				object = new GameObject(5, 1340, 380, 2);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Left upper platform
				object = new GameObject(4, 0, 300, 2);
				gameObjects.add(object);
				panel.addMapObject(object);	
				break;
				
				
			case 3:
				
				// Lowest platform
				object = new GameObject(3, 300, 400, 3);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Left upper platform
				object = new GameObject(4, 0, 100, 3);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Left upper platform
				object = new GameObject(4, 400, 100, 3);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Left upper platform
				object = new GameObject(4, 800, 100, 3);
				gameObjects.add(object);
				panel.addMapObject(object);
				
				// Left upper platform
				object = new GameObject(4, 1200, 100, 3);
				gameObjects.add(object);
				panel.addMapObject(object);
				break;
		}
	}
	
	public static class endPanel extends JFrame //implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		endPanel   self;
		MenuEngine mMenu;
		
		public endPanel	(boolean isMale, boolean humanWon, MenuEngine menu)
		{
			super();
			mMenu = menu;
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			if(humanWon)
			{
				if (isMale)
				{
					try 
					{
						setContentPane	(new JLabel(new ImageIcon(ImageIO.read(new File("src/images/sprites/adventurer/victory.png")))));
			        } 
					catch (IOException e) 
					{
			            e.printStackTrace();
			        }
				}
				else
				{
					try 
					{
						setContentPane	(new JLabel(new ImageIcon(ImageIO.read(new File("src/images/sprites/warrior/victory.png")))));
			        } 
					catch (IOException e) 
					{
			            e.printStackTrace();
			        }
				}
			}
	
			else if (isMale)
			{
				try 
				{
					setContentPane	(new JLabel(new ImageIcon(ImageIO.read(new File("src/images/sprites/adventurer/defeat.png")))));
		        } 
				catch (IOException e) 
				{
		            e.printStackTrace();
		        }
			}
			else
			{
				try 
				{
					setContentPane	(new JLabel(new ImageIcon(ImageIO.read(new File("src/images/sprites/warrior/defeat.png")))));
		        } 
				catch (IOException e) 
				{
		            e.printStackTrace();
		        }
			}		
			setUndecorated			(true);
			pack					();
			setLocationRelativeTo	(null);
			self = this;
		}
	}
}
