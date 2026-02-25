// ChatServer.java
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;


//todo open ngrok dir in terminal with "cd <path>"
//todo start ngrok with "ngrok tcp <localport>" (local port is 12345 by default bc i am so creative)

public class ChatServer {
    private static final int LOCAL_PORT = 12345; // this is the localhost port of the server that ngrok links to
    private static Set<PrintWriter> clientWriters = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(LOCAL_PORT);
        System.out.println("Server listening on port " + LOCAL_PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            clientWriters.add(out);
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection lost.");
        } finally {
            clientWriters.removeIf(w -> w.checkError()); // Clean up dead connections
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}
