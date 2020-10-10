package ru.barser.quota_ring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuotaRing {

    private final long interval;

    private final int quantity;

    private int pointer;

    private final List<Long> ring;

    public QuotaRing(int quantity, long intervalInSec) {
        this.quantity = quantity;
        this.interval = intervalInSec * 1000;
        this.ring = new ArrayList<>(quantity);
        for(int i = 0; i < quantity; i++) {
            ring.add(0L);
        }
        System.out.printf("QuotaRing initialized! Quota = %d operations per %d sec.%n",
                quantity, intervalInSec);
    }

    public synchronized void reserveQuota() throws QuotaExceedException {
        long currentTime = new Date().getTime();
        long previousTime = getPreviousTime();

        if (previousTime > 0 && currentTime - previousTime < interval) {
            throw new QuotaExceedException("Quota Exceed", interval - (currentTime - previousTime));

        } else {
            updateRing(currentTime);
        }
    }

    private void updateRing(long currentTime) {
        ring.remove(pointer);
        ring.add(pointer, currentTime);
        pointer++;
        if (pointer >= ring.size()) {
            pointer = 0;
        }
    }

    private long getPreviousTime() {
        return ring.get(pointer);
    }
}
