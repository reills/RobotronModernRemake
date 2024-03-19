/**
 * Robotron
 * @author Stephen Reilly
 * @version 1.0.0
 */

import java.awt.*; 
import javax.swing.*;  
import java.awt.event.*; 

public class GameDriver implements ActionListener
   {   
   private JFrame frame; 
   
   public void go()
      {
      frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
      
      JButton button = new JButton("Click here to begin");
      button.addActionListener(this);
        
      JPanel panel = new JPanel();
      Icon image = new ImageIcon("images/RobotronScreen.png");

      JLabel label = new JLabel(image);
        
      panel.add(label);
       
        
      frame.getContentPane().add(BorderLayout.SOUTH, button);
      frame.getContentPane().add(BorderLayout.CENTER, panel);
      frame.setSize(750,750);
      frame.setVisible(true);
      
      }
   
   public void actionPerformed(ActionEvent evt) 
      {   
      Board game = new Board();
      game.run();
      frame.dispose();
      } // end method main
  
   public static void main(String[] args) 
      {
      GameDriver frame = new GameDriver();
      frame.go();
      }
      
   } // end method GameDriver