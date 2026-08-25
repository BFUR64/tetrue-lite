package com.teic.trueris.game.system.timing;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.game.event.GravityChangeEvent;
import com.teic.trueris.game.event.LineClearEvent;
import com.teic.trueris.timer.GravityTimerExpired;
import com.teic.trueris.timer.GravityTimer;
import com.teic.trueris.game.query.position.MoveDownResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.List;

@NullMarked
public class GravityTimerSystem {
    private final World world;
    private final EventBus eventBus;

    private int gravity = Config.gravity.get();

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
                if (world.has(event.entityId(), GravityTimer.class)) {
                    world.put(event.entityId(), new GravityTimer(gravity));
                }
            }
        });

        eventBus.subscribe(LineClearEvent.class, event -> {
            if (!Config.gravityEnabled.get()) {
                return;
            }

            gravity = Math.max(Config.GRAVITY_MIN, gravity - 40);

            eventBus.publish(new GravityChangeEvent(gravity));
        });
    }

    public void update(long delta) {
        if (!Config.gravityEnabled.get()) {
            return;
        }

        List<Integer> entityIds = world.query(GravityTimer.class, OnGround.class);

        for (Integer entityId : entityIds) {
            boolean onGround = world.get(entityId, OnGround.class).onGround();
            GravityTimer oldGravity = world.get(entityId, GravityTimer.class);

            if (!onGround) {
                long newGravityDuration = oldGravity.duration() - delta;

                if (newGravityDuration <= 0) {
                    eventBus.publish(new GravityTimerExpired(entityId));
                    newGravityDuration = Duration.ofMillis(gravity).toNanos();
                }

                world.put(entityId, new GravityTimer(newGravityDuration));
            }
            else {
                world.put(entityId, new GravityTimer(Duration.ofMillis(gravity).toNanos()));
            }
        }
    }
}
