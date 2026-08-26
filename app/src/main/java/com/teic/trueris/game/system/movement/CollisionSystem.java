package com.teic.trueris.game.system.movement;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.block.Direction;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;
import com.teic.trueris.game.grid.GridReader;
import com.teic.trueris.game.query.position.*;
import com.teic.trueris.game.query.rotation.Rotate180Query;
import com.teic.trueris.game.query.rotation.Rotate180Response;
import com.teic.trueris.game.query.rotation.RotateQuery;
import com.teic.trueris.game.query.rotation.RotateResponse;
import com.teic.trueris.game.utils.Offset;
import com.teic.trueris.game.utils.RotationHelper;
import com.teic.trueris.game.utils.RotationPair;
import com.teic.trueris.game.utils.SrsData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.teic.trueris.game.utils.CellGrid.getCell;

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
            boolean valid = isValid(event.entityId(), event.dx(), 0);
            eventBus.publish(new MoveXResponse(event.entityId(), valid, event.dx()));
        });

        eventBus.subscribe(MoveDownQuery.class, event -> {
            boolean valid = isValid(event.entityId(), 0, 1);
            eventBus.publish(new MoveDownResponse(event.entityId(), valid));
        });

        eventBus.subscribe(DropDownQuery.class, event -> {
            int dy = 0;

            while (isValid(event.entityId(), 0, dy)) {
                dy++;
            }
            dy--;

            eventBus.publish(new DropDownResponse(event.entityId(), dy));
        });

        eventBus.subscribe(GroundCheckQuery.class, event -> {
            boolean clear = isValid(event.entityId(), 0, 1);
            eventBus.publish(new GroundCheckResponse(event.entityId(), clear));
        });

        eventBus.subscribe(PositionValidQuery.class, event -> {
            boolean valid = isValid(event.entityId(), 0, 0);
            eventBus.publish(new PositionValidResponse(event.entityId(), valid));
        });

        eventBus.subscribe(RotateQuery.class, event -> {
            InternalRotateResponse internalRotateResponse = rotationQuery(event.entityId(), event.rotationPair(), 0, 0);
            eventBus.publish(new RotateResponse(
                event.entityId(),
                internalRotateResponse.isValid,
                event.rotationPair().second(),
                internalRotateResponse.dx,
                internalRotateResponse.dy
            ));
        });

        eventBus.subscribe(Rotate180Query.class, event -> {
            boolean isValid = false;
            Direction direction = Direction.UP;
            int dx = 0;
            int dy = 0;

            if (world.has(event.entityId(), Rotation.class)) {
                Rotation rotation = world.get(event.entityId(), Rotation.class);
                direction = rotation.direction();
                Direction ninetyDirection = RotationHelper.rotateRight(direction);
                RotationPair rotationPair = new RotationPair(direction, ninetyDirection);

                InternalRotateResponse first = rotationQuery(event.entityId(), rotationPair, 0, 0);
                if (first.isValid) {
                    dx = first.dx;
                    dy = first.dy;

                    Direction oneEightyDirection = RotationHelper.rotateRight(ninetyDirection);
                    rotationPair = new RotationPair(ninetyDirection, oneEightyDirection);

                    InternalRotateResponse second = rotationQuery(event.entityId(), rotationPair, dx, dy);
                    isValid = second.isValid;
                    direction = oneEightyDirection;
                    dx += second.dx;
                    dy += second.dy;
                }
            }

            eventBus.publish(new Rotate180Response(event.entityId(), isValid, direction, dx, dy));
        });
    }

    private InternalRotateResponse rotationQuery(Integer entityId, RotationPair rotationPair, int baseDx, int baseDy) {
        if (!world.has(entityId, Shape.class)) {
            return new InternalRotateResponse(false, 0, 0);
        }

        Shape shape = world.get(entityId, Shape.class);
        int size = shape.blockTemplate().size();

        List<Offset> offsets;
        if (size <= 3) {
            offsets = SrsData.getThreeTable().get(rotationPair);
        }
        else {
            offsets = SrsData.getITable().get(rotationPair);
        }

        int dx = 0;
        int dy = 0;
        boolean isValid = false;

        for (int offsetIndex = 0; offsetIndex < offsets.size() && !isValid; offsetIndex++) {
            dx = offsets.get(offsetIndex).x();
            dy = offsets.get(offsetIndex).y();

            isValid = isValid(entityId, shape, rotationPair.second().ordinal(), baseDx + dx, baseDy + dy);
        }

        return new InternalRotateResponse(isValid, dx, dy);
    }

    private boolean isValid(int entityId, Shape shape, int direction, int dx, int dy) {
        if (!world.has(entityId, Position.class)) {
            return false;
        }

        Position position = world.get(entityId, Position.class);

        return isValid(position, direction, shape, dx, dy);
    }

    private boolean isValid(int entityId, int dx, int dy) {
        if (!world.has(entityId, Position.class, Rotation.class, Shape.class)) {
            return false;
        }

        Position position = world.get(entityId, Position.class);
        Rotation rotation = world.get(entityId, Rotation.class);
        Shape shape = world.get(entityId, Shape.class);

        return isValid(position, rotation.direction().ordinal(), shape, dx, dy);
    }

    public boolean isValid(Position position, int direction, Shape shape, int dx, int dy) {
        List<@Nullable CellType> rotatedCells = RotationHelper.rotateBlockNTimes(direction, shape.blockTemplate());

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

    @NullMarked
    private record InternalRotateResponse(boolean isValid, int dx, int dy) {}
}
