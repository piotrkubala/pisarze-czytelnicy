package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MainTest {
    @Test
    public void testMain() {
        // given
        Main main;

        // when
        main = new Main();

        // then
        assertNotNull(main);
    }
}


