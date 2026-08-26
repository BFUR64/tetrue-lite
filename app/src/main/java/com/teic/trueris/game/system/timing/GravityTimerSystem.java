package com.teic.trueris.game.system.timing;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.game.event.GravityChangeEvent;
import com.teic.trueris.game.event.LineClearEvent;
import com.teic.trueris.game.timer.GravityTimerExpired;
import com.teic.trueris.game.timer.GravityTimer;
import com.teic.trueris.game.query.position.MoveDownResponse;
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
                Integer entityId = event.entityId();

                if (world.has(entityId, GravityTimer.class)) {
                    setNewGravity(entityId, gravityMs);
                }
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

        for (Integer entityId : entityIds) {
            boolean onGround = world.get(entityId, OnGround.class).onGround();

            if (!onGround) {
                GravityTimer oldGravity = world.get(entityId, GravityTimer.class);
                long newGravityDuration = oldGravity.duration() - delta;

                if (newGravityDuration <= 0) {
                    eventBus.publish(new GravityTimerExpired(entityId));
                }
            }

            setNewGravity(entityId, gravityMs);
        }
    }

    private void setNewGravity(Integer entityId, int gravityMs) {
        world.put(entityId, new GravityTimer(Duration.ofMillis(gravityMs).toNanos()));
    }
}
