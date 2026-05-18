package com.teic.trueris;

import io.github.bfur64.menu.utils.Property;

import java.time.Duration;

public class Config {
    public static final String GAME_VERSION = "v2.1.0";

    public static final int BLOCK_OFFSET = 3;
    public static final int SPAWN_BUFFER = 2;
    public static final int GRID_HEIGHT = 20;
    public static final int GRID_WIDTH = 10;
    public static final int TARGET_FPS = 60;

    public static Duration GRAVITY_DEF = Duration.ofMillis(500);
    public static final Duration GRAVITY_MIN = Duration.ofMillis(40);
    private static final Duration GRAVITY_MAX = Duration.ofMillis(5000);

    private static Duration gravity = GRAVITY_DEF;

    public static void saveState() {
        GRAVITY_DEF = getGravity();
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
}
