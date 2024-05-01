package org.example;
import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
    private List<Message> messages;

    public ChatHistory() {
        messages = new ArrayList<>();
    }

    // Adds a message to the history
    public void addMessage(Message message) {
        if (message != null) {
            messages.add(message);
        }
    }

    public void clearHistory() {
        messages.clear();
    }

    public void loadHistory() {
        //Tillsvidare
    }

    public void saveHistory() {
       //Tillsvidare
    }
}
