package com.teic.trueris.game.block;

import java.util.Map;

public enum Direction {
    UP(0), RIGHT(1), DOWN(2), LEFT(3);

    private final int id;

    Direction(int id) {
        this.id = id;
    }

    private static final Map<Integer, Direction> BY_ID =  Map.of(UP.id, UP, RIGHT.id, RIGHT, DOWN.id, DOWN, LEFT.id, LEFT);

    public static Direction fromId(int id) {
        return BY_ID.get(id);
    }
}
