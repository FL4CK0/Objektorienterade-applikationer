
package org.example;

/**
 * Gränssnitt för meddelanden i en chattapplikation.
 * Detta gränssnitt definierar en grundläggande struktur för meddelanden genom att kräva en metod för att hämta
 * meddelandets innehåll. Implementeringar av detta gränssnitt kan representera olika typer av meddelanden,
 * såsom textmeddelanden eller multimedia-meddelanden (MMS).
 */
public interface Message {

    /**
     * Hämtar innehållet i meddelandet som en sträng.
     *
     * @return En sträng som representerar meddelandets innehåll.
     */
    String getContent();
}
