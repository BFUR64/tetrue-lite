package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.event.*;

public class BlockMovementSystem {
    private final World world;
    private final EventBus eventBus;

    public BlockMovementSystem(World world, EventBus eventBus) {
        this.world = world;
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

        eventBus.subscribe(GravityExpired.class, event -> {
            moveBlockDown(event.entityId());
        });

        eventBus.subscribe(LockTimerExpired.class, event -> {
            moveBlockDown(event.entityId());
        });
    }

    public void moveBlockDown(Integer entityId) {
        eventBus.publish(new MoveDownQuery(entityId));
    }

    public void moveBlockLeft(Integer entityId) {
        eventBus.publish(new MoveXQuery(entityId, -1));
    }

    public void moveBlockRight(Integer entityId) {
        eventBus.publish(new MoveXQuery(entityId, 1));
    }
}
