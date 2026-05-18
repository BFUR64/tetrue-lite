package com.teic.trueris;

import io.github.bfur64.menu.utils.Property;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;

import java.time.Duration;

public class Config {
    public static final String GAME_VERSION = "v2.3.0";

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

    private static KeyStroke hardDrop = new KeyStroke(KeyType.ARROW_UP);
    private static KeyStroke moveDown = new KeyStroke(KeyType.ARROW_DOWN);
    private static KeyStroke moveLeft = new KeyStroke(KeyType.ARROW_LEFT);
    private static KeyStroke moveRight = new KeyStroke(KeyType.ARROW_RIGHT);
    private static KeyStroke rotateLeft = new KeyStroke('q');
    private static KeyStroke rotateRight = new KeyStroke('e');

    private static Duration gravity = GravityDef;

    public static void saveState() {
        GravityDef = getGravity();
    }

    public static Property<Long> getGravityProperty() {
        return Property.create(
            () -> gravity.toMillis(),
            ms -> gravity = Duration.ofMillis(ms)
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
        return Property.create(Config::getGridHeight, Config::setGridHeight)
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
        return Property.create(Config::getGridWidth, Config::setGridWidth)
        .withValidator(value -> value >= GRID_WIDTH_MIN, "Width must be at least " + GRID_WIDTH_MIN + " cells")
        .withValidator(value -> value <= GRID_WIDTH_MAX, "...? Why?");
    }

    public static int getGridWidth() {
        return gridWidth;
    }

    private static void setGridWidth(int gridWidth) {
        Config.gridWidth = gridWidth;
    }

    // =====================
    // Gameplay Buttons
    // =====================
    public static Property<KeyStroke> getHardDropProperty() {
        return Property.create(Config::getHardDrop, Config::setHardDrop);
    }

    public static KeyStroke getHardDrop() {
        return hardDrop;
    }

    private static void setHardDrop(KeyStroke hardDrop) {
        Config.hardDrop = hardDrop;
    }



    public static Property<KeyStroke> getMoveDownProperty() {
        return Property.create(Config::getMoveDown, Config::setMoveDown);
    }

    public static KeyStroke getMoveDown() {
        return moveDown;
    }

    private static void setMoveDown(KeyStroke moveDown) {
        Config.moveDown = moveDown;
    }



    public static Property<KeyStroke> getMoveLeftProperty() {
        return Property.create(Config::getMoveLeft, Config::setMoveLeft);
    }

    public static KeyStroke getMoveLeft() {
        return moveLeft;
    }

    private static void setMoveLeft(KeyStroke moveLeft) {
        Config.moveLeft = moveLeft;
    }



    public static Property<KeyStroke> getMoveRightProperty() {
        return Property.create(Config::getMoveRight, Config::setMoveRight);
    }

    public static KeyStroke getMoveRight() {
        return moveRight;
    }

    private static void setMoveRight(KeyStroke moveRight) {
        Config.moveRight = moveRight;
    }



    public static Property<KeyStroke> getRotateLeftProperty() {
        return Property.create(Config::getRotateLeft, Config::setRotateLeft);
    }

    public static KeyStroke getRotateLeft() {
        return rotateLeft;
    }

    private static void setRotateLeft(KeyStroke rotateLeft) {
        Config.rotateLeft = rotateLeft;
    }



    public static Property<KeyStroke> getRotateRightProperty() {
        return Property.create(Config::getRotateRight, Config::setRotateRight);
    }

    public static KeyStroke getRotateRight() {
        return rotateRight;
    }

    private static void setRotateRight(KeyStroke rotateRight) {
        Config.rotateRight = rotateRight;
    }
}
