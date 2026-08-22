package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.event.GravityExpired;
import com.teic.trueris.game.event.MoveBlockAccepted;
import com.teic.trueris.game.event.MoveBlockCommand;

public class BlockMovementSystem {
    private final World world;
    private final EventBus eventBus;

    public BlockMovementSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveBlockAccepted.class, event -> {
            Position oldPosition = world.get(event.entityId(), Position.class);

            world.put(event.entityId(), new Position(
                oldPosition.x() + event.dx(),
                oldPosition.y() + event.dy())
            );
        });

        eventBus.subscribe(GravityExpired.class, event -> {
            moveBlockDown(event.entityId());
        });
    }

    public void moveBlockDown(Integer entityId) {
        eventBus.publish(new MoveBlockCommand(entityId, 0, 1));
    }

    public void moveBlockLeft(Integer entityId) {
        eventBus.publish(new MoveBlockCommand(entityId, -1, 0));
    }

    public void moveBlockRight(Integer entityId) {
        eventBus.publish(new MoveBlockCommand(entityId, 1, 0));
    }
}
