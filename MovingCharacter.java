import java.util.ArrayList;
/**
 * Interface Character Movement & Contact Detection
 * @author Stephen Reilly
 * @version 1.0.0
 */

public interface MovingCharacter
   {
   abstract int detectEnemy( ArrayList<Enemy> enemies );  // returns ArrayList index of contacted enemy
   abstract public int detectBullet( ArrayList<Bullet> bullets );  // returns ArrayList index of contacted bullet
   abstract public int move();
   } // end interface Moving Characters

