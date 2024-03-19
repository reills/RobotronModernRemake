/**
 * Abstract class character lays the foundation for
 * and access to, Bullet, Enemy, and Robotron 
 * @author Stephen Reilly
 * @version 1.0.0
 */
 
import javax.swing.*; 

public abstract class Character implements MovingCharacter
   {
   private double positionX;
   private double positionY;
   private double velocityX;
   private double velocityY;
   
   private int width;
   private int height;

   private Board gameBoard;
   
   private Icon image;
   private JLabel characterLabel;
   
   public Character()
      {
      }
   
   /** Sets up a generic constructor that can be inherited by its subclasses
    * @param double x location
    * @param double y location
    * @param Board b
    */   
   public Character( double x, double y,  Board b )
      {
      gameBoard = b;
      characterLabel = new JLabel( image );
      setPosition( x, y );
      }
    
   /** Sets the x and y Position for a moving character
    * @param double x position
    * @param double y position
    */   
   public void setPosition( double x, double y )
      {
      positionX = x;
      positionY = y;
      characterLabel.setLocation( (int) positionX, (int) positionY );
      }
   
   public void setPositionX( double x )
      {
      positionX = x;
      }
      
   public void setPositionY( double y )
      {
      positionY = y;
      }
      
   public double getPositionX()
      {
      return positionX;
      }
    
   public double getPositionY()
      {
      return positionY;
      }
   
   /** Sets the x and y velocities for a moving character
    * @param double x velocity
    * @param double y velocity
    */   
   public void setVelocity ( double x, double y )
      {
      velocityX = x;
      velocityY = y;
      }
      
   public void setVelocityX ( double x )
      {
      velocityX = x;
      }
      
   public void setVelocityY ( double y )
      {
      velocityY = y;
      }
      
   public double getVelocityX()
      {
      return velocityX;
      }
    
   public double getVelocityY()
      {
      return velocityY;
      }

   public int getWidth()
      {
      return width;
      }
      
   public int getHeight()
      {
      return height;
      }
    
   public Board getBoard()
      {
      return gameBoard;
      }
   
   public JLabel getJLabel()
      {
      return characterLabel;
      }
   
   /** Generic move method that fits all characters and returns a boolean
    * just so that when bullets hit the edge, they are removed from the arraylist.
    * The method checks for contact with the left, right, top, and bottom edge which prevents
    * characters from traveling off the frame. Also it adds "velocity" to the characters
    * current position so that they move the amount of pixels their velocity is set to.
    * @return boolean inBounds
    */
   public int move()
      {
      
      if(!( this instanceof Enemy) )   
         {
         positionX += velocityX;
         positionY += velocityY;
         }

      int inBounds = 0;
      int boardWidth = gameBoard.getFrame().getWidth();
      int boardHeight = gameBoard.getFrame().getHeight();
      int labelSize = gameBoard.getLabelSize();
      width = this.getJLabel().getWidth();
      height = this.getJLabel().getHeight();
      
      if( positionX > boardWidth - width  )  
         {
         positionX = boardWidth - width ;
         inBounds = 1;
         }

      if( positionX < 0 )
         {
         positionX = 0;
         inBounds = 2;
         }
         
      if( positionY > boardHeight - height - labelSize) // Labelsize is to prevent objects going on top of score/level/lives textfield
         {
         positionY = boardHeight - height - labelSize;  
         inBounds = 3;
         }
         
      if( positionY < 0 )
         {
         positionY = 0;
         inBounds = 4;
         }
      
      //System.out.println( "Position x = " + positionX + "positionY = " + positionY );
      setPosition ( positionX, positionY );  // updates character's JLabel location
      return inBounds;
      }
 
   }
      

             
