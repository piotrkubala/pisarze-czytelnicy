package pl.edu.agh.kis.pz1;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Reader extends Human {
    public Reader(Library libraryArg, float minTimeToWaitArg, float maxTimeToWaitArg, float minTimeToBeInLibraryArg, float maxTimeToBeInLibraryArg, int idArg) {
        super(libraryArg, minTimeToWaitArg, maxTimeToWaitArg, minTimeToBeInLibraryArg, maxTimeToBeInLibraryArg, idArg);
        type = HumanType.READER;
    }

    @Override
    public void run() {
        while (!shouldStop) {
            long timeToWait = (long) (1000 * (Math.random() * (maxTimeToWait - minTimeToWait) + minTimeToWait));

            try {
                Thread.sleep(timeToWait * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            library.requestRead(this);

            long timeToBeInLibrary = (long) (1000 * (Math.random() * (maxTimeToBeInLibrary - minTimeToBeInLibrary) + minTimeToBeInLibrary));

            try {
                Thread.sleep(timeToBeInLibrary * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            library.finishRead(this);
        }
    }

    @Override
    public String toString() {
        return "Reader " + id;
    }
}
