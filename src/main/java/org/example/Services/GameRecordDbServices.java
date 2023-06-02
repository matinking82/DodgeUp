package org.example.Services;

import org.example.Models.GameRecord;

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

            String query = "CREATE TABLE IF NOT EXISTS GameRecord ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "date VARCHAR(50),"
                    + "points INTEGER"
                    + ");";

            var sql = connection.createStatement();
            sql.execute(query);

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

    public List<GameRecord> getAll() {
        List<GameRecord> records = new ArrayList<>();
        try {
            String query = "SELECT * FROM GameRecord";
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
}