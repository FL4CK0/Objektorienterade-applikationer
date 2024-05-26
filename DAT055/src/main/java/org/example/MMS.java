package org.example;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Representerar ett bild-meddelande som innehåller bilddata inom chattapplikationen.
 * Denna klass implementerar Message gränssnittet och stöder serialisering för att underlätta nätverksöverföring.
 * Den hanterar bilddata som en byte-array och tillhandahåller metoder för att jämföra och hämta bildinformation.
 */
public class MMS implements Message, Serializable {
    private static final long serialVersionUID = 1L;
    private byte[] imageData;

    /**
     * Skapar ett MMS-objekt med givet bilddata.
     *
     * @param imageData Bilddata som en byte-array.
     */
    public MMS(byte[] imageData) {
        this.imageData = imageData;
    }


    @Override
    public String getContent() {
        return "Image data received"; // Or any other appropriate message
    }

    /**
     * Tillhandahåller en direkt åtkomst till bildens byte-data.
     *
     * @return Byte-arrayen som representerar bildens data.
     */
    public byte[] getImageData() {
        return imageData;
    }


    /**
     * Jämför detta MMS-objekt med ett annat objekt för att avgöra om de är lika.
     * Två MMS-objekt anses vara lika om deras bilddata är identiska.
     *
     * @param obj Det objekt som detta objekt ska jämföras med.
     * @return True om de båda objekten har identisk bilddata; annars false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MMS mms = (MMS) obj;
        return Arrays.equals(imageData, mms.imageData);
    }

    /**
     * Beräknar en hashkod för detta MMS-objekt baserat på dess bilddata.
     * Används för att effektivt kunna placera och söka efter objektet i en hash-baserad datastruktur.
     *
     * @return En hashkod som representerar bilddata.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(imageData);
    }

}