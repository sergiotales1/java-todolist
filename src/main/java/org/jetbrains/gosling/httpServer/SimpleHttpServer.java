package org.jetbrains.gosling.httpServer;

import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new StaticHandler());

        server.start();
        System.out.println("Server started at http://localhost:8080");
    }

    static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestedFile = exchange.getRequestURI().getPath();
            if (requestedFile.equals("/")) {
                requestedFile = "/index.html";
            }

            String filePath = "src/main/resources/static" + requestedFile;
            File file = new File(filePath);

            if (file.exists()) {
                String mimeType = Files.probeContentType(file.toPath());
                System.out.println(mimeType);
                byte[] content = Files.readAllBytes(file.toPath());

                exchange.getResponseHeaders().set("Content-Type", mimeType);
                exchange.sendResponseHeaders(200, content.length);
                OutputStream os = exchange.getResponseBody();
                os.write(content);
                os.close();
            } else {
                // If file is not found, return 404 error
                String response = "File not found";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
