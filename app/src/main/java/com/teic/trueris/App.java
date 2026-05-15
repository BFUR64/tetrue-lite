package com.teic.trueris;

import com.teic.trueris.game.GameLoop;
import com.teic.trueris.game.GameManager;
import com.teic.trueris.game.GameRenderer;
import com.teic.trueris.game.grid.GridData;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;

import java.io.IOException;

public class App {
    private final Terminal terminal;

    public static void main(String[] args) {
        try (Terminal terminal = Terminal.build()) {
            App app = new App(terminal);
            app.start();
        }
        catch (IOException error) {
            System.out.println("Failed!");
        }
    }

    public App(Terminal terminal) {
        this.terminal = terminal;
    }

    private void start() throws IOException {
        while (true) {
            terminal.clearScreen();

            terminal.putString(2, 1, "Tetrue Lite v2.0.2");
            terminal.putString(2, 3, "1. New Game");
            terminal.putString(2, 4, "2. About");
            terminal.putString(2, 5, "0. Exit");
            terminal.putString(2, 7, "Press the keys 1, 2, 0 to navigate.");

            terminal.flush();

            KeyStroke key = terminal.readInput();

            if (key.getKeyType() == KeyType.ESCAPE) {
                break;
            }

            if (key.getKeyType() != KeyType.CHARACTER) continue;

            if (key.getCharacter() == '1') {
                GridData gridData = new GridData();
                GameManager gameManager = new GameManager(gridData);
                GameRenderer gameRenderer = new GameRenderer(terminal, gridData, gameManager);

                GameLoop gameLoop = new GameLoop(terminal, gameRenderer, gameManager);

                gameLoop.run();
            }
            else if (key.getCharacter() == '2') {
                terminal.clearScreen();

                terminal.putString(2, 1, "About");
                terminal.putString(2, 3, "Simple Tetrue clone by TEIC.");
                terminal.putString(2, 5, "Renderer: " + terminal.getTerminalInfo());
                terminal.putString(2, 6, "X: " + terminal.getXSize() + " | Y: " + terminal.getYSize());
                terminal.putString(2, 8, "Press any key to continue...");
                terminal.flush();

                terminal.readInput();
            }
            else if (key.getCharacter() == '0') {
                break;
            }
        }

        terminal.clearScreen();
        terminal.flush();
    }
}
