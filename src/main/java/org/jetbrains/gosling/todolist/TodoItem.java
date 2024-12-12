package org.jetbrains.gosling.todolist;

public class TodoItem {
    private int id;
    private boolean isDone;
    private String description;

    public TodoItem(int id, String description, boolean isCompleted) {
        this.id = id;
        this.description = description;
        this.isDone = isCompleted;
    }

    public void toggleComplete(){
        this.isDone = !this.isDone;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
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
