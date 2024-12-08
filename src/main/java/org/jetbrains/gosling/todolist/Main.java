package org.jetbrains.gosling.todolist;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TodoList todoList = new TodoList();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("\n=== Todo List Menu ===");
            System.out.println("1. Add a Todo");
            System.out.println("2. View All Todos");
            System.out.println("3. Mark a Todo as Completed");
            System.out.println("4. Delete a Todo");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter the description of the Todo: ");
                    String description = scanner.nextLine();
                    todoList.addItem(description);
                    System.out.println("Todo added successfully!");
                    break;

                case 2:
                    todoList.listItems();
                    break;

                case 3:
                    System.out.print("Enter the ID of the Todo to toggle completed: ");
                    int todoId = scanner.nextInt();
                    todoList.markComplete(todoId);
                    System.out.println("ALL ITEMS:");
                    todoList.listItems();
                    break;

                case 4:
                    System.out.print("Enter the ID of the Todo to delete: ");
                    int deleteId = scanner.nextInt();
                    TodoItem itemToDelete = todoList.getItemById(deleteId);
                    if (itemToDelete != null) {
                        todoList.removeItem(deleteId);
                        System.out.println("Todo with description: " + itemToDelete.getDescription() + " is now deleted");
                    } else {
                        System.out.println("Todo with ID " + deleteId + " does not exist.");
                    }
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
