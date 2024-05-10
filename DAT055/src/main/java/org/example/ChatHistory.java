package org.example;

import java.util.Observable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class ChatHistory extends Observable {
    private List<Message> messages = new CopyOnWriteArrayList<>();

    public void addMessage(Message message) {
        messages.add(message);
        setChanged();
        notifyObservers(message);
    }

    public List<Message> getMessages() {
        return messages; // Return a snapshot of messages
    }
}