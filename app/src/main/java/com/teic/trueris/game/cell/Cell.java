package com.teic.trueris.game.cell;

public class Cell {
    public final Color color;
    private boolean isCopy;

    Cell() {
        this.color = Color.DEFAULT;
    }

    Cell(Color color) {
        this.color = color;
    }

    private Cell(Cell original) {
        this.color = original.color;
        this.isCopy = true;
    }

    public Cell copy() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot copy a singleton empty cell");
        }

        if (isCopy) {
            throw new IllegalStateException("Cannot make a new copy from a copy");
        }

        return new Cell(this);
    }

    public boolean isEmpty() {
        return this == CellRegistry.EMPTY;
    }
}
