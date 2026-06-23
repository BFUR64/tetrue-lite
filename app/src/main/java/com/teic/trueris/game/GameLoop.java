package com.teic.trueris.game;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

import com.teic.trueris.Config;
import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.ActionItem;
import io.github.bfur64.menu.item.display.LineBreak;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.display.StaticText;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;

public class GameLoop {
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
        terminal.clear();

        long delta = 0;

        running = true;
        while (running) {
            long frameStart = System.nanoTime();

            update(delta);

            long deadline = frameStart + nsPerFrame;
            long now = System.nanoTime();

            long remaining = (deadline - now) / 2;
            if (remaining > 1_000_000) {
                LockSupport.parkNanos(deadline - now);
            }

            while (now < deadline) {
                Thread.onSpinWait();
                now = System.nanoTime();
            }

            delta = System.nanoTime() - frameStart;
        }

        handleGameOver();
    }

    private void update(long delta) {
        handleGameState(terminal.poll());
        gameManager.update(delta);
        gameRenderer.update(delta);

        if (gameState.isGameOver()) {
            running = false;
        }
    }

    private void handleGameState(KeyStroke keyStroke) {
        if (keyStroke == null) {
            return;
        }

        if (keyStroke.keyType() == KeyType.ESCAPE) {
            running = false;
            return;
        }

        if (keyStroke.equals(Config.hardDropKey.get())) {
            gameManager.dropBlock();
        }
        else if (keyStroke.equals(Config.softDropKey.get())) {
            gameManager.moveBlockDown();
        }
        else if (keyStroke.equals(Config.moveLeftKey.get())) {
            gameManager.moveBlockLeft();
        }
        else if (keyStroke.equals(Config.moveRightKey.get())) {
            gameManager.moveBlockRight();
        }
        else if (keyStroke.equals(Config.rotateLeftKey.get())) {
            gameManager.rotateBlockLeft();
        }
        else if (keyStroke.equals(Config.rotateRightKey.get())) {
            gameManager.rotateBlockRight();
        }
        else if (keyStroke.equals(Config.holdKey.get())) {
            gameManager.holdBlock();
        }
    }

    private void handleGameOver() {
        List<Item> items = List.of(
            new LineBreak(),
            new StaticText("Game Over!"),
            new LineBreak(),
            new StaticText("Score: " + gameState.getScore()),
            new LineBreak(),
            new ActionItem("[ Return ] ", true)
        );

        MenuManager menu = new MenuManager(terminal, items);
        menu.start();
    }
}
