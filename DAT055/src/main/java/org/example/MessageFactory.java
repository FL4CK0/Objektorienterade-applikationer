package org.example;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Fabriksklass för att skapa olika typer av meddelanden baserat på innehåll.
 * Denna klass tillhandahåller en statisk metod för att generera meddelanden från generiska objekt,
 * vilket underlättar skapandet av meddelandeobjekt utan att direkt instansiera dem i klientkoden.
 */
public class MessageFactory {

    /**
     * Skapar ett meddelandeobjekt baserat på det givna innehållet.
     * om innehållet är en sträng, skapas ett TextMessage.
     * om innehållet är en File, försöker metoden läsa filen som en bild och skapa ett MMS med bildens data.
     * ananrs kastas ett undantag.
     *
     * @param content Objektet som innehåller meddelandeinnehållet, antingen en sträng eller en fil.
     * @return Ett implementerat Message-objekt, beroende på innehållets typ.
     * @throws RuntimeException Om det uppstår ett I/O-fel vid läsning av en fil.
     * @throws IllegalArgumentException Om innehållet inte är av en stödd typ eller inte kan hanteras.
     */
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