package com.teic.trueris;

import com.teic.trueris.game.GameLoop;
import com.teic.trueris.game.GameManager;
import com.teic.trueris.game.GameRenderer;
import com.teic.trueris.game.grid.GridData;
import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.ActionItem;
import io.github.bfur64.menu.item.BreakItem;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.TextItem;
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
            new BreakItem(),
            new TextItem("<< Tetrue Lite v2.0.3 >>"),
            new BreakItem(),
            new ActionItem("[ New Game ]", this::runNewGame),
            new ActionItem("[ About ]", this::runAbout),
            new ActionItem("[ Exit ]", () -> {}, true),
            new BreakItem(),
            new TextItem("  [TIP] Use the `UP` and `DOWN` keys to move"),
            new TextItem("  [TIP] Press `ENTER` to select an item"),
            new TextItem("  [TIP] Press `ESC` to close the menu")
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
        List<Item> items = List.of(
            new BreakItem(),
            new TextItem("<< About >>"),
            new BreakItem(),
            new TextItem("A simple Tetrue clone made by TEIC."),
            new BreakItem(),
            new TextItem("Renderer: " + terminal.getTerminalInfo()),
            new TextItem("Columns: " + terminal.getXSize() + " | Rows: " + terminal.getYSize()),
            new BreakItem(),
            new TextItem("Menu Manager: " + MenuManager.getVersion()),
            new BreakItem(),
            new ActionItem("[ Return ]", () -> {}, true)
        );

        MenuManager menu = new MenuManager(terminal, items);
        menu.run();
    }
}
