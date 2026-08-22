package com.teic.trueris.game.system;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.event.GravityExpired;
import com.teic.trueris.game.event.GravityTimer;
import com.teic.trueris.game.event.MoveBlockAccepted;

import java.time.Duration;
import java.util.List;

public class GravitySystem {
    private final World world;
    private final EventBus eventBus;

    public GravitySystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveBlockAccepted.class, event -> {
            if (event.dy() > 0) {
                if (world.has(event.entityId(), GravityTimer.class)) {
                    world.add(event.entityId(), new GravityTimer(Duration.ofMillis(Config.gravity.get()).toNanos()));
                }
            }
        });
    }

    public void update(long delta) {
        List<Integer> entityIds = world.query(GravityTimer.class);

        for (Integer entityId : entityIds) {
            GravityTimer oldGravity = world.get(entityId, GravityTimer.class);
            long newGravityDuration = oldGravity.duration() - delta;

            if (newGravityDuration <= 0) {
                eventBus.publish(new GravityExpired(entityId));
                newGravityDuration = Duration.ofMillis(Config.gravity.get()).toNanos();
            }

            world.add(entityId, new GravityTimer(newGravityDuration));
        }
    }
}
