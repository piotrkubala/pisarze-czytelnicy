package pl.edu.agh.kis.pz1;

public class Writer extends Human {
    public Writer(Library libraryArg, float minTimeToWaitArg, float maxTimeToWaitArg, float minTimeToBeInLibraryArg, float maxTimeToBeInLibraryArg, int idArg) {
        super(libraryArg, minTimeToWaitArg, maxTimeToWaitArg, minTimeToBeInLibraryArg, maxTimeToBeInLibraryArg, idArg);
        type = HumanType.WRITER;
    }

    @Override
    public void run() {
        while (!shouldStop) {
            long timeToWait = (long) (1000 * (Math.random() * (maxTimeToWait - minTimeToWait) + minTimeToWait));

            try {
                Thread.sleep(timeToWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldStop) {
                break;
            }

            library.requestWrite(this);

            if (shouldStop) {
                break;
            }

            long timeToBeInLibrary = (long) (1000 * (Math.random() * (maxTimeToBeInLibrary - minTimeToBeInLibrary) + minTimeToBeInLibrary));

            try {
                Thread.sleep(timeToBeInLibrary);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            library.finishWrite(this);
        }
    }

    @Override
    public String toString() {
        return "Writer " + id;
    }
}
