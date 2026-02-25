// ChatClient.java
import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(Main.SERVER_ADDRESS, Main.PORT);
            System.out.println("Connected to chat server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Thread to listen for messages from server
            new Thread(() -> {


                try {
                    Input inputHandler = new Input();
                    String incoming;
                    while ((incoming = in.readLine()) != null) {
                        System.out.println(incoming);
                        if (incoming.startsWith("Host")) {
                            incoming = incoming.substring(incoming.indexOf(":")+2);
                            System.out.println(incoming + "new one");
                            inputHandler.pasteString(incoming);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                } catch (Exception e) {
                    System.out.println("Error with input Handler");
                    throw new RuntimeException(e);
                }
            }).start();

            // Sending messages to server
            String msg;
            while ((msg = userInput.readLine()) != null) {
                out.println("Client[" + Main.NAME + "]: "+msg);
            }

            socket.close();
            System.out.println("closed");

        } catch (IOException e) {
            System.out.println("Could not connect to the server.");
            e.printStackTrace();
        }
    }
}
