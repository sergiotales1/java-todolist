package org.jetbrains.gosling.todolist;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) throws IOException {
        TodoList todoList = new TodoList();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new TodoHandler(todoList));

        server.start();
        System.out.println("Server started at http://localhost:8080");
    }
}
