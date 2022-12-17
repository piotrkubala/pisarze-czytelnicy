package pl.edu.agh.kis.pz1;

import sun.misc.Signal;

/**
 * Schemat @author Paweł Skrzyński
 * Algorytm @author Piotr Kubala
 */
public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Wrong number of arguments");
            return;
        }

        try {
            int readersCount = Integer.parseInt(args[0]);
            int writersCount = Integer.parseInt(args[1]);

            if (readersCount < 0 || writersCount < 0) {
                System.out.println("Wrong arguments");
                return;
            }

            Library library = new Library();

            Signal.handle(new Signal("INT"), signal -> {
                library.stopLibrary();
            });

            for (int i = 0; i < readersCount; i++) {
                Reader reader = new Reader(library, 1, 2, 1, 3, i);
                library.addHuman(reader);
                reader.start();
            }

            for (int i = 0; i < writersCount; i++) {
                Writer writer = new Writer(library, 1, 2, 4, 6, i);
                library.addHuman(writer);
                writer.start();
            }

            library.processWaitingQueue();
        } catch (NumberFormatException e) {
            System.out.println("Wrong arguments");
            return;
        }
    }
}
