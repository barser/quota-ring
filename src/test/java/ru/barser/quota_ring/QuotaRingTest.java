package ru.barser.quota_ring;

import org.testng.annotations.Test;

public class QuotaRingTest {

    @Test
    public void test() throws InterruptedException{
        QuotaRing quotaRing = new QuotaRing(5, 1);

        Thread t1 = new Thread(() -> {
            for(int i = 0; i <= 15; ) {
                try {
                    quotaRing.reserveQuota();
                    System.out.println(Thread.currentThread().getName() + " Quota reserved. MAKING SOME WORK " + i);
                    i++;
                } catch (QuotaExceedException exc) {
                    System.out.println("quota exceeds!");
                    try {
                        Thread.sleep(exc.getSuggestedWaitTimeMs());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0; i <= 15; ) {
                try {
                    quotaRing.reserveQuota();
                    System.out.println(Thread.currentThread().getName() + " Quota reserved. MAKING SOME WORK " + i);
                    i++;
                } catch (QuotaExceedException exc) {
                    System.out.println("quota exceeds!");
                    try {
                        Thread.sleep(exc.getSuggestedWaitTimeMs());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
