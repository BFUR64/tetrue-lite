package io.github.bfur64.tetrue.game.system.movement;

import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.World;
import io.github.bfur64.tetrue.game.block.Direction;
import io.github.bfur64.tetrue.game.component.Position;
import io.github.bfur64.tetrue.game.component.Rotation;
import io.github.bfur64.tetrue.game.query.rotation.Rotate180Query;
import io.github.bfur64.tetrue.game.query.rotation.Rotate180Response;
import io.github.bfur64.tetrue.game.query.rotation.RotateQuery;
import io.github.bfur64.tetrue.game.query.rotation.RotateResponse;
import io.github.bfur64.tetrue.game.utils.RotationHelper;
import io.github.bfur64.tetrue.game.utils.RotationPair;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BlockRotationSystem {
    private final World world;
    private final EventBus eventBus;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World and EventBus is intentionally shared between systems."
    )
    public BlockRotationSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(RotateResponse.class, event -> {
            int entityId = event.entityId();

            if (event.isValid()) {
                Position oldPosition = world.get(entityId, Position.class);

                world.put(entityId, new Position(
                    oldPosition.x() + event.dx(),
                    oldPosition.y() + event.dy()
                ));

                world.put(entityId, new Rotation(event.direction()));
            }
        });

        eventBus.subscribe(Rotate180Response.class, event -> {
            int entityId = event.entityId();

            if (event.isValid()) {
                Position oldPosition = world.get(entityId, Position.class);

                world.put(entityId, new Position(
                    oldPosition.x() + event.dx(),
                    oldPosition.y() + event.dy()
                ));

                world.put(entityId, new Rotation(event.direction()));
            }
        });
    }

    public void rotateLeft(int entityId) {
        rotate(entityId, false);
    }

    public void rotateRight(int entityId) {
        rotate(entityId, true);
    }

    public void rotate180(int entityId) {
        eventBus.publish(new Rotate180Query(entityId));
    }

    private void rotate(int entityId, boolean rightTurn) {
        Rotation rotation = world.get(entityId, Rotation.class);

        Direction direction = rotation.direction();
        Direction newDirection = rightTurn ?
            RotationHelper.rotateRight(rotation.direction())
            : RotationHelper.rotateLeft(rotation.direction());

        RotationPair rotationPair = new RotationPair(direction, newDirection);

        eventBus.publish(new RotateQuery(entityId, rotationPair));
    }
}
