package fr.sny1411.bingo.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

/**
 * Measures real elapsed time, independently of the server's ticks. It can be moved forward or sped up for testing.
 */
public final class Stopwatch {
    private final LongSupplier nanoClock;
    private long baseNanos;
    private long sinceNanos;
    private int speed;

    public Stopwatch() {
        this(System::nanoTime);
    }

    Stopwatch(LongSupplier nanoClock) {
        this.nanoClock = nanoClock;
    }

    public void start() {
        baseNanos = 0;
        sinceNanos = nanoClock.getAsLong();
        speed = 1;
    }

    public int elapsedSeconds() {
        return (int) TimeUnit.NANOSECONDS.toSeconds(elapsedNanos());
    }

    public void setElapsedSeconds(int seconds) {
        baseNanos = TimeUnit.SECONDS.toNanos(seconds);
        sinceNanos = nanoClock.getAsLong();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        baseNanos = elapsedNanos();
        sinceNanos = nanoClock.getAsLong();
        this.speed = speed;
    }

    private long elapsedNanos() {
        return baseNanos + (nanoClock.getAsLong() - sinceNanos) * speed;
    }
}
