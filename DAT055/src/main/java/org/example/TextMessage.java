package org.example;

import java.io.Serializable;

/**
 * Representerar ett textmeddelande inom chattapplikationen
 * Denna klass implementerar Message gränssnittet
 */
public class TextMessage implements Message, Serializable {
    private static final long serialVersionUID = 1L; // Ensure version compatibility
    private String text;

    /**
     * Skapar ett TextMessage-objekt med den givna textsträngen.
     *
     * @param text Texten för meddelandet.
     */
    public TextMessage(String text) {
        this.text = text;
    }


    @Override
    public String getContent() {
        return text;
    }

    @Override
    public String toString() {
        return text; // Return text directly for display
    }

    /**
     * Jämför detta TextMessage-objekt med ett annat objekt för att avgöra om de är lika.
     * Två TextMessage-objekt anses vara lika om deras textsträngar är lika.
     *
     * @param obj Objektet som detta objekt ska jämföras med.
     * @return Sant om båda objekten har samma text; annars falskt.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TextMessage that = (TextMessage) obj;
        return text != null ? text.equals(that.text) : that.text == null;
    }

    /**
     * Beräknar en hashkod för detta TextMessage-objekt baserat på dess text.
     * Används för att effektivt kunna placera och söka efter objektet i en hash-baserad datastruktur.
     *
     * @return En hashkod som representerar texten.
     */
    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

}