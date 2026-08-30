package io.github.bfur64.tetrue.game.system.timing;

import io.github.bfur64.tetrue.Config;
import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.World;
import io.github.bfur64.tetrue.game.component.OnGround;
import io.github.bfur64.tetrue.game.timer.LockTimer;
import io.github.bfur64.tetrue.game.timer.LockTimerExpired;
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

    public void update(double delta) {
        if (!Config.lockTimerEnabled.get()) {
            return;
        }

        List<Integer> entityIds = world.query(LockTimer.class, OnGround.class);

        for (int entityId : entityIds) {
            boolean onGround = world.get(entityId, OnGround.class).onGround();
            LockTimer oldLockTimer = world.get(entityId, LockTimer.class);

            if (onGround) {
                double newTime = oldLockTimer.duration() - delta;

                if (newTime <= 0) {
                    eventBus.publish(new LockTimerExpired(entityId));
                    continue;
                }

                world.put(entityId, new LockTimer(newTime));
            }
            else {
                world.put(
                    entityId,
                    new LockTimer(Config.lockDelay.get() / 1000.0)
                );
            }
        }
    }
}
