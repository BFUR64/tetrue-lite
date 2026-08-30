package io.github.bfur64.tetrue.game.system.presentation;

import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.World;
import io.github.bfur64.tetrue.game.block.BlockFactory;
import io.github.bfur64.tetrue.game.component.*;
import io.github.bfur64.tetrue.game.query.position.GhostPositionQuery;
import io.github.bfur64.tetrue.game.query.position.GhostPositionResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class GhostBlockSystem {
    private final BlockFactory blockFactory;
    private final World world;
    private final EventBus eventBus;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "BlockFactory, World, and EventBus is intentionally shared between systems."
    )
    public GhostBlockSystem(BlockFactory blockFactory, World world, EventBus eventBus) {
        this.blockFactory = blockFactory;
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(GhostPositionResponse.class, event -> {
            int entityId = event.entityId();

            Position oldPosition = world.get(entityId, Position.class);

            world.put(entityId, new Position(
                oldPosition.x(),
                oldPosition.y() + event.dy()
            ));
        });
    }

    public void update() {
        List<Integer> entityIds = world.query(HasGhost.class);

        for (int entityId : entityIds) {
            HasGhost hasGhost = world.get(entityId, HasGhost.class);

            if (hasGhost.childId() == null) {
                int ghostBlockId = blockFactory.createGhostBlock(entityId);
                world.put(entityId, new HasGhost(ghostBlockId));
            }
        }

        List<Integer> ghostEntityIds = world.query(IsGhost.class);

        for (int ghostEntityId : ghostEntityIds) {
            IsGhost isGhost = world.get(ghostEntityId, IsGhost.class);
            int parentId = isGhost.parentId();

            if (world.has(parentId, Held.class) || !world.exists(parentId)) {
                world.remove(ghostEntityId);
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
