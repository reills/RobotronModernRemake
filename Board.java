/**
 * Board runs the game logic and animation: "The brain" of Robotron
 * @author Stephen Reilly
 * @version 1.0.0
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Board extends JPanel implements MouseListener, KeyListener
   {
   private int score;
   private Level level;
   private int lifeTotal; 
   
   private Robotron player;
   private ArrayList<Enemy> allEnemies;
   private ArrayList<Bullet> enemyBullets;
   private ArrayList<Bullet> roboBullets;
   private Set<Integer> pressedKeys = new HashSet<>();
 
   private final double roboSpeed = 2;
     
   private final int screenWidth = 1000;
   private final int screenHeight = 600;
   private final int labelSize = 100;
   private final int roboSize = 50;
   private final int enemySize = 50;
   
   private javax.swing.Timer gameTimer;
   private javax.swing.Timer bulletTimer;
   private javax.swing.Timer moveTimer;

   private boolean levelInProgress;
   private boolean isGameOver;
   private boolean isPlayerHit;
   
   private JFrame frame;
   private JPanel panel;
   private JLabel gameOverLabel;
   
   private JTextField lifeTotalLabel;
   private JTextField scoreLabel;
   private JTextField levelLabel;
     
   /** Constructor for Board sets initial variables, creates and adds all JLabels
    * to the frame, creates a MouseListener and KeyListener for gameplay
    * and adds the enemies set in the level class to the allEnemies arrayList.
    * 
    */
   public Board()
      {
      allEnemies = new ArrayList<Enemy>();
      enemyBullets = new ArrayList<Bullet>();
      roboBullets = new ArrayList<Bullet>();
      
      player = new Robotron( screenWidth/2 - 25, screenHeight/2 -25, this );
      
      levelInProgress = true;
      score = 0;
      lifeTotal = 5;
      level = new Level( this );
      isGameOver = false;
      isPlayerHit = false; 
      
      allEnemies = level.getEnemies();

      frame = new JFrame("Robotron by Stephen Reilly");
      frame.setSize( screenWidth, screenHeight);
      frame.setResizable(false);
      frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      panel = new JPanel();
      panel.setLayout( null );
      panel.setBackground (Color.black);

      scoreLabel = new JTextField( String.valueOf ( "SCORE: " + score)  );
      scoreLabel.setEditable(false);
      scoreLabel.setHighlighter(null);
      scoreLabel.setBorder(BorderFactory.createEmptyBorder());
      scoreLabel.setForeground(Color.WHITE);
      scoreLabel.setBackground(Color.BLACK);
      scoreLabel.setFont (new Font("sansserif", Font.BOLD, 24));
      scoreLabel.setHorizontalAlignment(JTextField.CENTER);
      scoreLabel.setSize(200,30);
      scoreLabel.setLocation(800, 526);
      scoreLabel.requestFocus(true);
      
      levelLabel = new JTextField( String.valueOf( "LEVEL: " + level.getLevel() ) );
      levelLabel.setEditable(false);
      levelLabel.setHighlighter(null);
      levelLabel.setBorder(BorderFactory.createEmptyBorder());
      levelLabel.setForeground(Color.WHITE);
      levelLabel.setBackground(Color.BLACK);
      levelLabel.setFont (new Font("sansserif", Font.BOLD, 24));
      levelLabel.setHorizontalAlignment(JTextField.CENTER);
      levelLabel.setSize(200,30);
      levelLabel.setLocation(800, 500 );
      levelLabel.requestFocus(true);
      
      lifeTotalLabel = new JTextField( String.valueOf( "LIFE: " + lifeTotal ) );
      lifeTotalLabel.setEditable(false);
      lifeTotalLabel.setHighlighter(null);
      lifeTotalLabel.setBorder(BorderFactory.createEmptyBorder());
      lifeTotalLabel.setForeground(Color.WHITE);
      lifeTotalLabel.setBackground(Color.BLACK);
      lifeTotalLabel.setFont (new Font("sansserif", Font.BOLD, 24));
      lifeTotalLabel.setHorizontalAlignment(JTextField.CENTER);
      lifeTotalLabel.setSize(140,30);
      lifeTotalLabel.setLocation(0, 500 );
      lifeTotalLabel.requestFocus(true);
      
      gameOverLabel = new JLabel( new ImageIcon( "images/gameOver.png" ) );
      gameOverLabel.setSize( 400, 177 );
      gameOverLabel.setLocation( 300, 180 );
            
      frame.getContentPane().add( BorderLayout.CENTER, panel );
      frame.setVisible(true);
      
      // previous bug: clicking in a text field while the game is running
      // takes the focus off Robotron rendering it unmovable.
      //Fix:  requestFocus( true) on all text fields
      frame.addMouseListener( this );
      frame.addKeyListener(this);
      frame.setFocusable(true);
      frame.setFocusTraversalKeysEnabled(false);   
      
      }
   
     
   /**The 'k' serves as the key pressed by the user which is then compared to 
     * up/w, left/a, right/d, and down/s to call movement. The player's velocity is set with
    * respect to the current speed (so as to only change the speed with regards to the key 
    *  being pressed and not override the players current movement) and roboSpeed now set to 2. 
    */  
   @Override
   public void keyPressed(KeyEvent e) {
      int keyCode = e.getKeyCode();
      pressedKeys.add(keyCode);
      updatePlayerVelocity();
   }
 
   /**This method is necessary so when keys are no longer pressed the player stops.
     * The velocity is not compeletly set to zero in the case that the user is holding down two keys
    * and only releases one.
    */ 
   @Override
   public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();
      pressedKeys.remove(keyCode);
      updatePlayerVelocity();
   }
 
   @Override
   public void keyTyped(KeyEvent e) {
         // nothing required for typed character but necessary for keyevent interface
         return;
   }
   
   /*adjusts players speed based on which key was released. */
   private void updatePlayerVelocity() {
      int velocityX = 0;
      int velocityY = 0;

      // Determine player velocity based on currently pressed keys
      if (pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_W)) {
          velocityY -= roboSpeed;
      }
      if (pressedKeys.contains(KeyEvent.VK_DOWN) || pressedKeys.contains(KeyEvent.VK_S)) {
          velocityY += roboSpeed;
      }
      if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A)) {
          velocityX -= roboSpeed;
      }
      if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) {
          velocityX += roboSpeed;
      }

      player.setVelocity(velocityX, velocityY);
   }

   /** For each mouse click, a new bullet is added to the ArrayList roboBullets at Robotron's location.
     * Then, the x distance and the y distance of the click from robotron
    * is plugged into the distance formula which outputs the length from the bullet to Robotron. The length is scaled
    * to velocity x and y which are unit vectors of the bullets, or in other words, their directions/magnitudes. Finally,
    * the bullet is given a speed in that unit vector direction.
    */
    public void mousePressed(MouseEvent e) 
      {  
      int xMouse = e.getX();
      int yMouse = e.getY() -32;
      //subtracting 32 fixes bug where the click doesn't correspond to getmouseY.
      //this probably happens because the coordinates of the jframe don't match
      //those on jpanel because jpanel has a bar on the top of the screen which I figure takes up 32 pixles. 
      double playerX = player.getPositionX();
      double playerY = player.getPositionY();
      double bulletX = playerX + player.getWidth()/2 - 3; // 3 is the center of the bullet
      double bulletY = playerY + player.getHeight()/2 - 3;
      
      roboBullets.add( new Bullet( bulletX, bulletY, getBoard() ));
      
      int lastBullet = roboBullets.size() - 1;
      double distanceX = xMouse - bulletX;
      double distanceY = yMouse - bulletY;
      double speed = 5;
      //speed is the number of pixels the bullet moves each actionlistener cycle;
      double length = Math.sqrt( (distanceX*distanceX) + (distanceY*distanceY));
      double velocityY = distanceY/length * speed;
      double velocityX = distanceX/length * speed;

                  
      roboBullets.get(lastBullet).setVelocity( velocityX, velocityY ); 
      }
    
   public void mouseReleased(MouseEvent e) 
      {
      return;
      }
    
   public void mouseEntered(MouseEvent e) 
      {
      return;
      }
    
   public void mouseExited(MouseEvent e) 
      {
      return;
      }
    
   public void mouseClicked(MouseEvent e) 
      {
      return;
      }

   /** This is the most important method of the game that uses two actionListeners to run the animation
    * of the game. The main actionListener cycles every 10 milliseconds at which all the characters on the board
    * move methods are run. Then, once everything has been moved and contact acccounted for, redraw is called  which adds every
    * JLabel in the game board to their new moved positions. The other actionlistener is used as a timer, so if shooting 
    * enemies exist, they shoot each cycle ( 3 seconds). While all types of contact (robo/enemy, robo/bullet, 
    * enemy/bullet, bullet/edge) are checked in other classes' methods, runLevel sends an arrayList to these
    * methods which return the location of the contacted character in the arraylist and this method takes care of the
    * removal, checks if the characters still have lives, and calls gameover.
    */   
   public void run()
      {
      ActionListener al = new ActionListener()
         {
         public void actionPerformed( ActionEvent e )
            {
            if( levelInProgress )
               {

               //move method adds the speed of the enemy (different depending on the enemy) to its position
               //the move method in the enemey class uses a simple AI to move enemies closer to the player by getting 
               //the player's x,y location
               for( Enemy drawEnemy : allEnemies ) 
                  {
                  int edgeDetect = drawEnemy.move();
                  //drawEnemy.preventCrowding(allEnemies);
                  
                  if(drawEnemy.getEnemyType() == 5 || drawEnemy.getEnemyType() == 6  )
                     {
                     if( edgeDetect == 1 || edgeDetect == 2)
                        {
                        drawEnemy.setVelocityX( -drawEnemy.getVelocityX());
                        }
                     if( edgeDetect == 3 || edgeDetect == 4)
                        {
                        drawEnemy.setVelocityY( -drawEnemy.getVelocityY());
                        }
                     }
                    
                  }  
             
               // removes robobullets that have hit the edge
               for( int index = 0; index < roboBullets.size(); index++ )
                  {
                   if( roboBullets.get(index).move() > 0 )
                      {
                      roboBullets.remove(index);
                      index--;
                      }
                  }
               
               // checks for contact between playerbullets (robobullets) and all the enemies
               // if contact, then the life totals of the enemies are subtracted, hit enemies are removed, 
               //level promotion is checked, and the score value of the enemy added to the player's score.
               for( int index = 0; index < roboBullets.size(); index++ )
                  { 
                   int hitEnemyIndex = roboBullets.get(index).detectEnemy( allEnemies );
                   if( hitEnemyIndex >= 0 )
                      {
                       roboBullets.remove(index);
                       index--;
                       int enemyLife = allEnemies.get(hitEnemyIndex).getLives();
                       enemyLife--;
                       if( enemyLife == 0 )
                          {
                          score += allEnemies.get(hitEnemyIndex).getScoreValue();
                          allEnemies.remove(hitEnemyIndex);
                          checkLevelPromotion();
                          scoreLabel.setText(String.valueOf( "SCORE " + score ));
                          }
                       else
                          {
                          allEnemies.get(hitEnemyIndex).setLives(enemyLife);
                          }
                      }
                  }   
               
               //removes enemy bullets if they are out of bounds (i.e. they have hit the edge)
               for( int index = 0; index < enemyBullets.size(); index++ )
                   {
                   if( enemyBullets.get(index).move() > 0 )
                     {
                     enemyBullets.remove(index);
                     index--;
                     }
                  }
               
               //for contact between player and enemy bullets
               int enemyBulletIndex = player.detectBullet( enemyBullets );
               if( enemyBulletIndex >= 0 )
                  {
                  enemyBullets.remove(enemyBulletIndex);
                  lifeTotal--;
                  isPlayerHit = true;
                  lifeTotalLabel.setText(String.valueOf( "LIVES " + lifeTotal ));
                  //player.setPosition( (double) screenWidth/2, (double) screenHeight/2 );
                  if( lifeTotal == 0 )
                     {
                     gameOver();
                     }

                  }
               
               //for contact between enemy squares and player
               int enemyIndex = player.detectEnemy( allEnemies );
               if( enemyIndex >= 0)
                  {
                  isPlayerHit = true;
                  lifeTotal--;
                  lifeTotalLabel.setText(String.valueOf( "LIVES " + lifeTotal ));
                  //player.setPosition( (double) screenWidth/2, (double) screenHeight/2 );
                  if( lifeTotal == 0 )
                     {
                     gameOver();
                     }
                  else
                     {
                     //System.out.println("Hit by enemy #"+ enemyIndex );
                     score += allEnemies.get(enemyIndex).getScoreValue();
                     scoreLabel.setText(String.valueOf( "SCORE " + score ));
                     allEnemies.remove(enemyIndex);
                     checkLevelPromotion();
                     }
                  }
               //System.out.println("ActionListener Running for: " + ++time + " centiseconds" );   
               player.move();
               redraw();
               }
            else
               {
               gameOver();
               }
            }// end timerTest
            
      };
      
      //Since I didn't want enemy bullets every 10 milliseconds, a second actionlistener is used so enemies that can shoot
      // can do so every cycle of the second action listener currently set to 3 seconds (3000 centiseconds).
      ActionListener a2 = new ActionListener()
      {
         public void actionPerformed( ActionEvent e2 )
            {
            giveEnemyBullets();
            }
      };

      ActionListener a3 = new ActionListener()
      {
         public void actionPerformed( ActionEvent e3 )
            {
            for( Enemy e: allEnemies)
               {
               if( e.getEnemyType() == 5 || e.getEnemyType() == 6) 
                  {
                     e.randomMove();
                  }
               }
            }
      };

      gameTimer = new javax.swing.Timer( 10, al );
      bulletTimer = new javax.swing.Timer( 3000, a2 );
      moveTimer = new javax.swing.Timer( 2500 , a3);
      
      moveTimer.start(); 
      gameTimer.start();
      bulletTimer.start();
      }
   
     
    /** This is called everytime the first ActionListener cycles. Redraw() "updates" all the 
    * JLabels to match their moved location changed in runLevel and also paints
    * the score, robotron's life total, the level number, the red background when the player is hit, and, when
    * the game is over, the gameover JLabel.
    **/   
   public void redraw()
      {
      panel.removeAll();
      panel.setBackground (Color.black);
      
      if( isPlayerHit )
         {
         //Sometimes when the player is hit the screen doesn't flash red since the background transition is too fast and unnoticible
         panel.setBackground (Color.red);
         //System.out.println( "\t" + "PLAYER HIT" );
         isPlayerHit = false;
         }
      
      for ( Enemy e : allEnemies )
         {
         panel.add( e.getJLabel() );
         } // end for
      
      for ( Bullet b : roboBullets )
         {
         panel.add( b.getJLabel() );
         }
      
      for ( Bullet b : enemyBullets )
         {
         panel.add( b.getJLabel() );
         }
   
      panel.add( player.getJLabel()); 
      panel.add( scoreLabel );
      panel.add( levelLabel );
      panel.add( lifeTotalLabel );
      
      if( isGameOver )
         {
         panel.removeAll();
         panel.setBackground (Color.black);
         panel.add( gameOverLabel);
         }
     
      panel.repaint( 0, 0, screenWidth, screenHeight );
      }
   
   public JFrame getFrame()
      {
      return frame;
      }
   
   /** This getter is necessary so that level can update the enemy arrayList with either
    * a preset or a random list. 
    * @return The board's arrayList of enemies called allEnemies.
    */  
   public ArrayList<Enemy> getEnemies()
      {
      return allEnemies;
      }
   
   /** This getter is used so that bullets can be put on a board since they are added in an
    * inner class mouseListener
    * @return The board class
    */  
   public Board getBoard()
      {
       return this;
      } // for get Board;
   
   public int getRobotronSize()
      {
      return roboSize;
      }
   
   public int getEnemySize()
      {
      return enemySize;
      }

   public int getScreenHeight()
      {
      return screenHeight;
      }
      
   public int getLabelSize()
      {
      return labelSize;
      }

   public int getScreenWidth()
      {
      return screenWidth;
      }
      
   /** This getter is used by the enemy class so that enemies of a certain type
    * can track down the location of the player.
    * @return Robotron player, so that his position can be known by enemies
    */  
   public Robotron getPlayer()
      {
      return player;
      }

   /** Only is called when the player's life total is zero. Both timers are stopped and
    * the isGameOver is set to true so that the redraw method knows to add the gameover
    * image. 
    */     
   public void gameOver()
      {
       gameTimer.stop();
       bulletTimer.stop();
       isGameOver = true;
       levelInProgress = false;
       System.out.println("Game Over: " + "Final Score: " + score + " Level achieved: " + level.getLevel() );
      }
   
   /** Checks the board to see if any enemies are remaining in the arrayList. If not
    * a new level is generated and the player is set to the middle of the frame as to 
    * not be spawned untop of an enemy.
    */
   public void checkLevelPromotion()
      {
      if( allEnemies.size() == 0)
         {      
         level.newLevel();
         levelLabel.setText( String.valueOf("LEVEL: " + level.getLevel() ) );
         player.setPosition( (double) screenWidth/2 - player.getWidth()/2, (double) screenHeight/2 - player.getHeight()/2 );
         }
      }
      
   /** This applies the same logic as in the mouseListner method except instead of using the mouse click's x,y point
    * as the endpoint of the bullet, the players x,y location is used. This is in board because the other clases
    * such as bullet don't know about the arraylist of enemies or the location of robotron. The ability to shoot is given 
    * to enemy type 4 and 3 who shoot every cycle of the second actionlistener used in runlevel
    */ 
   public void giveEnemyBullets()
      {
      for( Enemy e : allEnemies )
         {
         if( e.getEnemyType() == 3 ||  e.getEnemyType() == 4 || e.getEnemyType() == 5) 
            {
            double playerX = player.getPositionX();
            double playerY = player.getPositionY();
            double enemyX = e.getPositionX();
            double enemyY = e.getPositionY();
            
            double bulletX = enemyX + e.getWidth()/2 - 3;
            double bulletY = enemyY + e.getHeight()/2 - 3;
            enemyBullets.add( new Bullet( bulletX, bulletY, getBoard() ));
            int lastBullet = enemyBullets.size() - 1;
            double distanceX = playerX - bulletX;
            double distanceY = playerY - bulletY;
            double speed = 5;
            //speed is the number of pixels the bullet moves each cycle;
            double length = Math.sqrt( (distanceX*distanceX) + (distanceY*distanceY));
            double velocityY = distanceY/length * speed;
            double velocityX = distanceX/length * speed;
            enemyBullets.get(lastBullet).setVelocity( velocityX, velocityY ); 
            }
         }
      }   
   }
