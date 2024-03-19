 /**
 * Class Robotron deals with contact of player with enemies/bullets and sets player icon/jlabel
 * @author Stephen Reilly
 * @version 1.0.0
 */

import javax.swing.*;
import java.util.ArrayList;
//import java.awt.Point;
//import java.awt.Rectangle;

public class Robotron extends Character
   {
   private Icon robotronImage; 
   private JLabel robotronLabel; 
   
   /** This constructor for Robotron is used by Board which places
   * robotron (x, y) in the middle of the board at the start of each level
   * @param double x Robotron's x coordinate
   * @param double y Roboton's y coordinate
   * @param Board b the Board class
   */
   public Robotron( double x, double y, Board b )
      {
      super( x, y, b );
      setVelocity( 0, 0 );

      robotronLabel = getJLabel();
      robotronLabel.setVisible( true );
      robotronLabel.setOpaque( true );

      robotronImage = new ImageIcon( "images/Robotron.png" );
      robotronLabel.setIcon( robotronImage );
      robotronLabel.setSize( 50, 50 );
   } // end two-argument constructor
  
   /** Takes the arrayList of enemies on screen supplied by the board class and checks each to see if any have contacted
    * robotron. If an enemy has been contacted, its position in the arrayList is returned, if nothing -1 is returned.
    * It checks for contact by getting the four corners of the enemy and comparing them to the four corners
    * of Robotron. If any of Robotron is "in" the enemy's square, contact has been made and the index of the enemy is returned.
    * @param ArrayList<Enemy> enemies Used to traverse enemy arrayList in board an return index of contacted. 
    */
   public int detectEnemy( ArrayList<Enemy> enemies )
      {
      int roboLeft = (int) getPositionX();
      int roboTop = (int) getPositionY();
      int roboRight = roboLeft + getWidth() - 1;
      int roboBottom = roboTop + getHeight() - 1;
      
      int enemyIndex = -1;
      
      for( Enemy e : enemies )
         {
         enemyIndex++;

         int enemyLeft = (int) e.getPositionX();
         int enemyTop = (int) e.getPositionY();
         int enemyRight = enemyLeft + e.getWidth() - 1;
         int enemyBottom = enemyTop + e.getHeight() - 1;
         
         if( enemyLeft>=roboLeft && enemyLeft<=roboRight && enemyTop>=roboTop && enemyTop<=roboBottom )  // check for enemy upper left corner (eX,eY) collision
            {
            return enemyIndex;
            }
         else if( enemyRight>=roboLeft && enemyRight<=roboRight && enemyTop>=roboTop && enemyTop<=roboBottom )  // check for enemy upper right corner (eX+eWidth,eY) collision 
            {
            return enemyIndex;
            }
         else if( enemyLeft>=roboLeft && enemyLeft<=roboRight && enemyBottom>=roboTop && enemyBottom<=roboBottom )  // check for enemy lower left corner (eX,eY+eHeight) collision 
            {
            return enemyIndex;
            }
         else if( enemyRight>=roboLeft && enemyRight<=roboRight && enemyBottom>=roboTop && enemyBottom<=roboBottom )  // check for enemy lower right corner (eX+eWidth,eY+eHeight) collision 
            {
            return enemyIndex;
            }
         }
       
      return -1;
            
      }
      
   /** Takes the arrayList of bullets on screen supplied by the board class and checks each to see if any have contacted
    * robotron. If a bullet has made contact, its position in the arrayList is returned, if not -1 is returned.
    * This checks for contact by  comparing the four corners of the bullets to the corners of Robotron.
    * If any of the bullet is "in" Robotron's square shape, contact has been made. This method is
    * here because it needs to know robotron's position and the bullet arrays' position.
    * @param ArrayList<Bullet> b Used to traverse bullet arrayList in board an return index of contacted bullets. 
    */
   public int detectBullet( ArrayList<Bullet> b )  // returns ArrayList index of contacted bullet
      {
      int roboLeft = (int) getPositionX();
      int roboTop = (int) getPositionY();
      int roboRight = roboLeft + getWidth() - 1;
      int roboBottom = roboTop + getHeight() - 1;
      
      int bulletIndex = -1;
      
      //Comparing the coordinates of each of the corners of a bullet to those of robotron
      for( Bullet bullet : b )
         {
         bulletIndex++;
         int bulletLeft = (int) bullet.getPositionX();
         int bulletTop = (int) bullet.getPositionY();
         int bulletRight = bulletLeft + bullet.getWidth() - 1;
         int bulletBottom = bulletTop + bullet.getHeight() - 1;
         
         if( bulletLeft>=roboLeft && bulletLeft<=roboRight && bulletTop>=roboTop && bulletTop<=roboBottom )  // check for bullet upper left corner collision
            {
            return bulletIndex;
            }
         else if( bulletRight>=roboLeft && bulletRight<=roboRight && bulletTop>=roboTop && bulletTop<=roboBottom )  // check for bullet upper right corner collision 
            {
            return bulletIndex;
            }
         else if( bulletLeft>=roboLeft && bulletLeft<=roboRight && bulletBottom>=roboTop && bulletBottom<=roboBottom )  // check for bullet lower left corner  collision 
            {
            return bulletIndex;
            }
         else if( bulletRight>=roboLeft && bulletRight<=roboRight && bulletBottom>=roboTop && bulletBottom<=roboBottom )  // check for bullet lower right corner  collision 
            {
            return bulletIndex;
            }
         }
         
      return -1;
      }
   } // end class Robotron
   
   
     
