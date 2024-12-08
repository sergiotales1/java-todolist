package org.jetbrains.gosling.todolist;

public class TodoItem {
    private int id;
    private boolean isCompleted;
    private String description;

    public TodoItem(int id, String description, boolean isCompleted) {
        this.id = id;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    public void toggleComplete(){
        this.isCompleted = !this.isCompleted;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
