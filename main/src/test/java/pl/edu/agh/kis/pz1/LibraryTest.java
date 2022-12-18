package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Klasa testująca działanie klasy Library
 */
public class LibraryTest {
    /**
     * Test sprawdzający, czy biblioteka jest poprawnie tworzona.
     */
    @Test
    public void testLibraryCreation() {
        // given
        Library library;

        // when
        library = new Library(10, true);

        // then
        assertNotNull(library);
    }
}
