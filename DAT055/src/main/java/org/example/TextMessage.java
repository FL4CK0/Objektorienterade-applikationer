package org.example;

public class TextMessage implements Message {
    private String text;

    public TextMessage(String text) {
        this.text = text;
    }

    @Override
    public void send() {
        // Logic to send text message
    }

    @Override
    public String getContent() {
        return text;
    }
}