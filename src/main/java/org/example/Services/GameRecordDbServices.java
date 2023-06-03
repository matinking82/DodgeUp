package org.example.Services;

import org.example.Main;
import org.example.Models.GameRecord;
import org.example.gameObjects.Block;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameRecordDbServices {
    private Connection connection;

    public GameRecordDbServices() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            // db parameters
            String url = "jdbc:sqlite:sqlite/db/office.db";
            // create a connection to the database
            connection = DriverManager.getConnection(url);

            String gameRecord = "CREATE TABLE IF NOT EXISTS GameRecord ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "date VARCHAR(50),"
                    + "points INTEGER"
                    + ");";

            String block = "CREATE TABLE IF NOT EXISTS Block (" +
                    "`check` BOOLEAN NOT NULL," +
                    "x INT NOT NULL," +
                    "y INT NOT NULL," +
                    "width INT NOT NULL," +
                    "height INT NOT NULL," +
                    "r INT NOT NULL," +
                    "g INT NOT NULL," +
                    "b INT NOT NULL" +
                    ")";

            String gameSave = "CREATE TABLE IF NOT EXISTS GameSave (" +
                    "itemCheck BOOLEAN NOT NULL," +
                    "points INT NOT NULL," +
                    "lives INT NOT NULL," +
                    "shields INT NOT NULL," +
                    "speed INT NOT NULL," +
                    "lastX INT NOT NULL" +
                    ")";

            var sql = connection.createStatement();
            sql.execute(gameRecord);
            sql.execute(block);
            sql.execute(gameSave);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private int getLastId() {
        int lastId = -1;
        try {
            // Prepare the SQL statement
            String sql = "SELECT MAX(id) AS MaxId FROM GameRecord;";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Execute the SQL statement
            ResultSet rs = statement.executeQuery();

            // Check if the result set has a row
            if (rs.next()) {
                // Get the value of the MaxId column
                lastId = rs.getInt("MaxId");
            }
        } catch (SQLException e) {
            System.out.println("Error getting last ID: " + e.getMessage());
        }
        return lastId;
    }
    public void add(GameRecord record) {
        record.setId(getLastId()+1);
        try {
            String query = "INSERT INTO GameRecord (date, points, id) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, record.getDate());
            statement.setInt(2, record.getPoints());
            statement.setInt(3, record.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<GameRecord> getTopFive() {
        List<GameRecord> records = new ArrayList<>();
        try {
            String query = "SELECT * FROM GameRecord ORDER BY points DESC LIMIT 5";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                GameRecord record = new GameRecord();
                record.setId(resultSet.getInt("id"));
                record.setDate(resultSet.getString("date"));
                record.setPoints(resultSet.getInt("points"));
                records.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return records;
    }

    public GameRecord getById(int id) {
        GameRecord record = null;
        try {
            String query = "SELECT * FROM GameRecord WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                record = new GameRecord();
                record.setId(resultSet.getInt("id"));
                record.setDate(resultSet.getString("date"));
                record.setPoints(resultSet.getInt("points"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return record;
    }

    public List<Block> LoadGame(){
        List<Block> blocks = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Block";

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                boolean check = rs.getBoolean("check");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int width = rs.getInt("width");
                int height = rs.getInt("height");
                int r = rs.getInt("r");
                int g = rs.getInt("g");
                int b = rs.getInt("b");
                Block block = new Block(width,height,x,y,r,g,b,check);

                blocks.add(block);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "SELECT * FROM GameSave";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Main.itemCheck = rs.getBoolean("itemCheck");
                Main.lastBlockX = rs.getInt("lastX");
                Main.speed = rs.getInt("speed");
                Main.points = rs.getInt("points");
                Main.lives = rs.getInt("lives");
                Main.shield = rs.getInt("shields");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return blocks;
    }

    public void addBlock(Block block){
            try {

                String sql = "INSERT INTO Block (`check`, x, y, width, height, r, g, b) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);

                // Set the parameter values for the prepared statement
                stmt.setBoolean(1, block.check);
                stmt.setInt(2, block.x);
                stmt.setInt(3, block.y);
                stmt.setInt(4, block.width);
                stmt.setInt(5, block.height);
                stmt.setInt(6, block.r);
                stmt.setInt(7, block.g);
                stmt.setInt(8, block.b);

                stmt.executeUpdate();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
    }
    public void SaveGame(){
        try {
            String sql = "INSERT INTO GameSave (itemCheck, lastX, speed, points, lives, shields) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            // Set the parameter values for the prepared statement
            stmt.setBoolean(1, Main.itemCheck);
            stmt.setInt(2, Main.lastBlockX);
            stmt.setInt(3, Main.speed);
            stmt.setInt(4, Main.points);
            stmt.setInt(5, Main.lives);
            stmt.setInt(6, Main.shield);

            stmt.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void deleteAllBlocks(){
        try {
            String sql = "DELETE FROM Block";
            String sql2 = "DELETE FROM GameSave";

            PreparedStatement stmt = connection.prepareStatement(sql);
            PreparedStatement stmt2 = connection.prepareStatement(sql2);

            stmt.executeUpdate();
            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}