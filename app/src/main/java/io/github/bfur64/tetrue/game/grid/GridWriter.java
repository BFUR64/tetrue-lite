package io.github.bfur64.tetrue.game.grid;

import io.github.bfur64.tetrue.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface GridWriter {
    void setCell(int x, int y, @Nullable CellType cellType);
}
