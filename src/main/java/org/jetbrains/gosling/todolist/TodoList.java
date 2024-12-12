package org.jetbrains.gosling.todolist;

import java.util.ArrayList;
import java.util.List;

public class TodoList {
    private List<TodoItem> items;
    private int nextId;

    public TodoList() {
        this.items = new ArrayList<>();
        this.nextId = 1;
    }

    public int addItem(String description) {
        TodoItem todoItem = new TodoItem(nextId, description, false);
        items.add(todoItem);
        nextId++;
        return nextId-1;
    }

    public void removeItem(int id) {
        items.removeIf(item -> item.getId() == id);
    }

    public TodoItem getItemById(int id) {
        for (TodoItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void markComplete(int id) {
        for (TodoItem item : items) {
            if (item.getId() == id) {
                item.toggleComplete();
            }
        }
    }

    public List<TodoItem> getItems() {
        return items;
    }

    public void setItems(List<TodoItem> items) {
        this.items = items;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public void listItems() {
        if (items.isEmpty()) {
            System.out.println("No tasks available");
        } else {
            for (TodoItem item : items) {
                String status = item.isDone() ? "Completed" : "Not Completed";
                System.out.println("ID: " + item.getId() + " - " + item.getDescription() + " (" + status + ") ");
            }
        }
    }

}
