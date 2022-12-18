package pl.edu.agh.kis.pz1;

/**
 * Klasa reprezentująca pisarza.
 */
public class Writer extends Human {
    /**
     * Konstruktor pisarza.
     * @param libraryArg biblioteka, do której przychodzi pisarz
     * @param minTimeToWaitArg minimalny czas jaki pisarz spędzi przed powrotem do biblioteki
     * @param maxTimeToWaitArg maksymalny czas jaki pisarz spędzi przed powrotem do biblioteki
     * @param minTimeToBeInLibraryArg minimalny czas jaki pisarz spędzi w bibliotece
     * @param maxTimeToBeInLibraryArg maksymalny czas jaki pisarz spędzi w bibliotece
     * @param idArg id czytelnika
     */
    public Writer(Library libraryArg, float minTimeToWaitArg, float maxTimeToWaitArg, float minTimeToBeInLibraryArg, float maxTimeToBeInLibraryArg, int idArg) {
        super(libraryArg, minTimeToWaitArg, maxTimeToWaitArg, minTimeToBeInLibraryArg, maxTimeToBeInLibraryArg, idArg);
        type = HumanType.WRITER;
    }

    /**
     * Główna metoda wątku pisarza.
     */
    @Override
    public void run() {
        while (!shouldStop) {
            long timeToWait = (long) (1000 * (Math.random() * (maxTimeToWait - minTimeToWait) + minTimeToWait));

            sleepForTime(timeToWait);

            if (!shouldStop) {
                library.requestWrite(this);

                if (shouldStop) {
                    break;
                }

                long timeToBeInLibrary = (long) (1000 * (Math.random() * (maxTimeToBeInLibrary - minTimeToBeInLibrary) + minTimeToBeInLibrary));

                sleepForTime(timeToBeInLibrary);

                library.finishWrite(this);
            }
        }
    }

    /**
     * Metoda wyświetlająca informacje o pisarzu.
     * @return string zawierający informacje o pisarzu
     */
    @Override
    public String toString() {
        return "Writer " + id;
    }
}
