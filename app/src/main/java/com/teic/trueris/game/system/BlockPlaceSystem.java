package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;
import com.teic.trueris.game.event.BlockPlaceEvent;
import com.teic.trueris.game.event.MoveDownResponse;
import com.teic.trueris.game.grid.GridData2;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.teic.trueris.game.utils.CellGrid.getCell;

@NullMarked
public class BlockPlaceSystem {
    public BlockPlaceSystem(GridData2 gridData, World world, EventBus eventBus) {
        eventBus.subscribe(MoveDownResponse.class, event -> {
            if (!event.canDrop() && world.has(event.entityId(), Position.class, Rotation.class, Shape.class, OnGround.class)) {
                if (!world.get(event.entityId(), OnGround.class).onGround()) return;

                Position position = world.get(event.entityId(), Position.class);
                Rotation rotation = world.get(event.entityId(), Rotation.class);
                Shape shape = world.get(event.entityId(), Shape.class);

                List<@Nullable CellType> rotatedCells = RotationSystem.rotateBlockNTimes(rotation.direction().ordinal(), shape.blockTemplate());

                int size = shape.blockTemplate().size();

                for (int row = 0; row < size; row++) {
                    for (int col = 0; col < size; col++) {
                        CellType cell = getCell(rotatedCells, size, col, row);

                        if (cell == null) {
                            continue;
                        }

                        gridData.setCell(position.x() + col, position.y() + row, cell);
                    }
                }

                eventBus.publish(new BlockPlaceEvent(event.entityId()));
            }
        });
    }
}
