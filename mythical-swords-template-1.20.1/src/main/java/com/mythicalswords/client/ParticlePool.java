package com.mythicalswords.client;

import java.util.concurrent.atomic.AtomicInteger;

public class ParticlePool {

    private static final int POOL_SIZE = 1000;
    private static final AtomicInteger active = new AtomicInteger(0);

    /** Acquire a particle slot. Returns false if pool exhausted this tick. */
    public static boolean acquire() {
        return active.incrementAndGet() <= POOL_SIZE;
    }

    /** Release a particle slot when done. */
    public static void release() {
        active.decrementAndGet();
    }

    /** Reset pool each client tick (called by WeaponAuraRenderer). */
    public static void resetTick() {
        active.set(0);
    }

    public static int getActiveCount() {
        return active.get();
    }
}
