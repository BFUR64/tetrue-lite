package com.teic.trueris.game.cell;

public class Cell {
    public final Color color;

    Cell() {
        this.color = Color.DEFAULT;
    }

    Cell(Color color) {
        this.color = color;
    }

    public boolean isEmpty() {
        return this == CellRegistry.EMPTY;
    }
}
