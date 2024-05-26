package org.example;
import java.io.*;
import java.net.Socket;


/**
 * Hanterar nätverkskommunikationen mellan klienten och servern.
 * Denna klass ansvarar för att upprätta och underhålla en nätverksanslutning,
 * skicka och ta emot meddelanden samt hantera strömmar för dataöverföring.
 */
public class UserNetwork {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ChatHistory chatHistory;
    private User user;

    /**
     * Konstruerar en ny UserNetwork-instans.
     * Initierar en nätverksanslutning med specifik serveradress och port, och länkar till en chatt-historik och användare.
     *
     * @param serverAddress IP-adressen till servern som klienten ska ansluta till.
     * @param serverPort Porten på servern som klienten ska ansluta till.
     * @param chatHistory En instans av ChatHistory för att lagra och hantera chattmeddelanden.
     * @param user En instans av User som representerar användaren i nätverkskommunikationen.
     */
    public UserNetwork(String serverAddress, int serverPort, ChatHistory chatHistory, User user) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.chatHistory = chatHistory;
        this.user = user;
    }

    /**
     * Ansluter till servern och initierar lyssnare för meddelanden från servern.
     * Skapar in- och ut-strömmar för att hantera överföring av data.
     */
    public void connectToServer() {
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // Flush the stream to ensure the header is written
            in = new ObjectInputStream(socket.getInputStream());

            // Send initial data such as the username after the streams are established
            out.writeObject(user.getUsername());
            out.flush();

            // Now listen for messages from the server
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            Object message;
            while ((message = in.readObject()) != null) {
                if (message instanceof Message) {
                    chatHistory.addMessage((Message) message);
                } else if (message instanceof MMS) {
                    chatHistory.addMessage((MMS) message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection lost");
            e.printStackTrace();
        }
    }
    /**
     * Skickar ett meddelande till servern.
     *
     * @param message Meddelandet som ska skickas. Kan vara av typen TextMessage eller MMS.
     */
    public void sendMessage(Message message) {
        try {
            if (message instanceof MMS) {
                out.writeObject(message); // Send the entire MMS object, not just the content
            } else {
                out.writeObject(message.getContent());
            }
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
            e.printStackTrace();
        }
    }

}