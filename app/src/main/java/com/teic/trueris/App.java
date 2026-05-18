package com.teic.trueris;

import com.teic.trueris.game.GameLoop;
import com.teic.trueris.game.GameManager;
import com.teic.trueris.game.GameRenderer;
import com.teic.trueris.game.grid.GridData;
import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.*;
import io.github.bfur64.terminal.Terminal;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class App {
    private final Terminal terminal;

    public static void main(String[] args) {
        try (Terminal terminal = Terminal.auto()) {
            App app = new App(terminal);
            app.newStart();
        }
        catch (IOException error) {
            System.out.println("Failed: " + error.getMessage() + Arrays.toString(error.getStackTrace()));
        }
    }

    public App(Terminal terminal) {
        this.terminal = terminal;
    }

    private void newStart() {
        List<Item> items = List.of(
            new LineBreak(),
            new StaticText("<< Tetrue Lite " + Config.GAME_VERSION + " >>"),
            new LineBreak(),
            new ActionItem("[ New Game ]", this::runNewGame),
            new ActionItem("[ Options ] ", this::runOptions),
            new ActionItem("[ About ]", this::runAbout),
            new ActionItem("[ Exit ]", () -> {}, true),
            new LineBreak(),
            new StaticText("  [TIP] Use the `UP` and `DOWN` keys to move"),
            new StaticText("  [TIP] Press `ENTER` to select an item"),
            new StaticText("  [TIP] Press `ESC` to close the menu")
        );

        MenuManager menu = new MenuManager(terminal, items);
        menu.run();
    }

    private void runNewGame() {
        GridData gridData = new GridData();
        GameManager gameManager = new GameManager(gridData);
        GameRenderer gameRenderer = new GameRenderer(terminal, gridData, gameManager);

        GameLoop gameLoop = new GameLoop(terminal, gameRenderer, gameManager);
        gameLoop.run();
    }

    private void runAbout() {
        List<String> terminalInfo = terminal.getTerminalInfo();

        List<Item> items = List.of(
            new LineBreak(),
            new StaticText("<< About >>"),
            new LineBreak(),
            new StaticText("A simple Tetrue clone made by TEIC."),
            new LineBreak(),
            new StaticText("| Rendering | "),
            new LineBreak(),
            new StaticText("Meta Library: " + terminalInfo.getFirst()),
            new StaticText(terminalInfo.get(1)),
            new StaticText(terminalInfo.getLast()),
            new LineBreak(),
            new StaticText("Current Renderer: " + terminal.getCurrentTerminal()),
            new LineBreak(),
            new DynamicText<>("Column: ", terminal::getXSize),
            new DynamicText<>("Row: ", terminal::getYSize),
            new LineBreak(),
            new StaticText("| Menu |"),
            new LineBreak(),
            new StaticText("Menu Manager: " + MenuManager.getVersion()),
            new LineBreak(),
            new ActionItem("[ Refresh ]", false),
            new ActionItem("[ Return ]", true)
        );

        MenuManager menu = new MenuManager(terminal, items);
        menu.run();
    }

    private void runOptions() {
        MenuManager menu = new MenuManager(terminal, List.of(
                new LineBreak(),
                new StaticText("<< Options >>"),
                new LineBreak(),
                new StaticText("| Game Options |"),
                new LineBreak(),
                new EditableItem<>("Gravity", ": ", Config.getGravityProperty(), "ms"),
                new LineBreak(),
                new EditableItem<>("Grid Height", ": ", Config.getGridHeightProperty(), "Cells"),
                new EditableItem<>("Grid Width", ": ", Config.getGridWidthProperty(), "Cells"),
                new LineBreak(),
                new ActionItem("[ Save & Return ]", Config::saveState, true)
        ));

        menu.run();
    }
}
