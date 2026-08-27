package io.github.bfur64.tetrue.game.block;

import io.github.bfur64.tetrue.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@NullMarked
public record BlockTemplate(int size, List<@Nullable CellType> cells, CellType cellType) {
    public BlockTemplate {
        cells = Collections.unmodifiableList(cells);
    }
}
