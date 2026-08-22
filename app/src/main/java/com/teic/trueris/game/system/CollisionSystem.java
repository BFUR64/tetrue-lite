package com.teic.trueris.game.system;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;
import com.teic.trueris.game.event.MoveBlockAccepted;
import com.teic.trueris.game.event.MoveBlockCommand;
import com.teic.trueris.game.event.MoveBlockRejected;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.teic.trueris.game.utils.CellGrid.getCell;

@NullMarked
public class CollisionSystem {
    private final World world;
    private final EventBus eventBus;

    private final int height = Config.gridHeight.get();
    private final int width = Config.gridWidth.get();

    public CollisionSystem(World world, EventBus eventBus) {
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveBlockCommand.class, event -> {
            Integer entityId = event.entityId();

            if (world.has(entityId, Position.class, Rotation.class, Shape.class)) {
                Position position = world.get(entityId, Position.class);
                Rotation rotation = world.get(entityId, Rotation.class);
                Shape shape = world.get(entityId, Shape.class);

                List<@Nullable CellType> rotatedCells = RotationSystem.rotateBlockNTimes(rotation.direction().ordinal(), shape.blockTemplate());

                int size = shape.blockTemplate().size();

                for (int row = 0; row < size; row++) {
                    for (int col = 0; col < size; col++) {
                        CellType cell = getCell(rotatedCells, size, col, row);

                        if (cell == null) {
                            continue;
                        }

                        int gridRow = position.y() + event.dy() + row;
                        int gridCol = position.x() + event.dx() + col;

                        if (isOutOfBounds(gridRow, gridCol)) {
                            boolean grounded = event.dy() > 0;

                            eventBus.publish(new MoveBlockRejected(entityId, grounded));
                            return;
                        }
                    }
                }
            }

            eventBus.publish(new MoveBlockAccepted(event.entityId(), event.dx(), event.dy()));
        });
    }

    private boolean isOutOfBounds(int gridRow, int gridCol) {
        return (
            gridRow < 0 || gridRow >= height
            || gridCol < 0 || gridCol >= width
        );
    }
}
