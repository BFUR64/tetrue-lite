package com.teic.trueris.game.grid;

import com.teic.trueris.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface GridWriter {
    void setCell(int x, int y, @Nullable CellType cellType);
}
