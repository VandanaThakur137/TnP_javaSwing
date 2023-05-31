/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trandpl.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Sir2
 */
public class DBConnection {
    
    
                    
                   // conn=DriverManager.getConnection("jdbc:oracle:thin:@//DESKTOP-DNL6BE8:1521/XE", "tnp", "project");
    private final static String DB_URL="jdbc:oracle:thin:@//DESKTOP-DNL6BE8:1521/XE";
    private final static String DB_USER_NAME="tnp";
    private final static String DB_PASS="project";
private static Connection conn=null;
   static 
   {
       try
       {
           Class.forName("oracle.jdbc.OracleDriver");
           conn=DriverManager.getConnection(DB_URL ,DB_USER_NAME,DB_PASS);

       }
       catch(SQLException ex)
       {
           ex.printStackTrace();
           System.out.println("Error in DBConnection :- ");
       }
       catch(ClassNotFoundException ex)
       {
           ex.printStackTrace();
           System.out.println("Error in DBConnection :- ");
       }
   }

   public static Connection getConnection()throws SQLException
   {
       return conn;
   }

   public static void closeConnection()
   {
       try
       {    
           if(conn!=null)
                conn.close();
       }
       catch(SQLException ex)
       {
           ex.printStackTrace();
           System.out.println("Error while Closing Conn ");
       }

   }

}
