/**
 * Class Enemy sets the moving AI, enemy attributes, and enemy icon/jlabel
 * @author Stephen Reilly
 * @version 1.0.0
 */

import java.util.ArrayList;
import javax.swing.*; 

public class Enemy extends Character
   {
   private int enemyType;
   private Icon image;
   private JLabel enemyLabel;
   private int totalEnemies = 7;
   private double [] speed = new double[totalEnemies];
   private int [] enemyLife = new int[totalEnemies];
   private int [] scoreValue = new int[totalEnemies];
   private int lifeTotal;
   private int enemySize;
   
   public Enemy()
      {
         
      }
   
    /** This constructor for Enemy is used by Level which controls
    * where (x, y) to place each Enemy, the enemy type, and adding the enemy to the board itself
    * so that an Enemy has access to the methods in Board.
    * @param double x the Enemy's x coordinate
    * @param double y the Enemy's y coordinate
    * @param int enemyType corresponding to the Enemy's attributes
    * @param Board b the Board class
    */
   public Enemy(int enemyType, double x, double y, Board b)
      {
      super( x, y, b );
      this.enemyType = enemyType;
      
      speed[0] =  0;
      speed[1] =  1;
      speed[2] =  1;
      speed[3] =  0;
      speed[4] = .5;
      speed[5] = 1;
      speed[6] = .75;
     
      //didn't really use these
      enemyLife[0] = 1;
      enemyLife[1] = 1;
      enemyLife[2] = 2;
      enemyLife[3] = 2;
      enemyLife[4] = 1;
      enemyLife[5] = 1;
      enemyLife[6] = 1;
      
      scoreValue[0] = 50;
      scoreValue[1] = 125;
      scoreValue[2] = 100;
      scoreValue[3] = 150;
      scoreValue[4] = 200;
      scoreValue[5] = 100;
      scoreValue[6] = 25;
      
      lifeTotal = enemyLife[enemyType];

      enemyLabel = this.getJLabel();
      enemySize = 50;
      enemyLabel.setSize( enemySize, enemySize );
      String filename = new String();
      filename = ( "images/Enemy_" + enemyType + ".png" ) ;
      setEnemyImage( filename );
      }

   public void setEnemyImage(String filename)
      {
      image = new ImageIcon( filename );
      enemyLabel.setIcon(image);
      } // end method setIcon
      
   public Icon getEnemyImage()
      {
      return image;
      }
       
   public int getEnemyType()
      {
      return enemyType;
      }
      
   /** Inherits the move method in character which prevents characters from going off screen and adds velocity to
    * the character's position. This method is a simple algorithm that works by getting the players x, y 
    * position and re-ajusting the enemy's direction accordingly to move closer to the player. Only enemy type 2,
    * 1, and 4 move and their speeds are set in the four argument constructor.
    * @return boolean inBounds
    */   
   public int move()
      {
      Robotron player = this.getBoard().getPlayer();
      
      if( enemyType == 1 || enemyType == 2 || enemyType == 4 )
         {
            
         if( this.getPositionX() < player.getPositionX() )
            {
            this.setVelocityX( speed[enemyType] );
            }
      
         if( this.getPositionX() > player.getPositionX() )
            {
            this.setVelocityX( -speed[enemyType] );
            }
   
         if( this.getPositionY() < player.getPositionY() )
            {
            this.setVelocityY( speed[enemyType] );
            }
      
         if( this.getPositionY() > player.getPositionY() )
            {
            this.setVelocityY( -speed[enemyType] );
            }
            
         }
         
      this.preventCrowding();
      return super.move();
      }
   
   /** Certain enemies use this method to set their velocity to random directions except for 0,0. This returns
    * super.move(), an int, that gives board information of which side the enemy hit so it's direction can 
    * // be reflected.
    * 
    * @return super.move()
    */   
   public int randomMove()
      {
      Robotron player = this.getBoard().getPlayer();
      
      if( Math.random() >= .5 )
         {
         if( this.getPositionX() < player.getPositionX() )
            {
            this.setVelocityX( speed[enemyType] );
            }
      
         if( this.getPositionX() > player.getPositionX() )
            {
            this.setVelocityX( -speed[enemyType] );
            }
   
         if( this.getPositionY() < player.getPositionY() )
            {
            this.setVelocityY( speed[enemyType] );
            }
      
         if( this.getPositionY() > player.getPositionY() )
            {
            this.setVelocityY( -speed[enemyType] );
            }
         }
      else
         {
          this.setVelocityX(( (int) (Math.random() * 3) -1) * speed[enemyType]);
          this.setVelocityY(( (int) (Math.random() * 3) -1) * speed[enemyType]);
          
          while( this.getVelocityX() == 0 && this.getVelocityY() == 0)
             {
             this.setVelocityX(( (int) (Math.random() * 3) -1) * speed[enemyType]);
             this.setVelocityY(( (int) (Math.random() * 3) -1) * speed[enemyType]);
             }
         }
         
      this.preventCrowding();
      return super.move();
      }
   
      
   /** For each enemy, the x velocity is added and the enemy is compared to all the enemies on the board
    * if the addition caused contact checked for in the for loop. The x velocity is reset to the old one
    * The same is done for the y velocity. The setting is done separately so the enemy moves smoothly and
    * helps prevent it from getting stuck. If the x was causing collision but the y isn't the enemy
    * should move up. Thus both must be checked individually.
    */      
   public void preventCrowding()
      {
      double oldPositionX = getPositionX();
      double oldPositionY = getPositionY();
      setPositionX( getPositionX() + getVelocityX());
         
      boolean contact = false;
         
      ArrayList<Enemy> enemies = getBoard().getEnemies();

      for( int index = 0; index < enemies.size(); index++)
         {
         //System.out.println( "Hash Enemy = " + enemies.get(index).hashCode() + " Hash This = " + this.hashCode());
         int positionLeft = (int) getPositionX();
         int positionTop = (int) getPositionY();
         int positionRight = (int) getPositionX() + getWidth() - 1;
         int positionBottom = (int) getPositionY() + getHeight() - 1;
         
         //Each object has a unique hashcode. This condition assures the method doesn't compare an enemy to itself
         if( enemies.get(index).hashCode() != this.hashCode() )
            {
            double enemyLeft = (double) enemies.get(index).getPositionX();
            double enemyTop = (double) enemies.get(index).getPositionY();
            double enemyRight = enemyLeft + enemies.get(index).getWidth() - 1;
            double enemyBottom = enemyTop + enemies.get(index).getHeight() - 1;
                           
            if( positionLeft >= enemyLeft && positionLeft <= enemyRight && positionTop >= enemyTop && positionTop <= enemyBottom )
               {
               contact = true;
               }
            
            if( positionRight >= enemyLeft && positionRight <= enemyRight && positionTop >= enemyTop && positionTop <= enemyBottom)
               {
               contact = true;
               }   
            
            if( positionLeft>= enemyLeft && positionLeft<= enemyRight && positionBottom>= enemyTop && positionBottom <= enemyBottom)
               {
               contact = true;
               }
               
            if( positionRight >= enemyLeft && positionRight <= enemyRight && positionBottom >= enemyTop && positionBottom <= enemyBottom)
               {
               contact = true;
               }
               
            if( contact )
               {
               setPositionX( oldPositionX );
               }
            }
         }
         
      contact = false;
      setPositionY( getPositionY() + getVelocityY());
         
      double positionLeft = (double) getPositionX();
      double positionTop = (double) getPositionY();
      double positionRight = (double) getPositionX() + getWidth() - 1;
      double positionBottom = (double) getPositionY() + getHeight() - 1;
      
      for( int index = 0; index < enemies.size(); index++)
         {   
         //System.out.println( "Hash Enemy = " + enemies.get(index).hashCode() + " Hash This = " + this.hashCode());
         if( enemies.get(index).hashCode() != this.hashCode() ) //cant compare itself
            {
            double enemyLeft = (double) enemies.get(index).getPositionX();
            double enemyTop = (double) enemies.get(index).getPositionY();
            double enemyRight = enemyLeft + enemies.get(index).getWidth() - 1;
            double enemyBottom = enemyTop + enemies.get(index).getHeight() - 1;
                           
            if( positionLeft >= enemyLeft && positionLeft <= enemyRight && positionTop >= enemyTop && positionTop <= enemyBottom )
               {
               contact = true;
               }
            
            if( positionRight >= enemyLeft && positionRight <= enemyRight && positionTop >= enemyTop && positionTop <= enemyBottom)
               {
               contact = true;
               }   
            
            if( positionLeft>= enemyLeft && positionLeft<= enemyRight && positionBottom>= enemyTop && positionBottom <= enemyBottom)
               {
               contact = true;
               }
               
            if( positionRight >= enemyLeft && positionRight <= enemyRight && positionBottom >= enemyTop && positionBottom <= enemyBottom)
               {
               contact = true;
               }
               
            if( contact )
               {
                setPositionY( oldPositionY );
               }
            }
         }  
         
      }
      
   public int detectEnemy( ArrayList<Enemy> enemies )
      {
      return -1;
      } 
   
 
      
   public int getLives()
      {
      return lifeTotal;
      }
      
   public void setLives(int lives)
      {
      lifeTotal = lives;
      }
   
   public int getScoreValue()
      {
      return scoreValue[enemyType];
      }
      
   public int detectBullet( ArrayList<Bullet> b )  // returns ArrayList index of contacted bullet
      {
       return(-1);
      }

   }
   
 
