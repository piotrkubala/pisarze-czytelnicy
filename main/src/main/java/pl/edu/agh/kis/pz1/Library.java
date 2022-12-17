package pl.edu.agh.kis.pz1;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Library {
    Logger logger = Logger.getLogger(Library.class.getName());

    /**
     * Formatter for the logger.
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
                    sb.append(sw.toString());
                } catch (Exception ex) {
                    // ignore all exceptions here
                }
            }
            return sb.toString();
        }
    }

    boolean shouldEnd = false;

    int maxReaders;

    int writersCount = 0;
    int readersCount = 0;

    Semaphore readerAndWriterSemaphore;
    Semaphore librarySemaphore = new Semaphore(0);

    BlockingQueue<Human> waitingQueue = new LinkedBlockingQueue<>();

    Vector<Human> humans = new Vector<>();

    public Library() {
        maxReaders = 5;

        createSemaphoreAndConfigureLogger();
    }

    public Library(int maxReadersArg) {
        maxReaders = maxReadersArg;

        createSemaphoreAndConfigureLogger();
    }

    void createSemaphoreAndConfigureLogger() {
        readerAndWriterSemaphore = new Semaphore(maxReaders);

        logger.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomRecordFormatter());
        logger.addHandler(consoleHandler);
    }

    public void addHuman(Human human) {
        humans.add(human);
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

        logger.info(sb.toString());
    }

    public void processWaitingQueue() {
        while (!shouldEnd) {
            librarySemaphore.acquireUninterruptibly();

            while (!waitingQueue.isEmpty()) {
                printQueueInfo();

                Human human = waitingQueue.poll();

                if (human == null) {
                    continue;
                }

                if (human.type == Human.HumanType.READER) {
                    readerAndWriterSemaphore.acquireUninterruptibly();
                    logger.info(human.toString() + " is reading");
                    readersCount++;
                } else {
                    readerAndWriterSemaphore.acquireUninterruptibly(maxReaders);
                    logger.info(human.toString() + " is writing");
                    writersCount++;
                }
                human.humanSemaphore.release();
            }
        }
    }

    public void stopLibrary() {
        logger.info("Stopping library");

        for (Human human : humans) {
            human.stopHuman();
        }

        shouldEnd = true;
        waitingQueue.clear();
        librarySemaphore.release(humans.size());

        logger.info("Library stopped");
    }

    public void requestRead(Reader reader) {
        logger.info(reader.toString() + " requested read");
        waitingQueue.add(reader);

        librarySemaphore.release();
        reader.humanSemaphore.acquireUninterruptibly();
    }

    public void requestWrite(Writer writer) {
        logger.info(writer.toString() + " requested write");
        waitingQueue.add(writer);

        librarySemaphore.release();
        writer.humanSemaphore.acquireUninterruptibly();
    }

    public void finishRead(Reader reader) {
        logger.info(reader.toString() + " finished reading");
        readerAndWriterSemaphore.release();
        readersCount--;

        librarySemaphore.release();
    }

    public void finishWrite(Writer writer) {
        logger.info(writer.toString() + " finished writing");
        readerAndWriterSemaphore.release(maxReaders);
        writersCount--;

        librarySemaphore.release();
    }
}
