/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grahn;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Bryce Tristan
 */
public class useDatabase {

    private Database database = new Database("HighScoreDatabase");              //creates object of class database

  
    public String displaytblHighScore(String difficulty)                        //Used for fetching and sending across a string with the players and their scores according to the difficulty specified 
    {
        String temp = "";
        int pos = 0;

        try {

            ResultSet rs = database.query("SELECT TOP 20* FROM tblHighScore WHERE Difficulty = '" + difficulty + "'" + "ORDER BY Score DESC, FirstName");     //fetches the top 20 players occording to their score and difficuly level

            while (rs.next()) {
                pos++;
                String username = rs.getString("FirstName");
                int score = rs.getInt("Score");

                temp = temp + pos + "#" + username + "#" + score + "\n";
            }
            System.out.println("DATA EXTRACTED");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return temp;
    }

    public void UpdateTable(String name) {                                      //Uploads a new player, their score and difficulty to the table

        int score = fetchScore();
        String what = "Difficulty";
        String difficulty = fetchWhat(what);                                    //fetches difficulty uploaded to the database
        System.out.println(difficulty);
        String tbl = "";

        try {
            database.change("INSERT INTO tblHighScore (FirstName,Score,Difficulty)" + "VALUES ('" + name + "','" + score + "','" + difficulty + "')");  // Inserts a new players username and score into a specific database table indicated
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    

    public void InsertData(int id, int score1, String difficulty, String stat, String closeGame) //inserts vital game status information into tblData
    {

        try {
            database.change("INSERT INTO tblData(Id,Score,Difficulty,Status,Close)" + "VALUES ('" + id + "','" + score1 + "','" + difficulty + "','" + stat + "','"
                    + closeGame + "')");
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    public void UpdateScore(int score)                                          //used to update score to the database each time an apple is eaten
    {
        int id = 1;
        try {
            database.change("UPDATE tblData SET Score = '" + score + "'" + "WHERE ID = '" + id + "'");
            System.out.println("Score updated");
        } catch (SQLException ex) {
            System.out.println(ex);                             
        }

    }

    public void updateWhat(String what, String value)                           //update a value in a certain column (what) into tblData when required
    {
        int id = 1;
        try {
            database.change("UPDATE tblData SET " + what + " = '" + value + "'" + "WHERE ID = '" + id + "'");
            System.out.println("Status updated");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void deleteData()                                                    //delete tblData's information at the end of each game to ensure each new game doesn't mix data
    {
        int id = 1;

        try {
            database.change("DELETE FROM tblData WHERE ID = '" + id + "'");
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    public int fetchScore() {                                                   //fetches the score from tblData so that it can be saved into the highcore tables with the username

        int id = 1;
        int score = 0;

        try {

            ResultSet rs = database.query("SELECT Score FROM tblData WHERE ID = '" + id + "'");
            rs.next();

            score = rs.getInt("Score");

            System.out.println("DATA EXTRACTED");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return score;
    }

    public String fetchWhat(String what)                                        // fetches a value from  a certain column in tblData when required
    {
        String item = "";

        int id = 1;

        try {

            ResultSet rs = database.query("SELECT " + what + " FROM tblData WHERE ID = '" + id + "'");

            rs.next();
            item = rs.getString(what);
            System.out.println("Fetch Item : " + item);

            System.out.println("DATA EXTRACTED");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return item;
    }

}
