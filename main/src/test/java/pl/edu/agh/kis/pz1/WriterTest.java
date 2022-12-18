package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Klasa testująca działanie klasy Writer.
 */
public class WriterTest {
    /**
     * Test sprawdzający, czy pisarz jest poprawnie tworzony.
     */
    @Test
    public void testWriterCreation() {
        // given
        Library library = new Library(10, true);
        Writer writer;

        // when
        writer = new Writer(library, 0.0f, 0.01f, 0.1f, 0.2f, 0);
        library.addHuman(writer);

        // then
        assertNotNull(writer);
    }
}
