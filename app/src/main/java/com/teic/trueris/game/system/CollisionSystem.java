package com.teic.trueris.game.system;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.IsGhost;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;
import com.teic.trueris.game.event.position.GhostPositionQuery;
import com.teic.trueris.game.event.position.GhostPositionResponse;
import com.teic.trueris.game.event.position.*;
import com.teic.trueris.game.event.rotation.MoveRotationQuery;
import com.teic.trueris.game.event.rotation.MoveRotationResponse;
import com.teic.trueris.game.grid.GridReader;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.teic.trueris.game.utils.CellGrid.getCell;

@NullMarked
public class CollisionSystem {
    private final GridReader gridReader;
    private final World world;

    private final int height = Config.gridHeight.get();
    private final int width = Config.gridWidth.get();

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World and EventBus is intentionally shared between systems."
    )
    public CollisionSystem(GridReader gridReader, World world, EventBus eventBus) {
        this.gridReader = gridReader;
        this.world = world;

        eventBus.subscribe(MoveXQuery.class, event -> {
            Boolean valid = isValid(event.entityId(), event.dx(), 0);

            if (valid != null) {
                eventBus.publish(new MoveXResponse(event.entityId(), valid, event.dx()));
            }
        });

        eventBus.subscribe(MoveDownQuery.class, event -> {
            Boolean valid = isValid(event.entityId(), 0, 1);

            if (valid != null) {
                eventBus.publish(new MoveDownResponse(event.entityId(), valid));
            }
        });

        eventBus.subscribe(DropDownQuery.class, event -> {
            Boolean valid = isValid(event.entityId(), 0, 1);

            if (valid != null) {
                eventBus.publish(new DropDownResponse(event.entityId(), valid));
            }
        });

        eventBus.subscribe(GroundCheckQuery.class, event -> {
            Boolean clear = isValid(event.entityId(), 0, 1);

            if (clear != null) {
                eventBus.publish(new GroundCheckResponse(event.entityId(), clear));
            }
        });

        eventBus.subscribe(PositionValidQuery.class, event -> {
            Boolean valid = isValid(event.entityId(), 0, 0);

            if (valid != null) {
                eventBus.publish(new PositionValidResponse(event.entityId(), valid));
            }
        });

        eventBus.subscribe(MoveRotationQuery.class, event -> {
            Integer entityId = event.entityId();

            if (world.has(entityId, Position.class, Shape.class)) {
                Position position = world.get(entityId, Position.class);
                Shape shape = world.get(entityId, Shape.class);

                int direction = event.direction();
                int dx = event.dx();
                int dy = event.dy();

                boolean valid = isValid(position, direction, shape, dx, dy);

                eventBus.publish(new MoveRotationResponse(entityId, valid, direction, dx, dy));
            }
        });

        eventBus.subscribe(GhostPositionQuery.class, event -> {
            Integer entityId = event.entityId();

            if (world.has(entityId, Position.class, Rotation.class, Shape.class, IsGhost.class)) {
                Position position = world.get(entityId, Position.class);
                Rotation rotation = world.get(entityId, Rotation.class);
                Shape shape = world.get(entityId, Shape.class);

                int dy = 0;
                while (isValid(position, rotation, shape, 0, dy)) {
                    dy++;
                }
                dy--;

                eventBus.publish(new GhostPositionResponse(entityId, dy));
            }
        });
    }

    private @Nullable Boolean isValid(int entityId, int dx, int dy) {
        if (!world.has(entityId, Position.class, Rotation.class, Shape.class)) {
            return null;
        }

        Position position = world.get(entityId, Position.class);
        Rotation rotation = world.get(entityId, Rotation.class);
        Shape shape = world.get(entityId, Shape.class);

        return isValid(position, rotation, shape, dx, dy);
    }

    public boolean isValid(Position position, Rotation rotation, Shape shape, int dx, int dy) {
        return isValid(position, rotation.direction().ordinal(), shape, dx, dy);
    }

    public boolean isValid(Position position, int direction, Shape shape, int dx, int dy) {
        List<@Nullable CellType> rotatedCells = RotationSystem.rotateBlockNTimes(direction, shape.blockTemplate());

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
        return gridReader.getCell(gridCol, gridRow) != null;
    }
}
