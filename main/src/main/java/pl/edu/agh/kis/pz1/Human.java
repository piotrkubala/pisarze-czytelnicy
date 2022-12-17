package pl.edu.agh.kis.pz1;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public abstract class Human extends Thread {
    public Semaphore humanSemaphore = new Semaphore(1);

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

    public Human(Library libraryArg, float minTimeToWaitArg, float maxTimeToWaitArg, float minTimeToBeInLibraryArg, float maxTimeToBeInLibraryArg, int idArg) {
        library = libraryArg;
        minTimeToWait = minTimeToWaitArg;
        maxTimeToWait = maxTimeToWaitArg;
        minTimeToBeInLibrary = minTimeToBeInLibraryArg;
        maxTimeToBeInLibrary = maxTimeToBeInLibraryArg;
        id = idArg;
    }

    public void stopHuman() {
        shouldStop = true;
    }

    public abstract String toString();
}
