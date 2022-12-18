package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Klasa testująca działanie klasy Reader.
 */
public class ReaderTest {
    /**
     * Test sprawdzający, czy czytelnik jest poprawnie tworzony.
     */
    @Test
    public void testReaderCreation() {
        // given
        Library library = new Library(10, true);
        Reader reader;

        // when
        reader = new Reader(library, 0.0f, 0.01f, 0.1f, 0.2f, 0);
        library.addHuman(reader);

        // then
        assertNotNull(reader);
    }
}
