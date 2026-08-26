package com.teic.trueris.game.system.lifecycle;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;
import com.teic.trueris.game.event.BlockPlaceEvent;
import com.teic.trueris.game.query.position.DropDownResponse;
import com.teic.trueris.game.query.position.MoveDownResponse;
import com.teic.trueris.game.grid.GridWriter;
import com.teic.trueris.game.timer.LockTimerExpired;
import com.teic.trueris.game.utils.RotationHelper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.teic.trueris.game.utils.CellGrid.getCell;

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

            if (world.has(event.entityId(), Position.class, Rotation.class, Shape.class)) {
                placeBlock(event.entityId());
            }
        });

        eventBus.subscribe(MoveDownResponse.class, event -> {
            if (!Config.softDropLock.get()) {
                return;
            }

            if (!event.canDrop() && world.has(event.entityId(), Position.class, Rotation.class, Shape.class)) {
                placeBlock(event.entityId());
            }
        });

        eventBus.subscribe(LockTimerExpired.class, event -> placeBlock(event.entityId()));
    }

    private void placeBlock(Integer entityId) {
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
