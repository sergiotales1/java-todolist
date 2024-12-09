package org.jetbrains.gosling.todolist;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import java.io.*;
import java.nio.file.Files;
import java.util.List;


class TodoHandler implements HttpHandler {
    private TodoList todoList;
    private Gson gson;

    public TodoHandler(TodoList todoList) {
        this.todoList = todoList;
        this.gson = new Gson();

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String reqPath = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (reqPath.equals("/")) {
            reqPath = "/index.html";
            handleHome(exchange, reqPath);
        } else if (reqPath.equals("/api/todos")){
            todoList.addItem("test");
            handleTodosApi(exchange, method);
        }
        else {
            handleHome(exchange, reqPath);
        }
    }

    private void handleHome(HttpExchange exchange, String reqPath) throws IOException {
        String filePath = "src/main/resources/static" + reqPath;
        File file = new File(filePath);
        if (file.exists()) {
            String mimeType = Files.probeContentType(file.toPath());
            byte[] content = Files.readAllBytes(file.toPath());

            exchange.getResponseHeaders().set("Content-Type", mimeType);
            exchange.sendResponseHeaders(200, content.length);
            OutputStream os = exchange.getResponseBody();
            os.write(content);
            os.close();
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleTodosApi(HttpExchange exchange, String method) throws IOException {
        if (method.equals("GET")) {
            List<TodoItem> todos = todoList.getItems();
            String todosJson = gson.toJson(todos);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, todosJson.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(todosJson.getBytes());
            }
        } else {
            sendMethodNotAllowed(exchange);
        }
    }

    private void sendNotFound(HttpExchange exchange) throws IOException {
        String response = "404 File Not Found";
        exchange.sendResponseHeaders(404, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void sendMethodNotAllowed(HttpExchange exchange) throws IOException {
        String response = "405 Method Not Allowed";
        exchange.sendResponseHeaders(405, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

}
