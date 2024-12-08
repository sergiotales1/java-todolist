package org.jetbrains.gosling.sqlitetuorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    // jdbc:sqlite will tell the DriverManager to implement the dependency that we installed searching for it
    // GPT: It then searches for a driver (from the installed dependencies) that can handle the sqlite protocol.
    private static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/chinook.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS warehouses (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "capacity REAL" +
                ");";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'warehouses' created or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    public static void insertData() {
        String sql = "INSERT INTO warehouses(name, capacity) VALUES(?, ?)";
        String[] names = {"Raw Materials", "Semifinished Goods", "Finished Goods"};
        double[] capacities = {3000.0, 4000.0, 5000.0};

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < names.length; i++) {
                pstmt.setString(1, names[i]);
                pstmt.setDouble(2, capacities[i]);
                pstmt.executeUpdate();
            }
            System.out.println("Data inserted successfully.");

        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }
    }

    public static void updateData() {
        String sql = "UPDATE warehouses SET name = ? , "
                + "capacity = ? "
                + "WHERE id = ?";
        String newName = "Semifinished Products";
        int capacity = 3000;
        int id = 2;

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newName);
            pstmt.setInt(2, capacity);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();

            System.out.println("Data updated successfully.");

        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }


    }

    public static void main(String[] args) {
        createTable();
//        insertData();
        updateData();
    }
}
