package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.game.event.position.GroundCheckQuery;
import com.teic.trueris.game.event.position.GroundCheckResponse;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class OnGroundSystem {
    private final World world;
    private final EventBus eventBus;

    public OnGroundSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(GroundCheckResponse.class, event -> {
            Integer entityId = event.entityId();

            if (world.has(entityId, OnGround.class)) {
                world.put(entityId, new OnGround(!event.isClear()));
            }
        });
    }

    public void update() {
        List<Integer> entityIds = world.query(OnGround.class);

        for (Integer entityId : entityIds) {
            eventBus.publish(new GroundCheckQuery(entityId));
        }
    }
}
