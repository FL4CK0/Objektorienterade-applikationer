package org.example;
import java.io.File;
import java.nio.file.Files;

/**
 * Gränssnitt för att definiera grundläggande kontrollerfunktioner i en chattapplikation.
 * Detta gränssnitt specificerar operationer för att ansluta till en server, skicka textmeddelanden,
 * och skicka bilder. Varje kontroller som implementerar detta gränssnitt bör hantera nätverkskommunikation,
 * användarinteraktion och andra kritiska funktioner för att stödja en chattklient.
 */

public interface IController {

    /**
     * Ansluter till en chattserver med den angivna serveradressen och användarnamnet.
     *
     * @param serverAddress IP-adressen eller domännamnet för servern som ska anslutas till.
     * @param username Användarnamnet som ska användas vid anslutningen.
     */
    void connectToServer(String serverAddress, String username);


    /**
     * Skickar ett textmeddelande till servern.
     *
     * @param messageText Texten för det meddelande som ska skickas.
     */
    void sendMessage(String messageText);

    /**
     * Skickar en bildfil som ett MMS-meddelande till servern.
     *
     * @param imageFile En fil som representerar bilden som ska skickas.
     */
    void sendImage(File imageFile);
}