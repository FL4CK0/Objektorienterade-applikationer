package org.example;
import java.io.File;

public class MMS implements Message {
    private File imageFile;

    public MMS(File imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public void send() {
        // Logic to send image message
    }

    @Override
    public String getContent() {
        // You might want to return a placeholder text or the image's file path
        return "Image: " + imageFile.getName();
    }
}