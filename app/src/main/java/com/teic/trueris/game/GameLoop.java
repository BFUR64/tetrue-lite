package com.teic.trueris.game;

import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

import com.teic.trueris.Config;
import com.teic.trueris.display.Renderer;
import com.teic.trueris.input.Input;
import com.teic.trueris.input.Key;

public class GameLoop {
    @SuppressWarnings("SpellCheckingInspection")
    private static final int NSEC = 1_000_000_000;

    private final Renderer renderer;
    private final Input input;

    private final GameRenderer gameRenderer;
    private final GameManager gameManager;
    private final GameState gameState;

    private boolean running;
    private final int nsPerFrame;

    public GameLoop(Renderer renderer, Input input, GameRenderer gameRenderer, GameManager gameManager) {
        this.renderer = renderer;
        this.input = input;

        this.gameRenderer = gameRenderer;
        this.gameManager = gameManager;
        this.gameState = gameManager;

        int targetFps = Config.TARGET_FPS;
        this.nsPerFrame = NSEC / targetFps;
    }

    public void run() throws IOException {
        renderer.clearScreen();

        long delta = 0;

        running = true;
        while (running) {
            long frameStart = System.nanoTime();

            update(delta);

            long deadline = frameStart + nsPerFrame;
            long now = System.nanoTime();

            while (now < deadline) {
                LockSupport.parkNanos(deadline - now);
                now = System.nanoTime();
            }

            delta = System.nanoTime() - frameStart;
        }

        handleGameOver();
    }

    private void update(long delta) throws IOException {
        handleGameState(input.pollInput());
        gameManager.update(delta);
        gameRenderer.update();

        if (gameState.isGameOver()) {
            running = false;
        }
    }

    private void handleGameState(Key key) {
        if (key == Key.UNKNOWN) {
            return;
        }

        if (key.matches(Key.ESCAPE)) {
            running = false;
        }
        else if (key.matches(Key.UP)) {
            gameManager.dropBlock();
        }
        else if (key.matches(Key.DOWN)) {
            gameManager.moveBlockDown();
        }
        else if (key.matches(Key.LEFT)) {
            gameManager.moveBlockLeft();
        }
        else if (key.matches(Key.RIGHT)) {
            gameManager.moveBlockRight();
        }
        else if (key.matches(Key.COUNTER_CLOCKWISE)) {
            gameManager.rotateBlockLeft();
        }
        else if (key.matches(Key.CLOCKWISE)) {
            gameManager.rotateBlockRight();
        }
    }

    private void handleGameOver() throws IOException {
        renderer.resetColorAndStyle();
        renderer.clearScreen();
        
        renderer.putString(2, 1, "Game Over!");

        renderer.putString(2, 3, "Score: " + gameState.getScore());

        renderer.putString(2, 5, "Press ESC to go back to Main Menu");

        renderer.flush();

        while (true) {
            Key key = input.readInput();
            if (key.matches(Key.ESCAPE)) {
                break;
            }
        }
    }
}
