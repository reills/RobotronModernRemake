/**
 * Class Bullet sets the parameters for bullet, its icon/jlabel, and 
 * @author Stephen Reilly
 * @version 1.0.0
 */

import java.util.ArrayList;
import javax.swing.*;

public class Bullet extends Character
   { 
   //private Board gameBoard;
   private Icon bulletImage;
   private JLabel bulletLabel;
   private final int bulletSize = 6; 
   
   /** This constructor for Bullet is used by Board which places
   * a Bullet (x, y) either in the middle of an Enemy or Robotron
   * @param double x Bullet's x coordinate
   * @param double y Bullet's y coordinate
   * @param Board b the Board class
   */
   public Bullet( double x, double y, Board b )
      {
      super(x, y, b);
      bulletLabel = this.getJLabel();
      bulletLabel.setSize( 6, 6 );
      setBulletImage();
      }   
   
   public int getBulletSize()
      {
      return bulletSize;
      }
      
   public void setBulletImage()
      {
      bulletImage = new ImageIcon ( "images/Bullet_0.png" );
      bulletLabel.setIcon(bulletImage);
      }
      
   /** Takes the arrayList of enemies supplied by the board class and checks each to see if any have made
    * contact with a bullet. If an enemy has been hit, its position in the arrayList is returned, if not -1 is returned.
    * This checks for contact by comparing the four corners of the bullets to the corners of each of the enemies.
    * If any of the bullet is "in" a Enemy square shape, contact has been made. This method is
    * here because it needs to know the bullet arrays' position although it could be in board.
    * @param ArrayList<Enemy> enemies  Used to traverse enemy arrayList in board an return the index of contacted enemies. 
    */
   public int detectEnemy( ArrayList<Enemy> enemies )  // returns ArrayList index of contacted enemy
      {
//x = left, y = top
      int bulletLeft = (int) getPositionX();
      int bulletTop = (int) getPositionY();
      int bulletRight = bulletLeft + getWidth() - 1;
      int bulletBottom = bulletTop + getHeight() - 1;

      int enemyIndex = -1;
      
      for( Enemy e : enemies )
         {
         enemyIndex++;
         
         int enemyLeft = (int) e.getPositionX();
         int enemyTop = (int) e.getPositionY();
         int enemyRight = enemyLeft + e.getWidth() -1;
         int enemyBottom = enemyTop + e.getHeight()  -1;
         
         if( bulletLeft>=enemyLeft && bulletLeft<=enemyRight && bulletTop>=enemyTop && bulletTop<=enemyBottom )  // check for bullet upper left corner (eX,eY) collision
            {
            return enemyIndex;
            }
         else if( bulletRight>=enemyLeft && bulletRight<=enemyRight && bulletTop>=enemyTop && bulletTop<=enemyBottom  )  // check for bullet upper right corner (eX+eWidth,eY) collision 
            {
            return enemyIndex;
            }
         else if( bulletLeft>=enemyLeft && bulletLeft<=enemyRight && bulletBottom>=enemyTop && bulletBottom<=enemyBottom )  // check for bullet lower left corner (eX,eY+eHeight) collision 
            {
            return enemyIndex;
            }
         else if( bulletLeft>=enemyLeft && bulletRight<=enemyRight && bulletBottom>=enemyTop && bulletBottom<=enemyTop )  // check for bullet lower right corner (eX+eWidth,eY+eHeight) collision 
            {
            return enemyIndex;
            }
         }
         
      return(-1);
      }
    
      
   public int detectBullet( ArrayList<Bullet> b )  // returns ArrayList index of contacted bullet
      {
         return(-1);
      }
   
   } // end class Bullet
   