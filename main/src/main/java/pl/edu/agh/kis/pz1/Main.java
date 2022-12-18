package pl.edu.agh.kis.pz1;

import sun.misc.Signal;

import java.util.logging.Logger;

/**
 * Główna klasa programu symulującego rozwiązanie problemu pisarzy i czytelników.
 * Schemat klas @author Paweł Skrzyński
 * Algorytm @author Piotr Kubala
 */
public class Main {
    /**
     * Logger głównego programu.
     */
    static Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Główna metoda programu.
     * @param args Argumenty wywołania programu.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            logger.info("Wrong number of arguments");
            return;
        }

        try {
            int readersCount = Integer.parseInt(args[0]);
            int writersCount = Integer.parseInt(args[1]);

            if (readersCount < 0 || writersCount < 0) {
                logger.info("Wrong arguments");
                return;
            }

            simulateLibrary(readersCount, writersCount, new float[] {1.0f, 2.0f, 3.0f, 5.0f}, new float[] {3.0f, 6.0f, 5.0f, 9.0f}, false);
        } catch (NumberFormatException e) {
            logger.info("Wrong arguments");
        }
    }

    /**
     * Symuluje działanie biblioteki.
     * @param readersCount ilość czytelników
     * @param writersCount ilość pisarzy
     * @param timeLimitsForReader {minTimeToWaitR, maxTimeToWaitR, minTimeToBeInLibraryR, maxTimeToBeInLibraryR}
     * @param timeLimitsForWriter {minTimeToWaitW, maxTimeToWaitW, minTimeToBeInLibraryW, maxTimeToBeInLibraryW}
     * @param debugMode czy włączyć tryb debugowania
     * @return string zawierający informacje o tym, co się dzieje w bibliotece (jeśli tryb debugowania jest włączony, w przeciwnym wypadku zwraca pusty string)
     */
    public static String simulateLibrary(int readersCount, int writersCount,
                                         float[] timeLimitsForReader, float[] timeLimitsForWriter,
                                         boolean debugMode) {
        Library library = new Library(5, debugMode);

        Signal.handle(new Signal("INT"), signal -> library.stopLibrary());

        for (int i = 0; i < readersCount; i++) {
            Reader reader = new Reader(library, timeLimitsForReader[0], timeLimitsForReader[1], timeLimitsForReader[2], timeLimitsForReader[3], i);
            library.addHuman(reader);
            reader.start();
        }

        for (int i = 0; i < writersCount; i++) {
            Writer writer = new Writer(library, timeLimitsForWriter[0], timeLimitsForWriter[1], timeLimitsForWriter[2], timeLimitsForWriter[3], i);
            library.addHuman(writer);
            writer.start();
        }

        library.processWaitingQueue();

        return library.getDebugString();
    }
}
