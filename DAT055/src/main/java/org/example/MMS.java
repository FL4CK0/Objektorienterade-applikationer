package org.example;
import java.io.Serializable;


public class MMS implements Message, Serializable {
    private static final long serialVersionUID = 1L;
    private byte[] imageData;

    // Constructor accepting byte array directly
    public MMS(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public void send() {
        // Implement the logic to send this image data over the network if necessary
    }

    @Override
    public String getContent() {
        return "Image data received"; // Or any other appropriate message
    }

    public byte[] getImageData() {
        return imageData;
    }
}