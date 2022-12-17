package pl.edu.agh.kis.pz1;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public abstract class Human extends Thread {
    protected Logger logger = Logger.getLogger(Human.class.getName());

    public final Semaphore humanSemaphore = new Semaphore(0);

    // time in seconds
    protected float minTimeToWait;
    protected float maxTimeToWait;

    protected float minTimeToBeInLibrary;
    protected float maxTimeToBeInLibrary;
    protected Library library;

    protected boolean shouldStop = false;

    protected int id;

    public enum HumanType {
        READER,
        WRITER
    }

    protected HumanType type;

    protected Human(Library libraryArg, float minTimeToWaitArg, float maxTimeToWaitArg, float minTimeToBeInLibraryArg, float maxTimeToBeInLibraryArg, int idArg) {
        library = libraryArg;
        minTimeToWait = minTimeToWaitArg;
        maxTimeToWait = maxTimeToWaitArg;
        minTimeToBeInLibrary = minTimeToBeInLibraryArg;
        maxTimeToBeInLibrary = maxTimeToBeInLibraryArg;
        id = idArg;
    }

    public void stopHuman() {
        shouldStop = true;
        humanSemaphore.release();
    }

    public void writeToLoggerInfo(String message) {
        if (logger != null && logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info(message);
        }
    }

    public void writeToLoggerSevere(String message) {
        if (logger != null && logger.isLoggable(java.util.logging.Level.SEVERE)) {
            logger.severe(message);
        }
    }

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

    @Override
    public abstract String toString();

    @Override
    public abstract void run();
}
