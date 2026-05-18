package com.teic.trueris;

import io.github.bfur64.menu.utils.Property;

import java.time.Duration;

public class Config {
    public static final String GAME_VERSION = "v2.2.0";

    public static final int SPAWN_BUFFER = 2;
    public static final int TARGET_FPS = 60;

    public static Duration GravityDef = Duration.ofMillis(500);
    public static final Duration GRAVITY_MIN = Duration.ofMillis(40);
    private static final Duration GRAVITY_MAX = Duration.ofMillis(5000);

    private static final int GRID_HEIGHT_MIN = 20;
    private static final int GRID_HEIGHT_MAX = 100;
    private static int gridHeight = GRID_HEIGHT_MIN ;

    private static final int GRID_WIDTH_MIN = 10;
    private static final int GRID_WIDTH_MAX = 100;
    private static int gridWidth = GRID_WIDTH_MIN;

    private static Duration gravity = GravityDef;

    public static void saveState() {
        GravityDef = getGravity();
    }

    public static Property<Long> getGravityProperty() {
        return Property.create(
            () -> gravity.toMillis(),
            ms -> gravity = Duration.ofMillis(ms),
            Long::parseLong
        )
        .withValidator(threshold -> threshold >= GRAVITY_MIN.toMillis(), "Time should be more than " + GRAVITY_MIN.toMillis() + " ms")
        .withValidator(threshold -> threshold <= GRAVITY_MAX.toMillis(), "Time should be less than " + GRAVITY_MAX.toSeconds() +  " s");
    }

    public static Duration getGravity() {
        return gravity;
    }

    public static void setGravity(Duration gravity) {
        Config.gravity = gravity;
    }

    public static Property<Integer> getGridHeightProperty() {
        return Property.create(
            Config::getGridHeight,
            Config::setGridHeight,
            Integer::parseInt
        )
        .withValidator(value -> value >= GRID_HEIGHT_MIN, "Height must be at least " + GRID_HEIGHT_MIN + " cells")
        .withValidator(value -> value <= GRID_HEIGHT_MAX, "...? Why?");
    }

    public static int getGridHeight() {
        return gridHeight;
    }

    private static void setGridHeight(int gridHeight) {
        Config.gridHeight = gridHeight;
    }

    public static Property<Integer> getGridWidthProperty() {
        return Property.create(
            Config::getGridWidth,
            Config::setGridWidth,
            Integer::parseInt
        )
        .withValidator(value -> value >= GRID_WIDTH_MIN, "Width must be at least " + GRID_WIDTH_MIN + " cells")
        .withValidator(value -> value <= GRID_WIDTH_MAX, "...? Why?");
    }

    public static int getGridWidth() {
        return gridWidth;
    }

    private static void setGridWidth(int gridWidth) {
        Config.gridWidth = gridWidth;
    }
}
