package org.example;
import java.io.File;

public class User {
    private String username;
    private ChatApplication chatApp; 

    public User(String username, ChatApplication chatApp) {
        this.username = username;
        this.chatApp = chatApp;
    }

    public void connectToChat() {
        chatApp.addUser(this);
    }

    public void disconnectFromChat() {
        chatApp.removeUser(this);
    }

    public void sendTextMessage(String text) {
        Message message = new TextMessage(text, this);  //Ändra i linje med textmessage/message
        chatApp.broadcastMessage(message);
    }

    public void sendMMS(File imageFile) {
        MMS mms = new MMS(imageFile, this);  //Ändra i linje med mms
        chatApp.broadcastMMS(mms);
    }

    public String getUsername() {
        return username;
    }

}
