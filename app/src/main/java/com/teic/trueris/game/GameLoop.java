package com.teic.trueris.game;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

import com.teic.trueris.Config;
import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.ActionItem;
import io.github.bfur64.menu.item.BreakItem;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.TextItem;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;

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

    public void run() {
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

    private void update(long delta) {
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

    private void handleGameOver() {
        List<Item> items = List.of(
            new BreakItem(),
            new TextItem("Game Over!"),
            new BreakItem(),
            new TextItem("Score: " + gameState.getScore()),
            new BreakItem(),
            new ActionItem("[ Return ] ", () -> {}, true)
        );

        MenuManager menu = new MenuManager(terminal, items);
        menu.run();
    }
}
