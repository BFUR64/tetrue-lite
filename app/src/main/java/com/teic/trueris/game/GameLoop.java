package com.teic.trueris.game;

import java.io.IOException;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import com.teic.trueris.Config;
import com.teic.trueris.LogType;
import com.teic.trueris.Logging;

public class GameLoop {
    private static final int NSEC = 1_000_000_000;
    private static final int MSEC = 1_000_000;
    private static final int SLEEP_THRESHOLD = MSEC * 2;

    private final Terminal terminal;
    private final Renderer renderer;
    private final GameManager gameManager;
    private final GameState gameState;

    private boolean running;
    private final int targetFps;
    private final int nsPerFrame;

    public GameLoop(Terminal terminal, Renderer renderer, GameManager gameManager) {
        this.terminal = terminal;
        this.renderer = renderer;
        this.gameManager = gameManager;
        this.gameState = gameManager;

        this.targetFps = Config.TARGET_FPS;
        this.nsPerFrame = NSEC / targetFps;
    }

    public void run() throws IOException {
        terminal.clearScreen();

        renderer.update();

        terminal.flush();

        terminal.readInput();

        long delta = 0;;

        running = true;
        while (running) {
            long frameStart = System.nanoTime();

            handleGameState(terminal.pollInput());
            gameManager.update(delta);

            renderer.update();

            if (gameState.isGameOver()) {
                running = false;
            }

            delta = System.nanoTime() - frameStart;
            long remaining = nsPerFrame - delta;

            if (remaining >= SLEEP_THRESHOLD) {
                try {
                    Thread.sleep(remaining / MSEC);
                }
                catch (InterruptedException e) {
                    Logging.writeStackTrace(LogType.ERROR, e);
                }
            }

            while (System.nanoTime() - frameStart < nsPerFrame) {}
        }

        handleGameOver();
    }

    private void handleGameState(KeyStroke key) {
        if (key == null) {
            return;
        }

        switch (key.getKeyType()) {
            case Escape -> {
                running = false;
            }

            case ArrowUp -> {
                gameManager.dropBlock();
            }

            case ArrowDown -> {
                gameManager.moveBlockDown();
            }

            case ArrowLeft -> {
                gameManager.moveBlockLeft();
            }

            case ArrowRight -> {
                gameManager.moveBlockRight();
            }

            case Home -> {
                gameManager.rotateBlockLeft();
            }

            case End -> {
                gameManager.rotateBlockRight();
            }

            default -> {
                return;
            }
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
