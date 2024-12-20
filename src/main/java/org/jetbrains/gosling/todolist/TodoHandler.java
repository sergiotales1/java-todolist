package org.jetbrains.gosling.todolist;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        } else if (reqPath.equals("/api/todos")) {
            handleTodosApi(exchange, method);
        } else if (reqPath.contains("/edit.html")) {
            handleEdit(exchange);
        } else if (reqPath.contains("/api/todo")) {
            handleSingleTodoApi(exchange, method);
        } else {
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
            return;
        }
        if (method.equals("POST")) {
            // read data from client
            InputStream requestBody = exchange.getRequestBody();
            Reader jsonReader = new InputStreamReader(requestBody);
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();

            // extract description and create todo
            String description = jsonObject.get("description").getAsString();
            int id = todoList.addItem(description);

            // structure the response to client
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("id", id);
            String jsonResponse = gson.toJson(responseJson);

            // send response
            exchange.sendResponseHeaders(201, jsonResponse.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonResponse.getBytes());
            }
            return;
        }
        sendMethodNotAllowed(exchange);
    }

    private void handleSingleTodoApi(HttpExchange exchange, String method) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        if (method.equals("GET")) {
            int todoId = Integer.parseInt(parts[3]);
            TodoItem todoItem = todoList.getItemById(todoId);

            if (todoItem != null) {
                String jsonResponse = gson.toJson(todoItem);

                try (OutputStream os = exchange.getResponseBody();) {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os.write(jsonResponse.getBytes());
                }
            } else {
                sendNotFound(exchange);
            }
        }
        if (method.equals("PATCH")) {
            // read data from client
            InputStream requestBody = exchange.getRequestBody();
            Reader jsonReader = new InputStreamReader(requestBody);
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();

            // extract description and create todo
            String description = jsonObject.get("description").getAsString();
            boolean isDone = jsonObject.get("isDone").getAsBoolean();
            int id = jsonObject.get("parsedId").getAsInt();

            todoList.editTodo(id, description, isDone);

            // structure the response to client
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("id", id);
            String jsonResponse = gson.toJson(responseJson);

            // send response
            exchange.sendResponseHeaders(201, jsonResponse.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonResponse.getBytes());
            }
            return;
        }
        if (method.equals("DELETE")) {
            InputStream requestBody = exchange.getRequestBody();
            Reader jsonReader = new InputStreamReader(requestBody);
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();

            int id = jsonObject.get("id").getAsInt();
            todoList.removeItem(id);

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("id", id);
            String jsonResponse = gson.toJson(responseJson);

            exchange.sendResponseHeaders(201, jsonResponse.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonResponse.getBytes());
            }
            return;
        }
    }

    private void handleEdit(HttpExchange exchange) throws IOException {
        String filePath = "src/main/resources/static/";
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        if (parts[2].contains("css") || parts[2].contains("js")) {
            filePath = filePath + parts[2];
        } else {
            filePath = filePath + parts[1];
        }

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
