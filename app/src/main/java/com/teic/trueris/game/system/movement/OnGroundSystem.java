package com.teic.trueris.game.system.movement;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.game.query.position.GroundCheckQuery;
import com.teic.trueris.game.query.position.GroundCheckResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class OnGroundSystem {
    private final World world;
    private final EventBus eventBus;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World and EventBus is intentionally shared between systems."
    )
    public OnGroundSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(GroundCheckResponse.class, event -> {
            int entityId = event.entityId();

            world.put(entityId, new OnGround(!event.isClear()));
        });
    }

    public void update() {
        List<Integer> entityIds = world.query(OnGround.class);

        for (int entityId : entityIds) {
            eventBus.publish(new GroundCheckQuery(entityId));
        }
    }
}
