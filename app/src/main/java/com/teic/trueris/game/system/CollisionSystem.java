package com.teic.trueris.game.system;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;
import com.teic.trueris.game.event.*;
import com.teic.trueris.game.grid.GridData2;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.teic.trueris.game.utils.CellGrid.getCell;

@NullMarked
public class CollisionSystem {
    private final GridData2 gridData;
    private final World world;
    private final EventBus eventBus;

    private final int height = Config.gridHeight.get();
    private final int width = Config.gridWidth.get();

    public CollisionSystem(GridData2 gridData, World world, EventBus eventBus) {
        this.gridData = gridData;
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveBlockCommand.class, event -> {
            Integer entityId = event.entityId();

            if (world.has(entityId, Position.class, Rotation.class, Shape.class)) {
                Position position = world.get(entityId, Position.class);
                Rotation rotation = world.get(entityId, Rotation.class);
                Shape shape = world.get(entityId, Shape.class);

                boolean grounded = event.dy() > 0;

                if (!isValid(position, rotation, shape, event.dx(), event.dy())) {
                    eventBus.publish(new MoveBlockRejected(entityId, grounded));
                    return;
                }

                eventBus.publish(new MoveBlockAccepted(event.entityId(), event.dx(), event.dy()));
            }
        });

        eventBus.subscribe(MoveBlockQuery.class, event -> {
            Integer entityId = event.entityId();

            if (world.has(entityId, Position.class, Rotation.class, Shape.class)) {
                Position position = world.get(entityId, Position.class);
                Rotation rotation = world.get(entityId, Rotation.class);
                Shape shape = world.get(entityId, Shape.class);

                eventBus.publish(new MoveBlockQueryResponse(entityId, isValid(position, rotation, shape, event.dx(), event.dy())));
            }
        });
    }

    public boolean isValid(Position position, Rotation rotation, Shape shape, int dx, int dy) {
        List<@Nullable CellType> rotatedCells = RotationSystem.rotateBlockNTimes(rotation.direction().ordinal(), shape.blockTemplate());

        int size = shape.blockTemplate().size();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                CellType cell = getCell(rotatedCells, size, col, row);

                if (cell == null) {
                    continue;
                }

                int gridRow = position.y() + dy + row;
                int gridCol = position.x() + dx + col;

                if (
                    isOutOfBounds(gridRow, gridCol)
                    || isColliding(gridRow, gridCol)
                ) {

                    return false;
                }
            }
        }

        return true;
    }

    private boolean isOutOfBounds(int gridRow, int gridCol) {
        return (
            gridRow < 0 || gridRow >= height
            || gridCol < 0 || gridCol >= width
        );
    }

    private boolean isColliding(int gridRow, int gridCol) {
        return gridData.getCell(gridCol, gridRow) != null;
    }
}
