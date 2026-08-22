package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.game.event.MoveBlockQuery;
import com.teic.trueris.game.event.MoveBlockQueryResponse;

import java.util.List;

public class OnGroundSystem {
    private final World world;
    private final EventBus eventBus;

    public OnGroundSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveBlockQueryResponse.class, event -> {
            Integer entityId = event.entityId();

            if (world.has(entityId, OnGround.class)) {
                world.put(entityId, new OnGround(!event.isValid()));
            }
        });
    }

    public void update(long delta) {
        List<Integer> entityIds = world.query(OnGround.class);

        for (Integer entityId : entityIds) {
            eventBus.publish(new MoveBlockQuery(entityId, 0, 1));
        }
    }
}
