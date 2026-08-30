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

import java.util.List;

@NullMarked
public class GravityTimerSystem {
    private final World world;
    private final EventBus eventBus;

    private double gravity = Config.gravityMs.get() / 1000.0d;

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
                setNewGravity(event.entityId(), gravity);
            }
        });

        eventBus.subscribe(LineClearEvent.class, event -> {
            if (!Config.gravityEnabled.get()) {
                return;
            }

            gravity = Math.max(Config.GRAVITY_MIN / 1000.0d, gravity - Config.speedStep.get() / 1000.0d);

            eventBus.publish(new GravityChangeEvent(gravity));
        });
    }

    public void update(double delta) {
        if (!Config.gravityEnabled.get()) {
            return;
        }

        List<Integer> entityIds = world.query(GravityTimer.class, OnGround.class);

        for (int entityId : entityIds) {
            boolean onGround = world.get(entityId, OnGround.class).onGround();

            if (!onGround) {
                GravityTimer oldGravity = world.get(entityId, GravityTimer.class);
                double newGravityDuration = oldGravity.duration() - delta;

                if (newGravityDuration <= 0) {
                    eventBus.publish(new GravityTimerExpired(entityId));
                    setNewGravity(entityId, gravity);
                }
                else {
                    setNewGravity(entityId, newGravityDuration);
                }
            }
            else {
                setNewGravity(entityId, gravity);
            }
        }
    }

    private void setNewGravity(int entityId, double gravity) {
        world.put(entityId, new GravityTimer(gravity));
    }
}
