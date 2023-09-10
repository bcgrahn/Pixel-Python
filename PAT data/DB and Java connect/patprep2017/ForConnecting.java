/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patprep2017;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dvythilingam
 */
public class ForConnecting {
  static public Connection conn;
  
  public ForConnecting() 
   {
       String workingDir = System.getProperty("user.dir");
	  
          String accessFileName=workingDir+"\\\\Jeppe2";
                   System.out.println("Current working directory : " + accessFileName);
       
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                     
           conn = DriverManager.getConnection("jdbc:ucanaccess://"+accessFileName+".accdb");
            System.out.println("connected");
        } catch (ClassNotFoundException ex) {
       //     Logger.getLogger(UseRunner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
        //    Logger.getLogger(UseRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
                } 
}
