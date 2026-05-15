package com.teic.trueris.game;

import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

import com.teic.trueris.Config;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;

public class GameLoop {
    @SuppressWarnings("SpellCheckingInspection")
    private static final int NSEC = 1_000_000_000;

    private final Terminal terminal;
    
    private final GameRenderer gameRenderer;
    private final GameManager gameManager;
    private final GameState gameState;

    private boolean running;
    private final int nsPerFrame;

    public GameLoop(Terminal terminal, GameRenderer gameRenderer, GameManager gameManager) {
        this.terminal = terminal;

        this.gameRenderer = gameRenderer;
        this.gameManager = gameManager;
        this.gameState = gameManager;

        int targetFps = Config.TARGET_FPS;
        this.nsPerFrame = NSEC / targetFps;
    }

    public void run() throws IOException {
        terminal.clearScreen();

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
        handleGameState(terminal.pollInput());
        gameManager.update(delta);
        gameRenderer.update();

        if (gameState.isGameOver()) {
            running = false;
        }
    }

    private void handleGameState(KeyStroke keyStroke) {
        if (keyStroke == null) {
            return;
        }

        switch (keyStroke.getKeyType()) {
            case CHARACTER -> {
                switch (keyStroke.getCharacter()) {
                    case 'q' -> gameManager.rotateBlockLeft();
                    case 'e' -> gameManager.rotateBlockRight();
                }
            }
            case ESCAPE -> running = false;
            case ARROW_UP -> gameManager.dropBlock();
            case ARROW_DOWN -> gameManager.moveBlockDown();
            case ARROW_LEFT -> gameManager.moveBlockLeft();
            case ARROW_RIGHT -> gameManager.moveBlockRight();
        }
    }

    private void handleGameOver() throws IOException {
        terminal.resetColorAndStyle();
        terminal.clearScreen();
        
        terminal.putString(2, 1, "Game Over!");

        terminal.putString(2, 3, "Score: " + gameState.getScore());

        terminal.putString(2, 5, "Press ESC to go back to Main Menu");

        terminal.flush();

        while (true) {
            KeyStroke key = terminal.readInput();
            if (key.getKeyType() == KeyType.ESCAPE) {
                break;
            }
        }
    }
}
