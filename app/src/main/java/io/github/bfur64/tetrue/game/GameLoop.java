package io.github.bfur64.tetrue.game;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

import io.github.bfur64.menu.Event;
import io.github.bfur64.tetrue.Config;
import io.github.bfur64.tetrue.MenuSound;
import io.github.bfur64.tetrue.game.event.GameOverEvent;
import io.github.bfur64.tetrue.game.event.ScoreChangeEvent;
import io.github.bfur64.tetrue.game.grid.GridData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.ActionItem;
import io.github.bfur64.menu.item.display.LineBreak;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.display.StaticText;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class GameLoop {
    private static final int NANOSECOND = 1_000_000_000;

    private static final boolean WINDOWS =
            System.getProperty("os.name").toLowerCase().contains("win");

    private static final long PARK_THRESHOLD =
            WINDOWS ? 17_000_000L : 2_000_000L;

    private static final long PARK_MARGIN =
            WINDOWS ? 2_000_000L : 500_000L;

    private final Terminal terminal;

    private int score;

    private int oneClear;
    private int twoClear;
    private int threeClear;
    private int fourClear;

    private final EventBus eventBus;
    private final GameRenderer gameRenderer;
    private final GameManager gameManager;

    private boolean running;
    private final int nsPerFrame;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "Terminal is intentionally shared between systems."
    )
    public GameLoop(Terminal terminal) {
        this.terminal = terminal;

        World world = new World();
        eventBus = new EventBus();
        GridData gridData = new GridData();

        gameRenderer = new GameRenderer(terminal, world, gridData, eventBus);
        gameManager = new GameManager(world, eventBus, gridData);

        int targetFps = Config.targetFps.get();
        this.nsPerFrame = NANOSECOND / targetFps;

        eventBus.subscribe(GameOverEvent.class, event -> running = false);
        eventBus.subscribe(ScoreChangeEvent.class, event -> {
            score = event.score();

            oneClear = event.oneClear();
            twoClear = event.twoClear();
            threeClear = event.threeClear();
            fourClear = event.fourClear();
        });
    }

    public void run() {
        terminal.clear();

        long delta = 0;

        running = true;
        while (running) {
            long frameStart = System.nanoTime();

            update(delta / (double) NANOSECOND);

            long deadline = frameStart + nsPerFrame;

            while (true) {
                long now = System.nanoTime();
                long remaining = deadline - now;

                if (remaining <= 0) {
                    break;
                }

                if (remaining > PARK_THRESHOLD) {
                    LockSupport.parkNanos(remaining - PARK_MARGIN);
                }
                else {
                    Thread.onSpinWait();
                }
            }

            delta = System.nanoTime() - frameStart;
        }

        handleGameOver();
    }

    private void update(double delta) {
        handleGameState(terminal.poll());
        gameManager.update(delta);
        gameRenderer.update(delta);
    }

    private void handleGameState(@Nullable KeyStroke keyStroke) {
        if (keyStroke == null) {
            return;
        }

        if (keyStroke.keyType() == KeyType.ESCAPE) {
            eventBus.publish(new GameOverEvent());
            return;
        }

        if (keyStroke.equals(Config.softDropKey.get())) {
            gameManager.moveBlockDown();
        }
        else if (keyStroke.equals(Config.hardDropKey.get())) {
            gameManager.dropBlock();
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
        else if (keyStroke.equals(Config.rotate180Key.get())) {
            gameManager.rotate180();
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
            new StaticText("Score: " + score),
            new LineBreak(),
            new StaticText(" -- Statistics (Cleared Lines) --"),
            new LineBreak(),
            new StaticText("One   : " + oneClear),
            new StaticText("Two   : " + twoClear),
            new StaticText("Three : " + threeClear),
            new StaticText("Four  : " + fourClear),
            new LineBreak(),
            new ActionItem("[ Return ] ", true)
        );

        MenuManager menu = new MenuManager(terminal, items);
        Event event = menu.getEvent();
        MenuSound.registerSounds(event);

        menu.start();
    }
}
