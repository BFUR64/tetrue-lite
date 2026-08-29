package io.github.bfur64.tetrue;

import io.github.bfur64.menu.Event;
import io.github.bfur64.tetrue.game.GameLoop;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.*;
import io.github.bfur64.menu.item.display.DynamicText;
import io.github.bfur64.menu.item.display.LineBreak;
import io.github.bfur64.menu.item.display.StaticText;
import io.github.bfur64.menu.item.input.InputItem;
import io.github.bfur64.menu.item.input.KeyInputItem;
import io.github.bfur64.menu.item.input.ToggleItem;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@NullMarked
public class App {
    private final Terminal terminal;

    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);

        try (TerminalRuntime runtime = createRuntime(argsList)) {
            Terminal terminal = runtime.terminal();
            App app = new App(terminal);
            app.start();
        }
        catch (Exception error) {
            System.err.println("Terminal initialization failed: " + error.getMessage() + Arrays.toString(error.getStackTrace()));
            System.exit(1);
        }
    }

    private static TerminalRuntime createRuntime(List<String> args) throws IOException {
        Terminal.Builder builder = Terminal.builder();

        if (args.contains("-jline")) {
            builder.jline();
        } else if (args.contains("-lanterna")) {
            builder.lanterna();
        }

        return builder.build();
    }

    @SuppressFBWarnings(
        value = "EI2",
        justification = "Terminal is intentionally shared between systems."
    )
    public App(Terminal terminal) {
        this.terminal = terminal;
    }

    private void start() {
        List<Item> items = List.of(
            new LineBreak(),
            new StaticText("<< Tetrue Lite " + Config.GAME_VERSION + " >>"),
            new LineBreak(),
            new ActionItem("[ New Game ]", this::runNewGame),
            new ListItem("[ Options ] ", this::runOptions),
            new ListItem("[ Credits ]", this::runCredits),
            new ListItem("[ About ]", this::runAbout),
            new ActionItem("[ Exit ]", true),
            new LineBreak(),
            new StaticText("  [TIP] Use the `UP` and `DOWN` keys to move"),
            new StaticText("  [TIP] Press `ENTER` to select an item"),
            new StaticText("  [TIP] Press `ESC` to close the menu")
        );

        MenuManager menu = new MenuManager(terminal, items);
        Event event = menu.getEvent();
        MenuSound.registerSounds(event);

        menu.start();
    }

    private void runNewGame() {
        GameLoop gameLoop = new GameLoop(terminal);
        gameLoop.run();
    }

    private List<Item> runCredits() {
        return List.of(
            new LineBreak(),
            new StaticText("<< Credits >>"),
            new LineBreak(),
            new StaticText(" -- SFX --"),
            new LineBreak(),
            new StaticText("\"Block Lock\": Pixel Explosion - Lumaro_Studios"),
            new StaticText("\"Line Clear\": Pixel Jump - Lumaro_Studios"),
            new StaticText("\"Button Click\": UI Sound 134 - Film & Special Effects"),
            new StaticText("\"Cursor Change\": Click - Film & Special Effects"),
            new LineBreak(),
            new StaticText("Background Music: Pixel Song #12 - freesound_community"),
            new LineBreak(),
            new LineBreak(),
            new StaticText(" -- Sources --"),
            new LineBreak(),
            new StaticText("* https://pixabay.com"),
            new LineBreak(),
            new ActionItem("[ Return ]", true)
        );
    }

    private List<Item> runAbout() {
        return List.of(
            new LineBreak(),
            new StaticText("<< About >>"),
            new LineBreak(),
            new StaticText("A simple Tetrue clone made by TEIC."),
            new LineBreak(),
            new LineBreak(),
            new StaticText(" -- Rendering -- "),
            new LineBreak(),
            new StaticText("Abstraction Library: " + terminal.libraryInfo()),
            new StaticText("Renderer: " + terminal.terminalInfo()),
            new LineBreak(),
            new DynamicText<>("Column: ", terminal::xSize),
            new DynamicText<>("Row: ", terminal::ySize),
            new LineBreak(),
            new LineBreak(),
            new StaticText(" -- Menu --"),
            new LineBreak(),
            new StaticText("Menu Manager: " + MenuManager.getVersion()),
            new LineBreak(),
            new ActionItem("[ Return ]", true)
        );
    }

    private List<Item> runOptions() {
        return List.of(
            new LineBreak(),
            new StaticText("<< Options >>"),
            new LineBreak(),
            new ListItem("[ Game Options ]", this::runGameOptions),
            new LineBreak(),
            new ListItem("[ Key Binds ]", this::runKeyBinds),
            new LineBreak(),
            new ListItem("[ Advanced Options ]", this::runAdvancedOptions),
            new LineBreak(),
            new ActionItem("[ Save & Return ]", true)
        );
    }

    private List<Item> runGameOptions() {
        return List.of(
            new LineBreak(),
            new StaticText("<< Game Options >>"),
            new LineBreak(),
            new InputItem<>("Target FPS", ": ", Config.targetFps),
            new LineBreak(),
            new LineBreak(),
            new StaticText(" -- Gravity --"),
            new LineBreak(),
            new ToggleItem("Gravity Enabled", Config.gravityEnabled),
            new InputItem<>("Starting Gravity", ": ", Config.gravityMs, "ms"),
            new InputItem<>("Speed Step", ": ", Config.speedStep, "ms"),
            new LineBreak(),
            new LineBreak(),
            new StaticText(" -- Lock Delay --"),
            new LineBreak(),
            new ToggleItem("Lock Delay Enabled", Config.lockTimerEnabled),
            new InputItem<>("Lock Delay", ": ", Config.lockDelay, "ms"),
            new LineBreak(),
            new LineBreak(),
            new StaticText(" -- Instant Lock Behavior -- "),
            new LineBreak(),
            new ToggleItem("Hard Drop", Config.hardDropLock),
            new ToggleItem("Soft Drop", Config.softDropLock),
            new LineBreak(),
            new LineBreak(),
            new ToggleItem("Sound Enabled", Config.soundEnabled),
            new LineBreak(),
            new ActionItem("[ Return ]", true)
        );
    }

    private List<Item> runKeyBinds() {
        return List.of(
            new LineBreak(),
            new StaticText("<< Key Binds >>"),
            new LineBreak(),
            new ToggleItem("Mobile Controls", Config.mobileControls),
            new LineBreak(),
            new KeyInputItem("Hard Drop", Config.hardDropKey),
            new KeyInputItem("Soft Drop", Config.softDropKey),
            new KeyInputItem("Move Left", Config.moveLeftKey),
            new KeyInputItem("Move Right", Config.moveRightKey),
            new KeyInputItem("Rotate Left", Config.rotateLeftKey),
            new KeyInputItem("Rotate Right", Config.rotateRightKey),
            new KeyInputItem("Rotate 180", Config.rotate180Key),
            new KeyInputItem("Hold Block", Config.holdKey),
            new LineBreak(),
            new ActionItem("[ Return ]", true)
        );
    }

    private List<Item> runAdvancedOptions() {
        return List.of(
            new LineBreak(),
            new StaticText("<< Advanced Options >>"),
            new LineBreak(),
            new InputItem<>("Grid Height", ": ", Config.gridHeight, "Cells"),
            new InputItem<>("Grid Width", ": ", Config.gridWidth, "Cells"),
            new LineBreak(),
            new ToggleItem("Show Debug", Config.showDebug),
            new LineBreak(),
            new ActionItem("[ Return ]", true)
        );
    }
}
