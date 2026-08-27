package io.github.bfur64.tetrue.game.grid;

import io.github.bfur64.tetrue.Config;
import io.github.bfur64.tetrue.game.cell.CellType;
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
