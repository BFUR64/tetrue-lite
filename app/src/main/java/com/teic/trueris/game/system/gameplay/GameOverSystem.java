package com.teic.trueris.game.system.gameplay;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.event.*;
import com.teic.trueris.game.query.position.PositionValidQuery;
import com.teic.trueris.game.query.position.PositionValidResponse;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GameOverSystem {
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
