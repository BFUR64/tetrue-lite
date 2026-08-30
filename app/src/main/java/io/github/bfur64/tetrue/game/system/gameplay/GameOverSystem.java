package io.github.bfur64.tetrue.game.system.gameplay;

import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.event.*;
import io.github.bfur64.tetrue.game.query.position.PositionValidQuery;
import io.github.bfur64.tetrue.game.query.position.PositionValidResponse;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GameOverSystem {
    public GameOverSystem(EventBus eventBus) {
        eventBus.subscribe(BlockSpawnEvent.class,
            event -> eventBus.publish(new PositionValidQuery(event.entityId()))
        );

        eventBus.subscribe(PositionValidResponse.class, event -> {
            if (!event.isValid()) {
                eventBus.publish(new GameOverEvent());
            }
        });
    }
}
