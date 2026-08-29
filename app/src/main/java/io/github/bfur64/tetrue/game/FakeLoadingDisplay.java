package io.github.bfur64.tetrue.game;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.bfur64.terminal.Terminal;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.ThreadLocalRandom;

@NullMarked
public class FakeLoadingDisplay {
    private static final String SOLID = "█";
    private static final String GHOST = "░";

    private final Terminal terminal;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "Terminal is intentionally shared between systems."
    )
    public FakeLoadingDisplay(Terminal terminal) {
        this.terminal = terminal;
    }

    public void startFakeLoadingDisplay() {
        try {
            terminal.clear();
            terminal.put(2, 1, "FLD™️ Loading...");
            terminal.put(2, 3, "0");

            for (int i = 0; i < 10; i++) {
                terminal.put(i + 4, 3, GHOST);
            }

            terminal.put(15, 3, "100%");
            terminal.flush();

            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 50));

            terminal.put(4, 3, SOLID);
            terminal.flush();

            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 150));

            terminal.put(5, 3, SOLID);
            terminal.flush();

            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 150));

            terminal.put(6, 3, SOLID);
            terminal.flush();

            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 300));

            terminal.put(7, 3, SOLID);
            terminal.put(8, 3, SOLID);
            terminal.put(9, 3, SOLID);
            terminal.flush();

            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 50));

            terminal.put(10, 3, SOLID);
            terminal.put(11, 3, SOLID);
            terminal.flush();

            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 50));

            terminal.put(12, 3, SOLID);
            terminal.put(13, 3, SOLID);
            terminal.flush();

            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
