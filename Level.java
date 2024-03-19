/**
 * Level Class adds enemies to the arraylist to start each level
 * @author Stephen Reilly
 * @version 1.0.0
 */
 
import java.util.ArrayList;
import java.awt.Point;

public class Level
   {
   private int level;
   //used to make sure generated enemies for random levels are at least this distance from player
   private final int minSeparationFromPlayer = 250;
   private Board gameBoard;
   private ArrayList<Enemy> enemies; 
   private int characterSize;
   private int screenHeight;
   private int screenWidth;
   private ArrayList<Point> enemyPositions;
  
   public Level( Board b )
      {
      enemyPositions = new ArrayList<Point>();
      level = 0;
      gameBoard = b;
      newLevel();
      characterSize = b.getRobotronSize();
      screenHeight = gameBoard.getScreenHeight() - characterSize; //50 is the size of the characters in pixels
      screenWidth = gameBoard.getScreenWidth() - 150; //50 for the same reason...plus the 100 pixel border for the text on the bottom.
      } // end Constructor Level

   public int getLevel()
      {
      return level;
      } // end method getLevelLabel
      
   public ArrayList<Enemy> getEnemies()
      {
      return enemies;
      } // end method getEnemies
      
   /** For level seven and beyond, this method returns a random "valid" Point ( an (x, y) position) 
    * to place each enemy. The while loop continues to generate random points
    * until the point is at least 200ish pixles away from the player so enemies don't 
    * spawn on top of the player.
    * @return Point that can be used to place enemies randomly
    */   
   public Point getValidLocation( Point playerPoint )
      {
      boolean isValidLocation = false;
      int x = 0;
      int y = 0;
      int characterSize = 50;
      Point p = null;
 
      while(!isValidLocation)
         { 
         x = ((int) (Math.random() * screenWidth)/characterSize) *characterSize;
         y = ((int) (Math.random() * screenHeight)/characterSize) * characterSize;
         p = new Point(x, y);

         // get the distance between the generated point and player position
         double distance = Math.sqrt(Math.pow(p.x - playerPoint.x, 2) + Math.pow(p.y - playerPoint.y, 2));
  
         isValidLocation = true;
         
         //ensure that the generated point is far enough from the player's position 
         if( enemyPositions.size() < 1 && distance > minSeparationFromPlayer )
            {
            enemyPositions.add(  p );
            }
         else{
            int overlap = detectEnemyOverlap(p, enemyPositions);

            //if enemy overlaps with existing enemy or is too close to player, location is bad.
            if( overlap > 0 || distance < minSeparationFromPlayer ) {
               isValidLocation = false;
            } else{
               enemyPositions.add(  p );

              //System.out.println(x+", "+y + ", distance from robo: " + distance);
            }
         }
      }  // end while loop
      
      return p;
   }// end method getValidLocation
   
      public int detectEnemyOverlap(Point p, ArrayList<Point> positions){
         int enemyLeft = (int) p.getX();
         int enemyTop = (int) p.getY();
         int enemyRight = enemyLeft + gameBoard.getEnemySize() - 1;
         int enemyBottom = enemyTop + gameBoard.getEnemySize() - 1;
         
         int enemyIndex = -1;
         
         for( Point e : positions )
            {
            enemyIndex++;
   
            int neighborLeft = (int) e.getX();
            int neighborTop = (int) e.getY();
            int neighborRight = neighborLeft + characterSize - 1;
            int neighborBottom = neighborTop + characterSize - 1;
            
            if( neighborLeft>=enemyLeft && neighborLeft<=enemyRight && neighborTop>=enemyTop && neighborTop<=enemyBottom )  // check for enemy upper left corner (eX,eY) collision
               {
               return enemyIndex;
               }
            else if( neighborRight>=enemyLeft && neighborRight<=enemyRight && neighborTop>=enemyTop && neighborTop<=enemyBottom )  // check for enemy upper right corner (eX+eWidth,eY) collision 
               {
               return enemyIndex;
               }
            else if( neighborLeft>=enemyLeft && neighborLeft<=enemyRight && neighborBottom>=enemyTop && neighborBottom<=enemyBottom )  // check for enemy lower left corner (eX,eY+eHeight) collision 
               {
               return enemyIndex;
               }
            else if( neighborRight>=enemyLeft && neighborRight<=enemyRight && neighborBottom>=enemyTop && neighborBottom<=enemyBottom )  // check for enemy lower right corner (eX+eWidth,eY+eHeight) collision 
               {
               return enemyIndex;
               }
            }
           
         return -1;
      }

   /** At the start of the level, this sets the location of the enemies
    * and their type. The enemies life total, speed, and shooting ability 
    * can be changed really easily in the enemy class. After level 6, enemies
    * are added randomly.
    * 
    * Type 0 Red = mine (can't move)
    * Type 1 Blue = fast moving enemies with one life
    * Type 2 Orange = slower enemies with two lives
    * Type 3 Yellow = Can't move but can shoot
    * Type 4 White = Moves very slowly and can shoot
    * Type 5 White/Orange = Moves faster but cannot shoot
    * Type 6 White/Red = Moves slow and can shoot
    * 
    */   
   public void newLevel()
      {
      level++;
      enemies = gameBoard.getEnemies();
      Point playerPoint = new Point( (int) gameBoard.getPlayer().getPositionX(), (int) gameBoard.getPlayer().getPositionY());
      //System.out.println("========LEVEL: " + level + " =======");

      if( level == 1 )
         {
         enemies.add(new Enemy( 0, 200, 150, gameBoard));
         enemies.add(new Enemy( 0, 200, 400, gameBoard));
         enemies.add(new Enemy( 0, 650, 400, gameBoard));
         enemies.add(new Enemy( 0, 650, 150, gameBoard));
         enemies.add(new Enemy( 6, 950, 0, gameBoard));
         //enemies.add(new Enemy( 6, 500, 300, gameBoard)); // center
         enemies.add(new Enemy( 6, 480, 0, gameBoard));
         enemies.add(new Enemy( 6, 0, 0, gameBoard));
         enemies.add(new Enemy( 6, 0, 500, gameBoard));
         enemies.add(new Enemy( 6, 950, 500, gameBoard));
         enemies.add(new Enemy( 6, 480, 500, gameBoard));
         }// end else-if
         
      else if( level == 2 )
         {
         enemies.add(new Enemy( 0, 600, 300, gameBoard));
         enemies.add(new Enemy( 0, 200, 300, gameBoard)); 
         enemies.add(new Enemy( 0, 500, 100, gameBoard)); 
         enemies.add(new Enemy( 2, 0, 0, gameBoard));
         enemies.add(new Enemy( 2, 950, 500, gameBoard));
         enemies.add(new Enemy( 2, 950, 0, gameBoard));
         enemies.add(new Enemy( 2, 0, 500, gameBoard));
         enemies.add(new Enemy( 1, 500, 0, gameBoard));
         enemies.add(new Enemy( 1, 500, 600, gameBoard));
         }// end else-if
         
      else if( level == 3 )
         {
         enemies.add(new Enemy( 5, 0, 0, gameBoard));
         enemies.add(new Enemy( 5, 950, 500, gameBoard));
         enemies.add(new Enemy( 5, 950, 0, gameBoard));
         enemies.add(new Enemy( 5, 0, 500, gameBoard));
         enemies.add(new Enemy( 5, 500, 0, gameBoard));
         enemies.add(new Enemy( 5, 500, 600, gameBoard));
         }   // end else-if
      else if( level == 4 )
         {
         enemies.add(new Enemy( 1, 950, 300, gameBoard));
         enemies.add(new Enemy( 1, 0, 300, gameBoard));
         enemies.add(new Enemy( 3, 950, 600, gameBoard));
         enemies.add(new Enemy( 3, 950, 0, gameBoard));
         enemies.add(new Enemy( 3, 0, 0, gameBoard));
         enemies.add(new Enemy( 1, 500, 0, gameBoard));
         enemies.add(new Enemy( 3, 0, 600, gameBoard));
         }   // end else-if
      else if( level == 5 )
         {
         enemies.add(new Enemy( 0, 600, 300, gameBoard));
         enemies.add(new Enemy( 0, 200, 300, gameBoard)); 
         enemies.add(new Enemy( 0, 500, 100, gameBoard)); 
         enemies.add(new Enemy( 2, 0, 0, gameBoard));
         enemies.add(new Enemy( 2, 950, 500, gameBoard));
         enemies.add(new Enemy( 2, 950, 0, gameBoard));
         enemies.add(new Enemy( 2, 0, 500, gameBoard));
         enemies.add(new Enemy( 1, 500, 0, gameBoard));
         enemies.add(new Enemy( 1, 500, 600, gameBoard));///
         } // end else-if
      else if( level == 6 )
         {
         enemies.add(new Enemy( 0, 200, 150, gameBoard));
         enemies.add(new Enemy( 0, 200, 480, gameBoard));
         enemies.add(new Enemy( 0, 650, 400, gameBoard));
         enemies.add(new Enemy( 1, 650, 150, gameBoard));
         enemies.add(new Enemy( 1, 950, 300, gameBoard));
         enemies.add(new Enemy( 4, 0, 300, gameBoard));
         enemies.add(new Enemy( 3, 950, 600, gameBoard));
         enemies.add(new Enemy( 3, 950, 0, gameBoard));
         enemies.add(new Enemy( 4, 0, 0, gameBoard));
         enemies.add(new Enemy( 4, 500, 0, gameBoard));
         enemies.add(new Enemy( 3, 0, 600, gameBoard));
         } // end else-if
      else if( level == 7 )
         {
         enemies.add(new Enemy( 4, 950, 300, gameBoard));
         enemies.add(new Enemy( 4, 0, 300, gameBoard));
         enemies.add(new Enemy( 4, 950, 600, gameBoard));
         enemies.add(new Enemy( 4, 950, 0, gameBoard));
         enemies.add(new Enemy( 4, 0, 0, gameBoard));
         enemies.add(new Enemy( 4, 500, 0, gameBoard));
         enemies.add(new Enemy( 4, 0, 600, gameBoard));
         }
      else if( level > 7 && level < 12 )
        {
        Point randomPoint = new Point();
        for( int index = 0; index < level; index++ )
           {
           int randomType = (int) (Math.random() * 5 );
           randomPoint = getValidLocation( playerPoint );
           enemies.add( new Enemy( randomType, randomPoint.getX(), randomPoint.getY(), gameBoard)); 
           } // end for loop
        } // end else
      else 
        {
        Point randomPoint = new Point();
        for( int index = 0; index < 12; index++ )
           {
           int randomType = (int) (Math.random() * 4 ) + 1;
           randomPoint = getValidLocation( playerPoint);
           enemies.add( new Enemy( randomType, randomPoint.getX(), randomPoint.getY(), gameBoard)); 
           } // end for loop
        } // end else

      } // end method newLevel

  } // end Class Level
