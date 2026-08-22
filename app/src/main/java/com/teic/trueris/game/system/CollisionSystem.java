package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.event.MoveBlockAccepted;
import com.teic.trueris.game.event.MoveBlockCommand;

public class CollisionSystem {
    private final World world;
    private final EventBus eventBus;

    public CollisionSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveBlockCommand.class, event -> {
            eventBus.publish(new MoveBlockAccepted(event.entityId(), event.dx(), event.dy()));
        });
    }
}
