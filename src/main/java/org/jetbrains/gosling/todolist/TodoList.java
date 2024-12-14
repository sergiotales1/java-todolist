package org.jetbrains.gosling.todolist;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class TodoList {
    private List<TodoItem> items;

    public TodoList() {
        this.items = new ArrayList<>();
    }

    public int addItem(String description) {
        String sql = "INSERT INTO todos (description, is_done) VALUES (?, 0)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, description);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
        }
        return -1;
    }

    public void removeItem(int id) {
        String sql = "DELETE FROM todos WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing item: " + e.getMessage());
        }
    }

    public TodoItem getItemById(int id) {
        String sql = "SELECT * FROM todos WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TodoItem(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getInt("is_done") == 1
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting item: " + e.getMessage());
        }
        return null;
    }

    public List<TodoItem> getItems() {
        String sql = "SELECT * FROM todos";
        List<TodoItem> items = new ArrayList<>();
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new TodoItem(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getInt("is_done") == 1
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting items: " + e.getMessage());
        }
        return items;
    }

    public TodoItem editTodo(int id, String newDescription, boolean isDone) {
        String selectSql = "SELECT * FROM todos WHERE id = ?";
        String updateSql = "UPDATE todos SET description = ?, is_done = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect()) {
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setInt(1, id);
                try (ResultSet rs = selectPstmt.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                            updatePstmt.setString(1, newDescription);
                            updatePstmt.setInt(2, isDone ? 1 : 0);
                            updatePstmt.setInt(3, id);
                            updatePstmt.executeUpdate();
                        }

                        return new TodoItem(
                                rs.getInt("id"),
                                newDescription,
                                isDone
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error editing todo item: " + e.getMessage());
        }

        return null; // Return null if no todo item was found or an error occurred
    }


}
