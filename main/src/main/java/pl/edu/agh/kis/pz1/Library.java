package pl.edu.agh.kis.pz1;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Klasa reprezentująca bibliotekę.
 */
public class Library {
    /**
     * Logger biblioteki.
     */
    final Logger logger = Logger.getLogger(Library.class.getName());

    /**
     * Formatter loggera.
     */
    static class CustomRecordFormatter extends Formatter {
        @Override
        public String format(final LogRecord r) {
            StringBuilder sb = new StringBuilder();
            sb.append(formatMessage(r)).append(System.getProperty("line.separator"));
            if (null != r.getThrown()) {
                sb.append("Throwable occurred: ");
                Throwable t = r.getThrown();
                try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
                    t.printStackTrace(pw);
                    sb.append(sw);
                } catch (Exception ex) {
                    // ignore all exceptions here
                }
            }
            return sb.toString();
        }
    }

    /**
     * Zmienna przełączająca tryb debugowania.
     */
    boolean debugMode = false;
    /**
     * Zmienna przechwująca łańcuch znaków w trybie debugowania, który byłby wypisany na konsolę.
     */
    StringBuilder debugStringBuilder = new StringBuilder();
    /**
     * Maksymalna ilość iteracji w trybie debugowania.
     */
    int debugMaxIterations;
    /**
     * Semafor synchronizujący dodawanie tekstu do łańcucha znaków w trybie debugowania.
     */
    final Semaphore debugSemaphore = new Semaphore(1);

    /**
     * Zmienna określająca czy biblioteka powinna zakończyć działanie.
     */
    boolean shouldEnd = false;

    /**
     * Maksymalna ilość miejsc w bibliotece dla czytelników.
     */
    int maxReaders;

    /**
     * Obecna ilość pisarzy w bibliotece.
     */
    int writersCount = 0;
    /**
     * Obecna ilość czytelników w bibliotece.
     */
    int readersCount = 0;

    /**
     * Semafor dla miejsc w bibliotece.
     */
    Semaphore readerAndWriterSemaphore;
    /**
     * Semafor dla bibliotekarza.
     */
    Semaphore librarySemaphore = new Semaphore(0);

    /**
     * Kolejka zapisująca kolejność wejścia czytelników i pisarzy do biblioteki.
     */
    BlockingQueue<Human> waitingQueue = new LinkedBlockingQueue<>();

    /**
     * Lista zapisanych czytelników i pisarzy w bibliotece.
     */
    ArrayList<Human> humans = new ArrayList<>();

    /**
     * Konstruktor biblioteki.
     * @param maxReadersArg Maksymalna ilość miejsc w bibliotece dla czytelników.
     * @param debugModeArg Zmienna przełączająca tryb debugowania.
     */
    public Library(int maxReadersArg, boolean debugModeArg) {
        maxReaders = maxReadersArg;

        debugMode = debugModeArg;
        debugMaxIterations = 1;

        createSemaphoreAndConfigureLogger();
    }

    /**
     * Metoda tworząca semafor dla miejsc w bibliotece i konfigurująca logger.
     */
    void createSemaphoreAndConfigureLogger() {
        readerAndWriterSemaphore = new Semaphore(maxReaders);

        logger.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomRecordFormatter());
        logger.addHandler(consoleHandler);
    }

    /**
     * Metoda dodająca czytelnika lub pisarza do biblioteki.
     * @param human Czytelnik lub pisarz do dodania.
     */
    public void addHuman(Human human) {
        humans.add(human);

        debugMaxIterations++;
    }

    /**
     * Metoda ustawiająca tryb debugowania.
     * @param debugModeArg zmienna przełączająca tryb debugowania.
     */
    public void setDebugMode(boolean debugModeArg) {
        debugMode = debugModeArg;
    }

    /**
     * Metoda zwracająca, czy tryb debugowania jest włączony.
     * @return czy tryb debugowania jest włączony.
     */
    public boolean getDebugMode() {
        return debugMode;
    }

    public void setDebugMaxIterations(int debugMaxIterationsArg) {
        debugMaxIterations = debugMaxIterationsArg;
    }

    public int getDebugMaxIterations() {
        return debugMaxIterations;
    }

    public String getDebugString() {
        return debugStringBuilder.toString();
    }

    public void resetDebugString() {
        debugStringBuilder = new StringBuilder();
    }

    void printQueueInfo() {
        StringBuilder sb = new StringBuilder();

        sb.append("------ Queue info ------");
        sb.append("\nWaiting queue size: ");
        sb.append(waitingQueue.size());
        sb.append("\nReaders count: ");
        sb.append(readersCount);
        sb.append("\nWriters count: ");
        sb.append(writersCount);
        sb.append("\n------ Queue info end ------");

        writeToLoggerInfo(sb.toString());
    }

    private void processHumanInQueue(Human human) {
        if (human.type == Human.HumanType.READER) {
            readerAndWriterSemaphore.acquireUninterruptibly();
            StringBuilder sb = new StringBuilder();
            sb.append(human);
            sb.append(" is reading");

            writeToLoggerInfo(sb.toString());
            readersCount++;
        } else {
            readerAndWriterSemaphore.acquireUninterruptibly(maxReaders);
            StringBuilder sb = new StringBuilder();
            sb.append(human);
            sb.append(" is writing");

            writeToLoggerInfo(sb.toString());
            writersCount++;
        }
        human.humanSemaphore.release();
    }

    public void processWaitingQueue() {
        int debugIterations = 0;

        while (!shouldEnd && (!debugMode || debugIterations < debugMaxIterations)) {
            librarySemaphore.acquireUninterruptibly();

            while (!waitingQueue.isEmpty() && (!debugMode || debugIterations < debugMaxIterations)) {
                if (debugMode) {
                    debugIterations++;
                }

                printQueueInfo();

                Human human = waitingQueue.poll();

                if (human == null) {
                    continue;
                }

                processHumanInQueue(human);
            }
        }

        if (debugMode) {
            stopLibrary();
        }
    }

    public void stopLibrary() {
        writeToLoggerInfo("Stopping library");

        for (Human human : humans) {
            human.stopHuman();
        }

        shouldEnd = true;
        waitingQueue.clear();
        librarySemaphore.release(humans.size());

        writeToLoggerInfo("Library stopped");
    }

    public void requestRead(Reader reader) {
        StringBuilder sb = new StringBuilder();
        sb.append(reader);
        sb.append(" wants to read");

        writeToLoggerInfo(sb.toString());
        waitingQueue.add(reader);

        librarySemaphore.release();
        reader.humanSemaphore.acquireUninterruptibly();
    }

    public void requestWrite(Writer writer) {
        StringBuilder sb = new StringBuilder();
        sb.append(writer);
        sb.append(" wants to write");

        writeToLoggerInfo(sb.toString());
        waitingQueue.add(writer);

        librarySemaphore.release();
        writer.humanSemaphore.acquireUninterruptibly();
    }

    public void finishRead(Reader reader) {
        StringBuilder sb = new StringBuilder();
        sb.append(reader);
        sb.append(" finished reading");

        writeToLoggerInfo(sb.toString());

        readerAndWriterSemaphore.release();
        readersCount--;

        librarySemaphore.release();
    }

    public void finishWrite(Writer writer) {
        StringBuilder sb = new StringBuilder();
        sb.append(writer);
        sb.append(" finished writing");

        writeToLoggerInfo(sb.toString());

        readerAndWriterSemaphore.release(maxReaders);
        writersCount--;

        librarySemaphore.release();
    }

    public void writeToLoggerInfo(String message) {
        if (debugMode) {
            debugSemaphore.acquireUninterruptibly();
            debugStringBuilder.append(message);
            debugStringBuilder.append("\n");
            debugSemaphore.release();
        } else if (logger != null && logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(message);
        }
    }

    public void writeToLoggerSevere(String message) {
        if (debugMode) {
            debugSemaphore.acquireUninterruptibly();
            debugStringBuilder.append(message);
            debugStringBuilder.append("\n");
            debugSemaphore.release();
        } else if (logger != null && logger.isLoggable(java.util.logging.Level.SEVERE)) {
            logger.severe(message);
        }
    }
}
