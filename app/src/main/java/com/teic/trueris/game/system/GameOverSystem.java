package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.event.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GameOverSystem {
    private final World world;
    private final EventBus eventBus;

    public GameOverSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(BlockSpawnEvent.class, event -> {
            eventBus.publish(new PositionValidQuery(event.entityId()));
        });

        eventBus.subscribe(PositionValidResponse.class, event -> {
            if (!event.isValid()) {
                eventBus.publish(new GameOverEvent());
            }
        });
    }
}
