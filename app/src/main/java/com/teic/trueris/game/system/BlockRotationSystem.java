package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.block.Direction;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.event.rotation.MoveRotationQuery;
import com.teic.trueris.game.event.rotation.MoveRotationResponse;
import com.teic.trueris.game.utils.RotationHelper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BlockRotationSystem {
    private final World world;
    private final EventBus eventBus;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World and EventBus is intentionally shared between systems."
    )
    public BlockRotationSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveRotationResponse.class, event -> {
            Integer entityId = event.entityId();

            if (event.isValid() && world.has(entityId, Position.class, Rotation.class)) {
                Position oldPosition = world.get(entityId, Position.class);

                world.put(entityId, new Position(
                    oldPosition.x() + event.dx(),
                    oldPosition.y() + event.dy()
                ));

                world.put(entityId, new Rotation(Direction.fromId(event.direction())));
            }
        });
    }

    public void rotateLeft(Integer entityId) {
        if (world.has(entityId, Position.class, Rotation.class)) {
            Rotation rotation = world.get(entityId, Rotation.class);
            int direction = RotationHelper.rotateLeft(rotation.direction());

            eventBus.publish(new MoveRotationQuery(entityId, direction, 0, 0));
        }
    }

    public void rotateRight(Integer entityId) {
        if (world.has(entityId, Rotation.class)) {
            Rotation rotation = world.get(entityId, Rotation.class);
            int direction = RotationHelper.rotateRight(rotation.direction());

            eventBus.publish(new MoveRotationQuery(entityId, direction, 0, 0));
        }
    }
}
