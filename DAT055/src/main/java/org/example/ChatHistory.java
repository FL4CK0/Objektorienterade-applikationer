package org.example;

import java.util.Observable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Hanterar lagring och avisering av chatthistorik i en chattapplikation.
 * Denna klass fungerar som en central lagringsplats för alla meddelanden och notifierar observerare
 * när nya meddelanden läggs till.
 */
public class ChatHistory extends Observable {
    private List<Message> messages = new CopyOnWriteArrayList<>();


    /**
     * Lägger till ett meddelande i chatthistoriken och notifierar alla registrerade observerare om det nya meddelandet.
     * Denna metod markerar först att det har skett en ändring med setChanged(), och kallar sedan på notifyObservers()
     * för att sända meddelandet till alla observerare.
     *
     * @param message Meddelandet som ska läggas till i historiken.
     */
    public void addMessage(Message message) {
        messages.add(message);
        setChanged();
        notifyObservers(message);
    }


}