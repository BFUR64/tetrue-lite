package com.teic.trueris;

import io.github.bfur64.menu.utils.Property;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;

public class Config {
    public static final String GAME_VERSION = "v2.4.0";

    public static final int TARGET_FPS = 60;

    // milliseconds
    public static int gravityDef = 500;
    public static final int GRAVITY_MIN = 40;
    private static final int GRAVITY_MAX = 5000;

    private static final int GRID_HEIGHT_MIN = 20;
    private static final int GRID_HEIGHT_MAX = 100;

    private static final int GRID_WIDTH_MIN = 10;
    private static final int GRID_WIDTH_MAX = 100;

    public static void saveState() {
        gravityDef = gravity.get();
    }

    public static Property<Integer> gravity = Property.of(gravityDef)
            .require(threshold -> threshold >= GRAVITY_MIN, "Time should be more than " + GRAVITY_MIN + " ms")
            .require(threshold -> threshold <= GRAVITY_MAX, "Time should be less than " + GRAVITY_MAX +  " ms")
            .parser(Integer::parseInt).build();

    public static Property<Integer> gridHeight = Property.of(GRID_HEIGHT_MIN)
            .require(value -> value >= GRID_HEIGHT_MIN, "Height must be at least " + GRID_HEIGHT_MIN + " cells")
            .require(value -> value <= GRID_HEIGHT_MAX, "...? Why?")
            .parser(Integer::parseInt).build();

    public static Property<Integer> gridWidth = Property.of(GRID_WIDTH_MIN)
            .require(value -> value >= GRID_WIDTH_MIN, "Width must be at least " + GRID_WIDTH_MIN + " cells")
            .require(value -> value <= GRID_WIDTH_MAX, "...? Why?")
            .parser(Integer::parseInt).build();

    // =====================
    // Gameplay Buttons
    // =====================
    public static Property<KeyStroke> hardDropKey = Property.of(new KeyStroke(KeyType.ARROW_UP)).build();

    public static Property<KeyStroke> softDropKey = Property.of(new KeyStroke(KeyType.ARROW_DOWN)).build();

    public static Property<KeyStroke> moveLeftKey = Property.of(new KeyStroke(KeyType.ARROW_LEFT)).build();

    public static Property<KeyStroke> moveRightKey = Property.of(new KeyStroke(KeyType.ARROW_RIGHT)).build();

    public static Property<KeyStroke> rotateLeftKey = Property.of(new KeyStroke('q')).build();

    public static Property<KeyStroke> rotateRightKey = Property.of(new KeyStroke('e')).build();

    public static Property<KeyStroke> holdKey = Property.of(new KeyStroke('c')).build();
}
