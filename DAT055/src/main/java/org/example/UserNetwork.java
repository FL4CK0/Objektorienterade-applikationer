package org.example;
import java.io.*;
import java.net.*;
import java.io.*;
import java.net.Socket;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.io.*;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.Base64;

public class UserNetwork {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ChatHistory chatHistory;
    private User user;

    public UserNetwork(String serverAddress, int serverPort, ChatHistory chatHistory, User user) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.chatHistory = chatHistory;
        this.user = user;
    }

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

    public void sendMessage(Message message) {
        try {
            if (message instanceof MMS) {
                out.writeObject(message); // Send the entire MMS object, not just the content
            } else {
                out.writeObject(user.getUsername() + ": " + message.getContent());
            }
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return user.getUsername();
    }
}