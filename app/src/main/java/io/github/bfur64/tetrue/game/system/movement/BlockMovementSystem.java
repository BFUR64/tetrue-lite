package io.github.bfur64.tetrue.game.system.movement;

import io.github.bfur64.tetrue.Config;
import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.World;
import io.github.bfur64.tetrue.game.component.Position;
import io.github.bfur64.tetrue.game.query.position.*;
import io.github.bfur64.tetrue.game.timer.GravityTimerExpired;
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
                Position oldPosition = world.get(event.entityId(), Position.class);

                world.put(event.entityId(), new Position(
                    oldPosition.x(),
                    oldPosition.y() + event.dy()
                ));
            }
        });

        eventBus.subscribe(GravityTimerExpired.class, event -> {
            if (Config.gravityEnabled.get()) {
                moveBlockDown(event.entityId());
            }
        });
    }

    public void moveBlockDown(int entityId) {
        eventBus.publish(new MoveDownQuery(entityId));
    }

    public void dropBlock(int entityId) {
        eventBus.publish(new DropDownQuery(entityId));
    }

    public void moveBlockLeft(int entityId) {
        eventBus.publish(new MoveXQuery(entityId, -1));
    }

    public void moveBlockRight(int entityId) {
        eventBus.publish(new MoveXQuery(entityId, 1));
    }
}
