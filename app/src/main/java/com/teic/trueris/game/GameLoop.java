package com.teic.trueris.game;

import java.io.IOException;

import com.googlecode.lanterna.input.KeyStroke;
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

    private boolean running;
    private final int targetFps;
    private final int nsPerFrame;

    public GameLoop(Terminal terminal, Renderer renderer, GameManager gameManager) {
        this.terminal = terminal;
        this.renderer = renderer;
        this.gameManager = gameManager;

        this.targetFps = Config.TARGET_FPS;
        this.nsPerFrame = NSEC / targetFps;
    }

    public void run() throws IOException {
        terminal.clearScreen();

        renderer.renderBorder();
        renderer.updateScreen();

        terminal.flush();

        running = true;
        while (running) {
            long frameStart = System.nanoTime();
            
            handleGameState(terminal.pollInput());
            
            long delta = System.nanoTime() - frameStart;
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
                renderer.updateScreen();
            }

            case ArrowDown -> {
                gameManager.moveBlockDown();
                renderer.updateScreen();
            }

            case ArrowLeft -> {
                gameManager.moveBlockLeft();
                renderer.updateScreen();
            }

            case ArrowRight -> {
                gameManager.moveBlockRight();
                renderer.updateScreen();
            }

            case Home -> {
                gameManager.rotateBlockLeft();
                renderer.updateScreen();
            }

            case End -> {
                gameManager.rotateBlockRight();
                renderer.updateScreen();
            }

            default -> {
                return;
            }
        }
    }
}
