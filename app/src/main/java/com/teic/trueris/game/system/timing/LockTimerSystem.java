package com.teic.trueris.game.system.timing;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.timer.LockTimer;
import com.teic.trueris.timer.LockTimerExpired;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.List;

@NullMarked
public class LockTimerSystem {
    private final World world;
    private final EventBus eventBus;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World and EventBus is intentionally shared between systems."
    )
    public LockTimerSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;
    }

    public void update(long delta) {
        if (!Config.lockTimerEnabled.get()) {
            return;
        }

        List<Integer> entityIds = world.query(LockTimer.class, OnGround.class);

        for (Integer entityId : entityIds) {
            boolean onGround = world.get(entityId, OnGround.class).onGround();
            LockTimer oldLockTimer = world.get(entityId, LockTimer.class);

            if (onGround) {
                long newTime = oldLockTimer.duration() - delta;

                if (newTime <= 0) {
                    eventBus.publish(new LockTimerExpired(entityId));
                    continue;
                }

                world.put(entityId, new LockTimer(newTime));
            }
            else {
                world.put(
                    entityId,
                    new LockTimer(Duration.ofMillis(Config.lockTimer.get()).toNanos())
                );
            }
        }
    }
}
