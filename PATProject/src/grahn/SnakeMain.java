/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grahn;

import javax.swing.JFrame;
import java.awt.EventQueue;



public class SnakeMain extends javax.swing.JFrame {

    
    
    public SnakeMain() {
        
        
       
        add(new GameBoard());                                                   //Initializes starts the game board (aka the game)
      
        dispose();                                                              // resizes and centres the JFrame
        setUndecorated(true);                                                   //Thanks to Mathew Broderick
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                              
       
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            @Override                                                    
            public void run() {                
                JFrame ex = new SnakeMain();
                ex.setVisible(true);                
            }
        });
    }
}
