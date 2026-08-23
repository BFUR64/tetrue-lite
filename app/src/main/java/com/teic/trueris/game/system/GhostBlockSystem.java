package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.component.*;
import com.teic.trueris.game.event.GhostPositionQuery;
import com.teic.trueris.game.event.GhostPositionResponse;

import java.util.List;

public class GhostBlockSystem {
    private final BlockFactory blockFactory;
    private final World world;
    private final EventBus eventBus;

    public GhostBlockSystem(BlockFactory blockFactory, World world, EventBus eventBus) {
        this.blockFactory = blockFactory;
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(GhostPositionResponse.class, event -> {
            Integer entityId = event.entityId();

            if (world.has(entityId, Position.class)) {
                Position oldPosition = world.get(entityId, Position.class);

                world.put(entityId, new Position(
                    oldPosition.x(),
                    oldPosition.y() + event.dy()
                ));
            }
        });
    }

    public void update() {
        List<Integer> entityIds = world.query(HasGhost.class);

        for (Integer entityId : entityIds) {
            HasGhost hasGhost = world.get(entityId, HasGhost.class);

            if (hasGhost.childId() == null) {
                Integer ghostBlockId = blockFactory.createGhostBlock(entityId);
                world.put(entityId, new HasGhost(ghostBlockId));
            }
        }

        List<Integer> ghostEntityIds = world.query(IsGhost.class);

        for (Integer ghostEntityId : ghostEntityIds) {
            IsGhost isGhost = world.get(ghostEntityId, IsGhost.class);
            Integer parentId = isGhost.parentId();

            if (world.has(parentId, Held.class) || !world.exists(parentId)) {
                world.remove(ghostEntityId);
                continue;
            }

            if (!world.has(parentId, Position.class, Rotation.class)) {
                continue;
            }

            Position parentPosition = world.get(parentId, Position.class);
            Rotation parentRotation = world.get(parentId, Rotation.class);

            world.put(ghostEntityId, new Position(parentPosition.x(), parentPosition.y()));
            world.put(ghostEntityId, new Rotation(parentRotation.direction()));

            eventBus.publish(new GhostPositionQuery(ghostEntityId));
        }
    }
}
