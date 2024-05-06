package org.example;
import java.io.File;
import org.example.MMS;
import java.io.File;
import java.nio.file.Files;

public class Controller {
    private UserInterface view;
    private ChatHistory chatHistory;
    private UserNetwork client;
    private final int port = 12345;  // Hardcoded port

    public Controller() {
        chatHistory = new ChatHistory();
        view = new UserInterface(this, chatHistory);
        view.setVisible(true);
    }

    public void connectToServer(String serverAddress, String username) {
        client = new UserNetwork(serverAddress, port, chatHistory, new User(username));
        new Thread(client::connectToServer).start();
    }

    public void sendMessage(String messageText) {
        Message message = MessageFactory.createMessage(messageText);
        client.sendMessage(message);
        Message messageForChatHistory = MessageFactory.createMessage("You: " + messageText);
        chatHistory.addMessage(messageForChatHistory);
    }

    public void sendImage(File imageFile) {
        try {
            byte[] imageData = Files.readAllBytes(imageFile.toPath());
            MMS imageMessage = new MMS(imageData);
            client.sendMessage(imageMessage);
            // Do not add here if the server is echoing messages back
            // chatHistory.addMessage(imageMessage);
        } catch (Exception e) {
            System.err.println("Failed to send image: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Controller();
    }
}