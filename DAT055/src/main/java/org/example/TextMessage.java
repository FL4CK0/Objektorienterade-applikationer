package org.example;

import java.io.Serializable;
import java.io.Serializable;

public class TextMessage implements Message, Serializable {
    private static final long serialVersionUID = 1L; // Ensure version compatibility
    private String text;

    public TextMessage(String text) {
        this.text = text;
    }

    @Override
    public void send() {
        System.out.println("Sending text message: " + text);
    }

    @Override
    public String getContent() {
        return text;
    }

    @Override
    public String toString() {
        return text; // Return text directly for display
    }
}