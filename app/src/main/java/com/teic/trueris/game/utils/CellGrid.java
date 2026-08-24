package com.teic.trueris.game.utils;

import com.teic.trueris.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public class CellGrid {
    public static @Nullable CellType getCell(@Nullable CellType[] cells, int width, int x, int y) {
        return cells[y * width + x];
    }

    public static @Nullable CellType getCell(List<@Nullable CellType> cells, int width, int x, int y) {
        return cells.get(y * width + x);
    }

    public static void setCell(@Nullable CellType[] cells, int width, int x, int y, @Nullable CellType cellType) {
        cells[y * width + x] = cellType;
    }
}
