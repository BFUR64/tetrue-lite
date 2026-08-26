package com.teic.trueris.game.system.movement;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.block.Direction;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;
import com.teic.trueris.game.query.rotation.MoveRotationQuery;
import com.teic.trueris.game.query.rotation.MoveRotationResponse;
import com.teic.trueris.game.utils.Offset;
import com.teic.trueris.game.utils.RotationHelper;
import com.teic.trueris.game.utils.RotationPair;
import com.teic.trueris.game.utils.SrsData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

import java.util.List;

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
        rotate(entityId, false);
    }

    public void rotateRight(Integer entityId) {
        rotate(entityId, true);
    }

    private void rotate(Integer entityId, boolean rightTurn) {
        if (world.has(entityId, Position.class, Rotation.class, Shape.class)) {
            Rotation rotation = world.get(entityId, Rotation.class);
            Shape shape = world.get(entityId, Shape.class);

            Direction direction = rotation.direction();
            Direction newDirection = rightTurn ?
                RotationHelper.rotateRight(rotation.direction())
                : RotationHelper.rotateLeft(rotation.direction());

            RotationPair rotationPair = new RotationPair(direction, newDirection);

            int size = shape.blockTemplate().size();

            List<Offset> offsets;
            if (size <= 3) {
                offsets = SrsData.getThreeTable().get(rotationPair);
            }
            else {
                offsets = SrsData.getITable().get(rotationPair);
            }

            eventBus.publish(new MoveRotationQuery(entityId, newDirection, offsets));
        }
    }
}
