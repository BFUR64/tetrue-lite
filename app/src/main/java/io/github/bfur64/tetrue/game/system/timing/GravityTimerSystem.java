package io.github.bfur64.tetrue.game.system.timing;

import io.github.bfur64.tetrue.Config;
import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.World;
import io.github.bfur64.tetrue.game.component.OnGround;
import io.github.bfur64.tetrue.game.event.GravityChangeEvent;
import io.github.bfur64.tetrue.game.event.LineClearEvent;
import io.github.bfur64.tetrue.game.timer.GravityTimerExpired;
import io.github.bfur64.tetrue.game.timer.GravityTimer;
import io.github.bfur64.tetrue.game.query.position.MoveDownResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.List;

@NullMarked
public class GravityTimerSystem {
    private final World world;
    private final EventBus eventBus;

    private int gravityMs = Config.gravityMs.get();

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World and EventBus is intentionally shared between systems."
    )
    public GravityTimerSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveDownResponse.class, event -> {
            if (!Config.gravityEnabled.get()) {
                return;
            }

            if (event.canDrop()) {
                setNewGravityMs(event.entityId(), gravityMs);
            }
        });

        eventBus.subscribe(LineClearEvent.class, event -> {
            if (!Config.gravityEnabled.get()) {
                return;
            }

            gravityMs = Math.max(Config.GRAVITY_MIN, gravityMs - Config.speedStep.get());

            eventBus.publish(new GravityChangeEvent(gravityMs));
        });
    }

    public void update(long delta) {
        if (!Config.gravityEnabled.get()) {
            return;
        }

        List<Integer> entityIds = world.query(GravityTimer.class, OnGround.class);

        for (int entityId : entityIds) {
            boolean onGround = world.get(entityId, OnGround.class).onGround();

            if (!onGround) {
                GravityTimer oldGravity = world.get(entityId, GravityTimer.class);
                long newGravityDuration = oldGravity.duration() - delta;

                if (newGravityDuration <= 0) {
                    eventBus.publish(new GravityTimerExpired(entityId));
                    setNewGravityMs(entityId, gravityMs);
                }
                else {
                    setNewGravityNs(entityId, newGravityDuration);
                }
            }
            else {
                setNewGravityMs(entityId, gravityMs);
            }
        }
    }

    private void setNewGravityMs(int entityId, int gravityMs) {
        world.put(entityId, new GravityTimer(Duration.ofMillis(gravityMs).toNanos()));
    }

    private void setNewGravityNs(int entityId, long gravityNs) {
        world.put(entityId, new GravityTimer(gravityNs));
    }
}
