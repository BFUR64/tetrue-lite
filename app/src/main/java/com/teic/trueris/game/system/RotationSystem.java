package com.teic.trueris.game.system;

import com.teic.trueris.game.block.BlockTemplate;
import com.teic.trueris.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NullMarked
public class RotationSystem {
    public List<@Nullable CellType> rotateBlockNTimes(int amount, BlockTemplate blockTemplate) {
        int size = blockTemplate.size();

        @Nullable CellType[] cells = blockTemplate.cells().toArray(new CellType[0]);

        for (int i = 0; i < amount; i++) {
            cells = rotateArrayRight(size, cells);
        }

        //noinspection Java9CollectionFactory
        return Collections.unmodifiableList(Arrays.asList(cells));
    }

    private @Nullable CellType[] rotateArrayRight(int size, @Nullable CellType[] cells) {
        @Nullable CellType[] newCells = new CellType[size * size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                setCell(newCells, size, size - 1 - col, row, getCell(cells, size, row, col));
            }
        }

        return newCells;
    }

    public static @Nullable CellType getCell(@Nullable CellType[] cells, int width, int x, int y) {
        return cells[y * width + x];
    }

    public static void setCell(@Nullable CellType[] cells, int width, int x, int y, @Nullable CellType cellType) {
        cells[y * width + x] = cellType;
    }
}
