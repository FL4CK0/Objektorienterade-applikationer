package org.example;
import java.io.File;

public class MessageFactory {

    public static Message createMessage(Object content) {
        if (content instanceof String) {
            return new TextMessage((String) content);
        } else if (content instanceof File) {
            return new MMS((File) content);
        } else {
            throw new IllegalArgumentException("Invalid message content");
        }
    }
}