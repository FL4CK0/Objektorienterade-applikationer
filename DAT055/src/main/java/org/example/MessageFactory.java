package org.example;
import java.io.File;
import java.io.IOException;
import org.example.MMS;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MessageFactory {

    public static Message createMessage(Object content) {
        if (content instanceof String) {
            return new TextMessage((String) content);
        } else if (content instanceof File) {
            // Convert File to byte[] before creating MMS
            File imageFile = (File) content;
            try {
                byte[] imageData = Files.readAllBytes(imageFile.toPath());
                return new MMS(imageData);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read image file: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("Invalid message content");
        }
    }
}