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
    // Fields to store the connection and statement objects
    private Connection connection;
    private Statement statement;

    // Constructor to establish the database connection
    public Conn() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "SHRU@04");
            
            // Create a statement object for executing SQL queries
            statement = connection.createStatement();
        } catch (Exception e) {
            // Print any exceptions that occur during connection setup
            e.printStackTrace();
        }
    }
    
    // Getter methods to retrieve the connection and statement objects
    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }
}
