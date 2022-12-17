package pl.edu.agh.kis.pz1;

public class Reader extends Human {
    public Reader(Library libraryArg, float minTimeToWaitArg, float maxTimeToWaitArg, float minTimeToBeInLibraryArg, float maxTimeToBeInLibraryArg, int idArg) {
        super(libraryArg, minTimeToWaitArg, maxTimeToWaitArg, minTimeToBeInLibraryArg, maxTimeToBeInLibraryArg, idArg);
        type = HumanType.READER;
    }

    @Override
    public void run() {
        while (!shouldStop) {
            long timeToWait = (long) (1000 * (Math.random() * (maxTimeToWait - minTimeToWait) + minTimeToWait));

            sleepForTime(timeToWait);

            if (!shouldStop) {
                library.requestRead(this);

                if (shouldStop) {
                    break;
                }

                long timeToBeInLibrary = (long) (1000 * (Math.random() * (maxTimeToBeInLibrary - minTimeToBeInLibrary) + minTimeToBeInLibrary));

                sleepForTime(timeToBeInLibrary);

                library.finishRead(this);
            }
        }
    }

    @Override
    public String toString() {
        return "Reader " + id;
    }
}
