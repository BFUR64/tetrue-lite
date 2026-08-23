package com.teic.trueris.game.system;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.game.event.timer.GravityTimerExpired;
import com.teic.trueris.game.event.timer.GravityTimer;
import com.teic.trueris.game.event.position.MoveDownResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.List;

@NullMarked
public class GravitySystem {
    private final World world;
    private final EventBus eventBus;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World and EventBus is intentionally shared between systems."
    )
    public GravitySystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveDownResponse.class, event -> {
            if (event.canDrop()) {
                if (world.has(event.entityId(), GravityTimer.class)) {
                    world.put(event.entityId(), new GravityTimer(Duration.ofMillis(Config.gravity.get()).toNanos()));
                }
            }
        });
    }

    public void update(long delta) {
        List<Integer> entityIds = world.query(GravityTimer.class, OnGround.class);

        for (Integer entityId : entityIds) {
            boolean onGround = world.get(entityId, OnGround.class).onGround();
            GravityTimer oldGravity = world.get(entityId, GravityTimer.class);

            if (!onGround) {
                long newGravityDuration = oldGravity.duration() - delta;

                if (newGravityDuration <= 0) {
                    eventBus.publish(new GravityTimerExpired(entityId));
                    newGravityDuration = Duration.ofMillis(Config.gravity.get()).toNanos();
                }

                world.put(entityId, new GravityTimer(newGravityDuration));
            }
            else {
                world.put(entityId, new GravityTimer(Duration.ofMillis(Config.gravity.get()).toNanos()));
            }
        }
    }
}
