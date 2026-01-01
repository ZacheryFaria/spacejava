package xyz.faria.space.spaceapi.client;

import java.util.logging.Logger;

public class ApiLimiter {

    private static final Logger logger = Logger.getLogger(ApiLimiter.class.getName());

    private static final ApiLimiter INSTANCE = new ApiLimiter(2.3f);
    private final float requestsPerSecond;
    private volatile long lastAcquireTime;

    public ApiLimiter(float requestsPerSecond) {
        lastAcquireTime = 0;
        this.requestsPerSecond = requestsPerSecond;
    }

    public static ApiLimiter getInstance() {
        return INSTANCE;
    }

    public synchronized void acquire() {
        var now = System.currentTimeMillis();
        logger.info(
            String.format("Acquiring rate limiter. Waiting for %d ms", nextLimitTime() - now));
        if (now < nextLimitTime()) {
            try {
                wait(nextLimitTime() - now);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        lastAcquireTime = now;
    }

    private long nextLimitTime() {
        return lastAcquireTime + (long) (1000 / requestsPerSecond);
    }

}
