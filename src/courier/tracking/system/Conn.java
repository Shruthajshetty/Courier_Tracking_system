/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package courier.tracking.system;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conn {
   
    private Connection connection;
    private Statement statement;

    
    public Conn() {
        try {
          
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "SHRU@04");
            
           
            statement = connection.createStatement();
        } catch (Exception e) {
          
            e.printStackTrace();
        }
    }
    
 
    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }
}
