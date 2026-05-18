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

    private static int gridHeight = 20;
    private static int gridWidth = 10;

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
        .withValidator(threshold -> threshold >= GRAVITY_MIN.toMillis(), "Time should be more than 40 ms")
        .withValidator(threshold -> threshold <= GRAVITY_MAX.toMillis(), "Time should be less than 5 s");
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
        );
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
        );
    }

    public static int getGridWidth() {
        return gridWidth;
    }

    private static void setGridWidth(int gridWidth) {
        Config.gridWidth = gridWidth;
    }
}
