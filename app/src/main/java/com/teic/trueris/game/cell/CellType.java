package com.teic.trueris.game.cell;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum CellType {
    O(Color.YELLOW),
    J(Color.BLUE),
    L(Color.ORANGE),
    S(Color.GREEN),
    Z(Color.RED),
    T(Color.PURPLE),
    I(Color.CYAN);

    private final Color color;

    CellType(Color color) {
        this.color = color;
    }

    public Color color() {
        return color;
    }
}
