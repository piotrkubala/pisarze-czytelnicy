package pl.edu.agh.kis.pz1;

/**
 * Klasa reprezentująca czytelnika.
 */
public class Reader extends Human {
    /**
     * Konstruktor czytelnika.
     * @param libraryArg biblioteka, do której należy czytelnik
     * @param minTimeToWaitArg minimalny czas jaki czytelnik spędzi przed powrotem do biblioteki
     * @param maxTimeToWaitArg maksymalny czas jaki czytelnik spędzi przed powrotem do biblioteki
     * @param minTimeToBeInLibraryArg minimalny czas jaki czytelnik spędzi w bibliotece
     * @param maxTimeToBeInLibraryArg maksymalny czas jaki czytelnik spędzi w bibliotece
     * @param idArg id czytelnika
     */
    public Reader(Library libraryArg, float minTimeToWaitArg, float maxTimeToWaitArg, float minTimeToBeInLibraryArg, float maxTimeToBeInLibraryArg, int idArg) {
        super(libraryArg, minTimeToWaitArg, maxTimeToWaitArg, minTimeToBeInLibraryArg, maxTimeToBeInLibraryArg, idArg);
        type = HumanType.READER;
    }

    /**
     * Główna metoda wątku czytelnika.
     */
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

    /**
     * Metoda wyświetlająca informacje o czytelniku.
     * @return string zawierający informacje o czytelniku
     */
    @Override
    public String toString() {
        return "Reader " + id;
    }
}
