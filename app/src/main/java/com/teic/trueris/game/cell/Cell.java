package com.teic.trueris.game.cell;

public record Cell(Color color) {
    public Cell() {
        this(Color.DEFAULT);
    }

    public boolean isEmpty() {
        return this == CellRegistry.EMPTY;
    }
}
