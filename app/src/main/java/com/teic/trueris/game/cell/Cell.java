package com.teic.trueris.game.cell;

@Deprecated
public record Cell(Color color) {
    public Cell() {
        this(Color.DEFAULT);
    }

    public boolean isEmpty() {
        return this == CellRegistry.EMPTY;
    }
}
