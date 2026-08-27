package io.github.bfur64.tetrue.game.grid;

import io.github.bfur64.tetrue.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface GridReader {
    @Nullable CellType getCell(int x, int y);
}
