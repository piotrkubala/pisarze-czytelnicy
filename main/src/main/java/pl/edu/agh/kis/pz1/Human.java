package pl.edu.agh.kis.pz1;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

/**
 * Klasa abstarkcyjna reprezentująca człowieka (pisarza lub czytelnika).
 */
public abstract class Human extends Thread {
    /**
     * Logger czytelnika lub pisarza.
     */
    protected Logger logger = Logger.getLogger(Human.class.getName());

    /**
     * Semafor umożliwiający blokowanie czytelnika lub pisarza.
     */
    public final Semaphore humanSemaphore = new Semaphore(0);

    /**
     * Wyrażony w sekundach minimalny czas jaki czytelnik lub pisarz spędzi przed powrotem do biblioteki.
     */
    protected float minTimeToWait;
    /**
     * Wyrażony w sekundach maksymalny czas jaki czytelnik lub pisarz spędzi przed powrotem do biblioteki.
     */
    protected float maxTimeToWait;

    /**
     * Wyrażony w sekundach minimalny czas jaki czytelnik lub pisarz spędzi w bibliotece.
     */
    protected float minTimeToBeInLibrary;
    /**
     * Wyrażony w sekundach maksymalny czas jaki czytelnik lub pisarz spędzi w bibliotece.
     */
    protected float maxTimeToBeInLibrary;
    /**
     * Biblioteka, do której należy czytelnik lub pisarz.
     */
    protected Library library;

    /**
     * Określa czy czytelnik lub pisarz powinien zakończyć działanie.
     */
    protected boolean shouldStop = false;

    /**
     * Id czytelnika lub pisarza.
     */
    protected int id;

    /**
     * Definicja typu czytelnika lub pisarza.
     */
    public enum HumanType {
        READER,
        WRITER
    }

    /**
     * Typ czytelnika lub pisarza.
     */
    protected HumanType type;

    /**
     * Konstruktor czytelnika lub pisarza.
     * @param libraryArg biblioteka, do której należy czytelnik lub pisarz
     * @param minTimeToWaitArg minimalny czas jaki czytelnik lub pisarz spędzi przed powrotem do biblioteki
     * @param maxTimeToWaitArg maksymalny czas jaki czytelnik lub pisarz spędzi przed powrotem do biblioteki
     * @param minTimeToBeInLibraryArg minimalny czas jaki czytelnik lub pisarz spędzi w bibliotece
     * @param maxTimeToBeInLibraryArg maksymalny czas jaki czytelnik lub pisarz spędzi w bibliotece
     * @param idArg id czytelnika lub pisarza
     */
    protected Human(Library libraryArg, float minTimeToWaitArg, float maxTimeToWaitArg, float minTimeToBeInLibraryArg, float maxTimeToBeInLibraryArg, int idArg) {
        library = libraryArg;
        minTimeToWait = minTimeToWaitArg;
        maxTimeToWait = maxTimeToWaitArg;
        minTimeToBeInLibrary = minTimeToBeInLibraryArg;
        maxTimeToBeInLibrary = maxTimeToBeInLibraryArg;
        id = idArg;
    }

    /**
     * Metoda informująca czytelnika lub pisarza, że powinien wyjść z biblioteki i zakończyć działanie.
     */
    public void stopHuman() {
        shouldStop = true;
        humanSemaphore.release();
    }

    /**
     * Metoda wypisujące wiadomość do loggera jako info.
     * @param message wiadomość do wypisania
     */
    public void writeToLoggerInfo(String message) {
        if (logger != null && logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(message);
        }
    }

    /**
     * Metoda wypisujące wiadomość do loggera jako severe.
     * @param message wiadomość do wypisania
     */
    public void writeToLoggerSevere(String message) {
        if (logger != null && logger.isLoggable(java.util.logging.Level.SEVERE)) {
            logger.severe(message);
        }
    }

    /**
     * Metoda czekająca określoną ilość czasu wyrażony w milisekundach.
     * @param time ilość czasu w milisekundach
     */
    public void sleepForTime(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ie) {
            StringBuilder sb = new StringBuilder();
            sb.append("InterruptedException: ");
            sb.append(ie);

            writeToLoggerSevere(sb.toString());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Metoda abstrakcyjna wypisująca informacje o czytelniku lub pisarzu.
     * @return informacje o czytelniku lub pisarzu
     */
    @Override
    public abstract String toString();

    /**
     * Metoda obstrakcyjna reprezentująca działanie czytelnika lub pisarza.
     */
    @Override
    public abstract void run();
}
