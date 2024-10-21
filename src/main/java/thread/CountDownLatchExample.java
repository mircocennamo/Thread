package thread;

import java.util.concurrent.CountDownLatch;

/**
 * @author mirco.cennamo on 21/10/2024
 * @project Thread
 */
public class CountDownLatchExample {

    public static void main(String[] args) {
        int threadCount = 3;
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(new Worker(latch)).start();
        }

        try {
            latch.await();  // Wait for all workers to finish
            System.out.println("All workers have finished their tasks.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Worker implements Runnable {
    private final CountDownLatch latch;

    Worker(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " is working.");
            Thread.sleep((long) (Math.random() * 1000));  // Simulate work
            System.out.println(Thread.currentThread().getName() + " has finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();  // Decrement the count
        }
    }
}
