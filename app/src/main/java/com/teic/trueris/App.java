package com.teic.trueris;

import com.teic.trueris.game.GameLoop;
import com.teic.trueris.game.GameManager;
import com.teic.trueris.game.GameRenderer;
import com.teic.trueris.game.grid.GridData;
import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.*;
import io.github.bfur64.menu.item.display.*;
import io.github.bfur64.menu.item.input.*;
import io.github.bfur64.terminal.BufferedTerminal;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalBackend;

import java.util.Arrays;
import java.util.List;

public class App {
    private final TerminalBackend terminal;

    public static void main(String[] args) {
        try (TerminalBackend terminal = BufferedTerminal.auto()) {
            terminal.start();

            App app = new App(terminal);
            app.newStart();
        }
        catch (Exception error) {
            System.out.println("Failed: " + error.getMessage() + Arrays.toString(error.getStackTrace()));
        }
    }

    public App(TerminalBackend terminal) {
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
            new ActionItem("[ Exit ]", true),
            new LineBreak(),
            new StaticText("  [TIP] Use the `UP` and `DOWN` keys to move"),
            new StaticText("  [TIP] Press `ENTER` to select an item"),
            new StaticText("  [TIP] Press `ESC` to close the menu")
        );

        MenuManager menu = new MenuManager(terminal, items);
        menu.start();
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
            new LineBreak(),
            new StaticText("<< About >>"),
            new LineBreak(),
            new StaticText("A simple Tetrue clone made by TEIC."),
            new LineBreak(),
            new StaticText("| Rendering | "),
            new LineBreak(),
            new StaticText("Abstraction Library: " + Terminal.getLibraryInfo()),
            new StaticText("Renderer: " + terminal.getTerminalInfo()),
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
        menu.start();
    }

    private void runOptions() {
        MenuManager menu = new MenuManager(terminal, List.of(
                new LineBreak(),
                new StaticText("<< Options >>"),
                new LineBreak(),
                new ActionItem("[ Game Options ]", this::runGameOptions),
                new LineBreak(),
                new ActionItem("[ Key Binds ]", this::runKeyBinds),
                new LineBreak(),
                new ActionItem("[ Advanced Options ]", this::runAdvancedOptions),
                new LineBreak(),
                new ActionItem("[ Save & Return ]", Config::saveState, true)
        ));

        menu.start();
    }

    private void runGameOptions() {
        MenuManager menu = new MenuManager(terminal, List.of(
                new LineBreak(),
                new StaticText("<< Game Options >>"),
                new LineBreak(),
                new InputItem<>("Gravity", ": ", Config.gravity, "ms"),
                new LineBreak(),
                new ActionItem("[ Return ]", Config::saveState, true)
        ));

        menu.start();
    }

    private void runKeyBinds() {
        MenuManager menu = new MenuManager(terminal, List.of(
                new LineBreak(),
                new StaticText("<< Key Binds >>"),
                new LineBreak(),
                new KeyInputItem("Hard Drop", Config.hardDropKey),
                new KeyInputItem("Soft Drop", Config.softDropKey),
                new KeyInputItem("Move Left", Config.moveLeftKey),
                new KeyInputItem("Move Right", Config.moveRightKey),
                new KeyInputItem("Rotate Left", Config.rotateLeftKey),
                new KeyInputItem("Rotate Right", Config.rotateRightKey),
                new KeyInputItem("Hold Block", Config.holdKey),
                new LineBreak(),
                new ActionItem("[ Return ]", Config::saveState, true)
        ));

        menu.start();
    }

    private void runAdvancedOptions() {
        MenuManager menu = new MenuManager(terminal, List.of(
                new LineBreak(),
                new StaticText("<< Advanced Options >>"),
                new LineBreak(),
                new ToggleItem("Mobile Controls", Config.mobileControls),
                new LineBreak(),
                new InputItem<>("Grid Height", ": ", Config.gridHeight, "Cells"),
                new InputItem<>("Grid Width", ": ", Config.gridWidth, "Cells"),
                new LineBreak(),
                new ToggleItem("Show Gravity", Config.showGravity),
                new ToggleItem("No SRS", Config.noSRS),
                new LineBreak(),
                new ActionItem("[ Return ]", Config::saveState, true)
        ));

        menu.start();
    }
}
