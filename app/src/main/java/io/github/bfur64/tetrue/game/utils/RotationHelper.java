package io.github.bfur64.tetrue.game.utils;

import io.github.bfur64.tetrue.game.block.BlockTemplate;
import io.github.bfur64.tetrue.game.block.Direction;
import io.github.bfur64.tetrue.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.bfur64.tetrue.game.utils.CellGrid.getCell;
import static io.github.bfur64.tetrue.game.utils.CellGrid.setCell;

@NullMarked
public final class RotationHelper {
    public static List<@Nullable CellType> rotateBlockNTimes(int amount, BlockTemplate blockTemplate) {
        int size = blockTemplate.size();

        @Nullable CellType[] cells = blockTemplate.cells().toArray(new CellType[0]);

        for (int i = 0; i < amount; i++) {
            cells = rotateArrayRight(size, cells);
        }

        //noinspection Java9CollectionFactory
        return Collections.unmodifiableList(Arrays.asList(cells));
    }

    private static @Nullable CellType[] rotateArrayRight(int size, @Nullable CellType[] cells) {
        @Nullable CellType[] newCells = new CellType[size * size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                setCell(newCells, size, size - 1 - col, row, getCell(cells, size, row, col));
            }
        }

        return newCells;
    }

    public static Direction rotateLeft(Direction currentDirection) {
        return Direction.fromId(((currentDirection.ordinal() - 1) % 4 + 4) % 4);
    }

    public static Direction rotateRight(Direction currentDirection) {
        return Direction.fromId(((currentDirection.ordinal() + 1) % 4 + 4) % 4);
    }
}
