package org.example;

import java.util.ArrayList;
import java.util.List;

public class ChatApplication {
    private List<User> users;
    private ChatHistory chatHistory;

    public ChatApplication() {
        this.users = new ArrayList<>();
        this.chatHistory = new ChatHistory();
    }

    /*
    public void startSession() {
        users = new ArrayList<>();
        chatHistory = new ChatHistory();
    }
    */
    
    public void endSession() {
        updateChatHistory();
        users.clear();
    }

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public void removeUser(User user) {
        if (users.contains(user)) {
            users.remove(user);
        }
    }

    public void broadcastMessage(Message message) {
        chatHistory.addMessage(message);
        message.send();
    }

    public void broadcastMMS(MMS mms) {
        chatHistory.addMessage(mms);
        mms.send();
    }

}
