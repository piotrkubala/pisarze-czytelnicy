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
                Thread.sleep(timeToWait * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            library.requestWrite(this);

            long timeToBeInLibrary = (long) (1000 * (Math.random() * (maxTimeToBeInLibrary - minTimeToBeInLibrary) + minTimeToBeInLibrary));

            try {
                Thread.sleep(timeToBeInLibrary * 1000);
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
