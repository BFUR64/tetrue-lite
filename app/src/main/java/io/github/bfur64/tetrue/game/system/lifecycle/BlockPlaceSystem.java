package io.github.bfur64.tetrue.game.system.lifecycle;

import io.github.bfur64.tetrue.Config;
import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.World;
import io.github.bfur64.tetrue.game.cell.CellType;
import io.github.bfur64.tetrue.game.component.Position;
import io.github.bfur64.tetrue.game.component.Rotation;
import io.github.bfur64.tetrue.game.component.Shape;
import io.github.bfur64.tetrue.game.event.BlockPlaceEvent;
import io.github.bfur64.tetrue.game.query.position.DropDownResponse;
import io.github.bfur64.tetrue.game.query.position.MoveDownResponse;
import io.github.bfur64.tetrue.game.grid.GridWriter;
import io.github.bfur64.tetrue.game.timer.LockTimerExpired;
import io.github.bfur64.tetrue.game.utils.RotationHelper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static io.github.bfur64.tetrue.game.utils.CellGrid.getCell;

@NullMarked
public class BlockPlaceSystem {
    private final GridWriter gridWriter;
    private final World world;
    private final EventBus eventBus;

    @SuppressFBWarnings(
            value = "EI2",
            justification = "GridWriter, World, and EventBus is intentionally shared between systems."
    )
    public BlockPlaceSystem(GridWriter gridWriter, World world, EventBus eventBus) {
        this.gridWriter = gridWriter;
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(DropDownResponse.class, event -> {
            if (!Config.hardDropLock.get()) {
                return;
            }

            placeBlock(event.entityId());
        });

        eventBus.subscribe(MoveDownResponse.class, event -> {
            if (!Config.softDropLock.get()) {
                return;
            }

            placeBlock(event.entityId());
        });

        eventBus.subscribe(LockTimerExpired.class, event -> placeBlock(event.entityId()));
    }

    private void placeBlock(int entityId) {
        Position position = world.get(entityId, Position.class);
        Rotation rotation = world.get(entityId, Rotation.class);
        Shape shape = world.get(entityId, Shape.class);

        List<@Nullable CellType> rotatedCells = RotationHelper.rotateBlockNTimes(rotation.direction().ordinal(), shape.blockTemplate());

        int size = shape.blockTemplate().size();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                CellType cell = getCell(rotatedCells, size, col, row);

                if (cell == null) {
                    continue;
                }

                gridWriter.setCell(position.x() + col, position.y() + row, cell);
            }
        }

        eventBus.publish(new BlockPlaceEvent(entityId));
    }
}
