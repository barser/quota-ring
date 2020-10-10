package ru.barser.quota_ring;

public class QuotaExceedException extends Exception {

    private long suggestedWaitTimeMs;

    public QuotaExceedException(String message, long suggestedWaitTimeMs) {
        super(message);
        this.suggestedWaitTimeMs = suggestedWaitTimeMs;
    }

    public long getSuggestedWaitTimeMs() {
        return suggestedWaitTimeMs;
    }
}
