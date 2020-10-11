package ru.barser.quota_ring;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class QuotaRingTest {

    @Test
    public void test() throws InterruptedException {

        QuotaRing quotaRing = new QuotaRing(75, 1);

        final AtomicInteger counter = new AtomicInteger(0);
        final int tasksPerThread = 5;
        int threadNumber = 100;
        Thread[] threads = new Thread[threadNumber];

        for (int t = 0; t < threadNumber; t++) {
            Thread thread = new Thread(() -> {
                for (int i = 0; i < tasksPerThread; ) {
                    try {
                        quotaRing.reserveQuota();
                        System.out.println(Thread.currentThread().getName() +
                                " Quota reserved. MAKING SOME WORK " + (i + 1));
                        i++;
                        counter.getAndIncrement();
                    } catch (QuotaExceedException exc) {
                        System.out.println("quota exceeds! wait for " + exc.getSuggestedWaitTimeMs() + " ms");
                        try {
                            Thread.sleep(exc.getSuggestedWaitTimeMs());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            threads[t] = thread;
        }
        LocalDateTime start = LocalDateTime.now();

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        LocalDateTime stop = LocalDateTime.now();

        System.out.println("==========================================================");
        System.out.printf("* Started at %1$tH:%1$tM:%1$tS%n", start);
        System.out.printf("* Stopped at %1$tH:%1$tM:%1$tS%n", stop);
        System.out.printf("* Delta = %d seconds%n", Duration.between(start, stop).getSeconds());
        System.out.printf("* Tasks done: %d%n", counter.get());

        Assert.assertEquals(counter.get(), tasksPerThread * threadNumber);
    }
}
