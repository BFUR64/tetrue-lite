package com.teic.trueris.game.system;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.game.event.GravityExpired;
import com.teic.trueris.game.event.GravityTimer;
import com.teic.trueris.game.event.MoveDownResponse;
import com.teic.trueris.game.event.MoveXResponse;

import java.time.Duration;
import java.util.List;

public class GravitySystem {
    private final World world;
    private final EventBus eventBus;

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
                    eventBus.publish(new GravityExpired(entityId));
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
