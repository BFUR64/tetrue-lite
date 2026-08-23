package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.event.position.*;
import com.teic.trueris.game.event.timer.GravityTimerExpired;
import com.teic.trueris.game.event.timer.LockTimerExpired;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BlockMovementSystem {
    private final EventBus eventBus;

    public BlockMovementSystem(World world, EventBus eventBus) {
        this.eventBus = eventBus;

        eventBus.subscribe(MoveXResponse.class, event -> {
            if (event.isValid() && world.has(event.entityId(), Position.class)) {
                Position oldPosition = world.get(event.entityId(), Position.class);

                world.put(event.entityId(), new Position(
                    oldPosition.x() + event.dx(),
                    oldPosition.y()
                ));
            }
        });

        eventBus.subscribe(MoveDownResponse.class, event -> {
            if (event.canDrop() && world.has(event.entityId(), Position.class)) {
                Position oldPosition = world.get(event.entityId(), Position.class);

                world.put(event.entityId(), new Position(
                    oldPosition.x(),
                    oldPosition.y() + 1
                ));
            }
        });

        eventBus.subscribe(DropDownResponse.class, event -> {
            if (world.has(event.entityId(), Position.class)) {
                if (event.canDrop()) {
                    Position oldPosition = world.get(event.entityId(), Position.class);

                    world.put(event.entityId(), new Position(
                        oldPosition.x(),
                        oldPosition.y() + 1
                    ));
                    
                    dropBlock(event.entityId());
                }
            }

        });

        eventBus.subscribe(GravityTimerExpired.class, event -> moveBlockDown(event.entityId()));

        eventBus.subscribe(LockTimerExpired.class, event -> moveBlockDown(event.entityId()));
    }

    public void moveBlockDown(Integer entityId) {
        eventBus.publish(new MoveDownQuery(entityId));
    }

    public void dropBlock(Integer entityId) {
        eventBus.publish(new DropDownQuery(entityId));
    }

    public void moveBlockLeft(Integer entityId) {
        eventBus.publish(new MoveXQuery(entityId, -1));
    }

    public void moveBlockRight(Integer entityId) {
        eventBus.publish(new MoveXQuery(entityId, 1));
    }
}
