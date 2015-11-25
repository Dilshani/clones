/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.extrogene;



import com.extrogene.model.AppConstant;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Lahiru_Ruwan
 */
public class Util {
    private static final AppConstant APP_CONSTANTS = new AppConstant();
    public Connection GetConnection(){
    Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            if(APP_CONSTANTS.IS_RELEASE)
                conn = DriverManager.getConnection("jdbc:mysql://localhost/movie_plus", "extrogene", "Ex+7o9e^E");
            else
                conn = DriverManager.getConnection("jdbc:mysql://localhost/televisionary", "root", "");
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return conn;
    }
}
