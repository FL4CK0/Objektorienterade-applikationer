package org.example;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;

/**
 * Klass serverfunktionaliteten i chattapplikationen
 * Klassen hanterar klientanslutningar, meddelandeutsändning och lagring av meddelandehistorik.
 * Den lyssnar på en specifik port för inkommande klientanslutningar och hanterar dem via interna ClientHandler-instanser.
 */

public class HostNetwork {
    private static final int PORT = 12345;
    private static final String HOST_IP = "localhost"; // Example IP address
    private static final String HISTORY_FILE = "chat_history.dat";
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static List<Message> messageHistory = new ArrayList<>(); // Store messages for history


    /**
     * Startpunkt för serverapplikationen. Initierar laddning av chatthistorik och startar servern för att lyssna på anslutningar.
     * När en klient ansluter, hanteras den av en ny tråd som instansierar ClientHandler.
     *
     * @param args Används inte i denna applikation.
     */

    public static void main(String[] args) {
        loadHistory(); // Load history at startup
        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(HOST_IP))) {
            System.out.println("Server is running and waiting for connections on " + HOST_IP + ":" + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New user connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.out.println("Server could not start or has been stopped. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadHistory() {
        File file = new File(HISTORY_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                messageHistory = (List<Message>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load chat history: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void saveHistory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HISTORY_FILE))) {
            oos.writeObject(messageHistory);
        } catch (IOException e) {
            System.out.println("Failed to save chat history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sänder ett meddelande till alla anslutna klienter, undantaget avsändaren.
     * Metoden lägger också till meddelandet i den lokala historiken och sparar den omedelbart.
     *
     * @param message Meddelandet som ska sändas.
     * @param sender Klienthanteraren för avsändaren av meddelandet; kan vara null om meddelandet ska sändas till alla inklusive avsändaren.
     */
    public static void broadcast(Message message, ClientHandler sender) {
        messageHistory.add(message); // Add to history when broadcasting
        saveHistory(); // Save after updating history
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Returnerar en kopia av den nuvarande meddelandehistoriken.
     * Används för att ge nya klienter historiken när de ansluter sig till servern.
     *
     * @return En ny lista som innehåller alla sparade meddelanden.
     */
    // Method to get the message history
    public static List<Message> getHistory() {
        return new ArrayList<>(messageHistory);
    }

    // Internal class to handle client connections
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private User user;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(clientSocket.getInputStream());
                out.writeObject("Enter your username:");
                String username = (String) in.readObject(); // Initialize username at connection time
                this.user = new User(username);
                System.out.println("User " + username + " connected.");

                // Send a message to all clients that a new user has connected
                TextMessage connectMessage = new TextMessage("*" + username + " has connected*");
                HostNetwork.broadcast(connectMessage, null); // Broadcast without a sender

                sendAllPreviousMessages(); // Send history to newly connected client
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Could not initialize communication with client: " + socket.getInetAddress().getHostAddress());
                e.printStackTrace();
            }
        }

        /**
         * Körmetod för klienthanteraren som används när en klientanslutning initieras.
         * lyssnar kontinuerligt på inkommande meddelanden från den anslutna klienten
         * och bearbetar dessa meddelanden genom att antingen logga dem eller sända dem vidare till andra klienter.
         * Om meddelandet är av typen String sänds det som en TextMessage, och om det är en MMS hanteras det separat.
         * Vid undantag eller avslut av anslutningen hanteras resursstädning och klienten tas bort från den aktiva klientlistan.
         */
        @Override
        public void run() {
            try {
                Object clientMessage;
                while ((clientMessage = in.readObject()) != null) {
                    if (clientMessage instanceof String) {
                        String messageContent = (String) clientMessage;
                        System.out.println(messageContent); // Log for debugging
                        HostNetwork.broadcast(new TextMessage(user.getUsername() + ": " + messageContent), this); // Broadcast to other clients
                    } else if (clientMessage instanceof MMS) {
                        HostNetwork.broadcast(new TextMessage(user.getUsername() + ": "), this); //broadcasting who sent image
                        HostNetwork.broadcast((MMS) clientMessage, this); // Broadcast image message
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling client# " + user.getUsername() + ": " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        private void sendMessage(Message message) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.out.println("Error sending message to client " + user.getUsername() + ": " + e.getMessage());
            }
        }

        private void sendAllPreviousMessages() {
            for (Message message : HostNetwork.getHistory()) {
                sendMessage(message);
            }
        }

        private void cleanup() {
            clients.remove(this);
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error while closing resources for user " + user.getUsername());
            }
        }
    }
}
