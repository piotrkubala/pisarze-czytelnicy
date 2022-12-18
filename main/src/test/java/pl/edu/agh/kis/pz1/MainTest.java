package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Klasa testująca działanie klasy Main.
 */
public class MainTest {
    /**
     * Test sprawdzający, czy klasa Main jest poprawnie tworzona.
     */
    @Test
    public void testMain() {
        // given
        Main main;

        // when
        main = new Main();

        // then
        assertNotNull(main);
    }

    /**
     * Test sprawdzający, czy symulacja biblioteki działa poprawnie.
     */
    @Test
    public void testLibrarySimulation() {
        // given
        Main main;

        // when
        main = new Main();
        String debugString = main.simulateLibrary(10, 3, new float[] {0.0f, 0.01f, 0.1f, 0.2f}, new float[] {0.0f, 0.02f, 0.05f, 0.06f}, true);

        // then
        assertTrue(debugString.contains("reading"));
    }
}


