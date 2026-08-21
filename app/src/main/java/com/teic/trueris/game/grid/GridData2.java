package com.teic.trueris.game.grid;

import com.teic.trueris.Config;
import com.teic.trueris.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class GridData2 {
    private final @Nullable CellType[] grid;
    private final int width;

    public GridData2() {
        width = Config.gridWidth.get();

        grid = new CellType[Config.gridHeight.get() * width];
    }

    public @Nullable CellType getCell(int x, int y) {
        return grid[y * width + x];
    }

    public void setCell(int x, int y, @Nullable CellType cellType) {
        grid[y * width + x] = cellType;
    }
}
