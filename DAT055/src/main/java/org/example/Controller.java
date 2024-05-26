package org.example;
import java.io.File;
import java.nio.file.Files;

/**
 * Kontrollerklass för att hantera interaktion mellan användargränssnittet, nätverkskommunikation och chatthistorik.
 * Denna klass initierar och hanterar huvudkomponenterna i chattapplikationen, inklusive användargränssnittet,
 * anslutning till servern, och skickande av meddelanden och bilder.
 */

public class Controller implements IController {
    private UserInterface view;
    private ChatHistory chatHistory;
    private UserNetwork client;
    private final int port = 12345;  // Hardcoded port

    /**
     * Skapar en ny Controller-instans, initierar chatthistoriken och användargränssnittet,
     * och gör användargränssnittet synligt för användaren.
     */
    public Controller() {
        chatHistory = new ChatHistory();
        view = new UserInterface(this, chatHistory);
        view.setVisible(true);
    }


    @Override
    public void connectToServer(String serverAddress, String username) {
        client = new UserNetwork(serverAddress, port, chatHistory, new User(username));
        new Thread(client::connectToServer).start();
    }
    @Override
    public void sendMessage(String messageText) {
        Message message = MessageFactory.createMessage(messageText);
        client.sendMessage(message);
        Message messageForChatHistory = MessageFactory.createMessage("You: " + messageText);
        chatHistory.addMessage(messageForChatHistory);
    }
    @Override
    public void sendImage(File imageFile) {
        try {
            byte[] imageData = Files.readAllBytes(imageFile.toPath());
            MMS imageMessage = new MMS(imageData);
            client.sendMessage(imageMessage);
        } catch (Exception e) {
            System.err.println("Failed to send image: " + e.getMessage());
        }
    }

    /**
     * Huvudmetod som startar applikationen genom att skapa en instans av Controller
     *
     * @param args kommandoradsargument som inte används av denna applikation.
     */
    public static void main(String[] args) {
        new Controller();
    }
}