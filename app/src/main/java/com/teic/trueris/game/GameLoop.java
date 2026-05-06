package com.teic.trueris.game;

import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import com.teic.trueris.Config;

public class GameLoop {
    @SuppressWarnings("SpellCheckingInspection")
    private static final int NSEC = 1_000_000_000;

    private final Terminal terminal;
    private final Renderer renderer;
    private final GameManager gameManager;
    private final GameState gameState;

    private boolean running;
    private final int nsPerFrame;

    public GameLoop(Terminal terminal, Renderer renderer, GameManager gameManager) {
        this.terminal = terminal;
        this.renderer = renderer;
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
        renderer.update();

        if (gameState.isGameOver()) {
            running = false;
        }
    }

    private void handleGameState(KeyStroke key) {
        if (key == null) {
            return;
        }

        switch (key.getKeyType()) {
            case Escape -> running = false;

            case ArrowUp -> gameManager.dropBlock();

            case ArrowDown -> gameManager.moveBlockDown();

            case ArrowLeft -> gameManager.moveBlockLeft();

            case ArrowRight -> gameManager.moveBlockRight();

            case Home -> gameManager.rotateBlockLeft();

            case End -> gameManager.rotateBlockRight();

            default -> {}
        }
    }

    private void handleGameOver() throws IOException {
        terminal.resetColorAndSGR();
        terminal.clearScreen();
        
        terminal.setCursorPosition(2, 1);
        terminal.putString("Game Over!");

        terminal.setCursorPosition(2, 3);
        terminal.putString("Score: " + gameState.getScore());

        terminal.setCursorPosition(2, 5);
        terminal.putString("Press ESC to go back to Main Menu");

        while (true) {
            KeyStroke keyStroke = terminal.readInput();
            if (keyStroke.getKeyType() == KeyType.Escape) {
                break;
            }
        }
    }
}
