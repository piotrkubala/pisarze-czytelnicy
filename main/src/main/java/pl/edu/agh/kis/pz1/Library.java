package pl.edu.agh.kis.pz1;

import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

public class Library {
    Logger logger = Logger.getLogger(Library.class.getName());

    boolean shouldEnd = false;

    int maxReaders;

    Semaphore readerAndWriterSemaphore;
    Semaphore librarySemaphore = new Semaphore(1);

    Queue<Human> waitingQueue = new SynchronousQueue<>();

    public Library() {
        maxReaders = 5;

        createSemaphore();
    }

    public Library(int maxReadersArg) {
        maxReaders = maxReadersArg;

        createSemaphore();
    }

    void createSemaphore() {
        readerAndWriterSemaphore = new Semaphore(maxReaders);
    }

    public synchronized void processWaitingQueue() {
        while (!shouldEnd) {
            if (readerAndWriterSemaphore.availablePermits() > 0 && !waitingQueue.isEmpty()) {
                Human human = waitingQueue.poll();

                if (human == null) {
                    continue;
                }

                if (human.type == Human.HumanType.READER) {
                    readerAndWriterSemaphore.acquireUninterruptibly();
                    logger.info(human.toString() + " is reading");
                } else {
                    readerAndWriterSemaphore.acquireUninterruptibly(maxReaders);
                    logger.info(human.toString() + " is writing");
                }
                human.humanSemaphore.release();
            }
        }
    }

    public void requestRead(Reader reader) {
        logger.info("Reader " + reader.toString() + " requested read");
        waitingQueue.add(reader);

        reader.humanSemaphore.acquireUninterruptibly();
    }

    public void requestWrite(Writer writer) {
        logger.info("Writer " + writer.toString() + " requested write");
        waitingQueue.add(writer);

        writer.humanSemaphore.acquireUninterruptibly();
    }

    public void finishRead(Reader reader) {
        logger.info("Reader " + reader.toString() + " finished reading");
        readerAndWriterSemaphore.release();
    }

    public void finishWrite(Writer writer) {
        logger.info("Writer " + writer.toString() + " finished writing");
        readerAndWriterSemaphore.release(maxReaders);
    }
}
