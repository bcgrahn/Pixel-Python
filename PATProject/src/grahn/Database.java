/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grahn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Bryce Tristan
 */
public class Database {

    public static Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    static public Connection conn;

    public Database(String databaseName)                                         //Forms the link between the interface and the database. receives the database name
    {
        String directory = System.getProperty("user.dir");

        String url = directory + "\\" + databaseName;

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            connection = DriverManager.getConnection("jdbc:ucanaccess://" + url + ".accdb");
            System.out.println("CONNECTED");

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Database not found");

        } catch (ExceptionInInitializerError ex) {
            System.out.println("ERROR");
            ex.getCause();

        }
    }

    public ResultSet query(String stmt) throws SQLException                     //Used for fetchig data from the database. recieves query                     // Mathew Broderick
    {
        statement = connection.prepareStatement(stmt);
        resultSet = statement.executeQuery();
        System.out.println(resultSet);
        return resultSet;
    }

    
    public void change(String changeIn) throws SQLException                     //used for removing or inserting data from or to the database. revieves query
    {
        statement = connection.prepareStatement(changeIn);
        statement.executeUpdate();
        statement.close();
    }

}
