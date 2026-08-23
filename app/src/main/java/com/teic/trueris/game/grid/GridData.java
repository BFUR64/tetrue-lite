package com.teic.trueris.game.grid;

import com.teic.trueris.Config;
import com.teic.trueris.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class GridData implements  GridReader, GridWriter{
    private final @Nullable CellType[] grid;
    private final int width;

    public GridData() {
        width = Config.gridWidth.get();

        grid = new CellType[Config.gridHeight.get() * width];
    }

    @Override
    public @Nullable CellType getCell(int x, int y) {
        return grid[y * width + x];
    }

    @Override
    public void setCell(int x, int y, @Nullable CellType cellType) {
        grid[y * width + x] = cellType;
    }
}
