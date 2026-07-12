package com.mythicalswords.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Lightweight tick-based task scheduler.
 *
 * Why this exists: several abilities used {@code world.getServer().execute(...)}
 * to "delay" work over time, but {@code execute} runs the task on the very next
 * server task drain (same tick), so multi-tick effects (Serpent Strike's 5s
 * duration, Swift Strikes' staggered slashes) all fired in a single burst.
 *
 * Tasks scheduled here run after the requested number of full server ticks
 * (minimum 1 tick = 50ms), driven by END_SERVER_TICK.
 */
public final class ServerScheduler {

    private record Task(long runAt, Runnable action) {}

    private static final List<Task> TASKS = new ArrayList<>();
    private static long currentTick = 0;

    private ServerScheduler() {}

    /** Register the tick driver. Call once from the mod initializer. */
    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            currentTick++;
            if (TASKS.isEmpty()) {
                return;
            }
            // Collect due tasks first, then run (so a task may safely re-schedule).
            List<Runnable> due = new ArrayList<>();
            Iterator<Task> it = TASKS.iterator();
            while (it.hasNext()) {
                Task t = it.next();
                if (currentTick >= t.runAt()) {
                    due.add(t.action());
                    it.remove();
                }
            }
            for (Runnable r : due) {
                r.run();
            }
        });
    }

    /**
     * Run {@code action} after {@code delayTicks} server ticks (minimum 1).
     */
    public static void schedule(int delayTicks, Runnable action) {
        TASKS.add(new Task(currentTick + Math.max(1, delayTicks), action));
    }
}
