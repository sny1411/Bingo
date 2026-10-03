package fr.sny1411.bingo.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

/**
 * Measures real elapsed time, independently of the server's ticks.
 */
public final class Stopwatch {
    private final LongSupplier nanoClock;
    private long startNanos;

    public Stopwatch() {
        this(System::nanoTime);
    }

    Stopwatch(LongSupplier nanoClock) {
        this.nanoClock = nanoClock;
    }

    public void start() {
        startNanos = nanoClock.getAsLong();
    }

    public int elapsedSeconds() {
        return (int) TimeUnit.NANOSECONDS.toSeconds(nanoClock.getAsLong() - startNanos);
    }
}
