package io.github.bfur64.tetrue;

import io.github.bfur64.menu.Property;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Config {
    public static final String GAME_VERSION = "v3.3.1";

    // milliseconds
    private static final int gravityDef = 500;
    public static final int GRAVITY_MIN = 40;
    private static final int GRAVITY_MAX = 5000;

    private static final int GRID_HEIGHT_MIN = 20;
    private static final int GRID_HEIGHT_MAX = 100;

    private static final int GRID_WIDTH_MIN = 4;
    private static final int GRID_WIDTH_MIN_DEF = 10;
    private static final int GRID_WIDTH_MAX = 100;

    public static final Property<Integer> gravityMs = Property.of(gravityDef)
        .require(threshold -> threshold >= GRAVITY_MIN, "Time should be more than " + GRAVITY_MIN + " ms")
        .require(threshold -> threshold <= GRAVITY_MAX, "Time should be less than " + GRAVITY_MAX +  " ms")
        .parser(Integer::parseInt).build();

    public static final Property<Integer> gridHeight = Property.of(GRID_HEIGHT_MIN)
        .require(value -> value >= GRID_HEIGHT_MIN, "Height must be at least " + GRID_HEIGHT_MIN + " cells")
        .require(value -> value <= GRID_HEIGHT_MAX, "...? Why?")
        .parser(Integer::parseInt).build();

    public static final Property<Integer> gridWidth = Property.of(GRID_WIDTH_MIN_DEF)
        .require(value -> value >= GRID_WIDTH_MIN, "Width must be at least " + GRID_WIDTH_MIN + " cells")
        .require(value -> value <= GRID_WIDTH_MAX, "...? Why?")
        .parser(Integer::parseInt).build();

    // =====================
    // Gameplay Buttons
    // =====================
    public static final Property<KeyStroke> hardDropKey = Property.of(new KeyStroke(' ')).build();

    public static final Property<KeyStroke> softDropKey = Property.of(new KeyStroke(KeyType.ARROW_DOWN)).build();

    public static final Property<KeyStroke> moveLeftKey = Property.of(new KeyStroke(KeyType.ARROW_LEFT)).build();

    public static final Property<KeyStroke> moveRightKey = Property.of(new KeyStroke(KeyType.ARROW_RIGHT)).build();

    public static final Property<KeyStroke> rotateLeftKey = Property.of(new KeyStroke('z')).build();

    public static final Property<KeyStroke> rotateRightKey = Property.of(new KeyStroke('x')).build();

    public static final Property<KeyStroke> rotate180Key = Property.of(new KeyStroke('a')).build();

    public static final Property<KeyStroke> holdKey = Property.of(new KeyStroke('c')).build();

    // =====================
    // Game Flags
    // =====================
    public static final Property<Boolean> showDebug = Property.of(false).build();
    public static final Property<Boolean> gravityEnabled = Property.of(true).build();
    public static final Property<Boolean> lockTimerEnabled = Property.of(true).build();
    public static final Property<Boolean> hardDropLock = Property.of(true).build();
    public static final Property<Boolean> softDropLock = Property.of(false).build();
    public static final Property<Boolean> soundEnabled = Property.of(true).build();

    // =====================
    // Game Modifiers
    // =====================
    public static final Property<Integer> targetFps = Property.of(60)
        .require(value -> value >= 15, "Minimum of 15 FPS")
        .require(value -> value <= 1000, "Maximum of 1000 FPS")
        .parser(Integer::parseInt).build();

    public static final Property<Integer> lockDelay = Property.of(500)
        .require(value -> value >= 0, "Minimum of 0ms lock delay")
        .require(value -> value <= 5000, "Maximum of 5000ms lock delay")
        .parser(Integer::parseInt).build();

    public static final Property<Integer> speedStep = Property.of(40)
        .require(value -> value >= 0, "Minimum of 0ms speed step")
        .require(value -> value <= 500, "Maximum of 500ms speed step")
        .parser(Integer::parseInt).build();

    // =====================
    // Control Switching
    // =====================
    private static boolean isMobileMode;

    private static void switchControls(boolean bool) {
        isMobileMode = bool;

        if (isMobileMode) {
            mobileControls();
            return;
        }

        desktopControls();
    }

    private static boolean isMobileMode() {
        return isMobileMode;
    }

    public static final Property<Boolean> mobileControls = Property.of(false)
        .setter(Config::switchControls)
        .getter(Config::isMobileMode)
        .build();

    private static void mobileControls() {
        hardDropKey.set(new KeyStroke(' '));
        softDropKey.set(new KeyStroke(KeyType.ARROW_DOWN));
        moveLeftKey.set(new KeyStroke(KeyType.ARROW_LEFT));
        moveRightKey.set(new KeyStroke(KeyType.ARROW_RIGHT));
        rotateLeftKey.set(new KeyStroke(KeyType.HOME));
        rotateRightKey.set(new KeyStroke(KeyType.END));
        rotate180Key.set(new KeyStroke(KeyType.PAGE_UP));
        holdKey.set(new KeyStroke('-'));
    }

    private static void desktopControls() {
        hardDropKey.set(new KeyStroke(KeyType.ARROW_UP));
        softDropKey.set(new KeyStroke(KeyType.ARROW_DOWN));
        moveLeftKey.set(new KeyStroke(KeyType.ARROW_LEFT));
        moveRightKey.set(new KeyStroke(KeyType.ARROW_RIGHT));
        rotateLeftKey.set(new KeyStroke('z'));
        rotateRightKey.set(new KeyStroke('x'));
        rotate180Key.set(new KeyStroke('a'));
        holdKey.set(new KeyStroke('c'));
    }
}
