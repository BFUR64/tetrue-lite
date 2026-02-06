
package com.teic.trueris;

import java.io.IOException;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.teic.trueris.game.GameLoop;
import com.teic.trueris.game.GameManager;
import com.teic.trueris.game.Renderer;
import com.teic.trueris.game.grid.GridData;

public class App {
    private final Terminal terminal;
    private final TextGraphics textGraphics;

    public static void main(String[] args) {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            terminal.setCursorVisible(false);

            TextGraphics textGraphics = terminal.newTextGraphics();

            terminal.enterPrivateMode();

            App app = new App(terminal, textGraphics);
            app.startMenu();

            terminal.exitPrivateMode();
        }
        catch (Exception error) {
            Logging.writeStackTrace(LogType.ERROR, error);
        }
    }

    private App(Terminal terminal, TextGraphics textGraphics) {
        this.terminal = terminal;
        this.textGraphics = textGraphics;
    }

    private void startMenu() throws IOException {
        while (true) {
            terminal.resetColorAndSGR();
            terminal.clearScreen();

            textGraphics.putString(2, 1, "Tetrue Lite");
            textGraphics.putString(2, 3, "1. New Game");
            textGraphics.putString(2, 4, "2. About");
            textGraphics.putString(2, 5, "0. Exit");
            textGraphics.putString(2, 7, "Press the keys 1, 2, 0 to navigate.");

            KeyStroke keyStroke = terminal.readInput();

            if (keyStroke.getKeyType() != KeyType.Character) {
                continue;
            }

            char input = keyStroke.getCharacter();

            if (input == '1') {
                GridData gridData = new GridData();
                Renderer renderer = new Renderer(textGraphics, gridData);
                GameManager gameManager = new GameManager(gridData);

                GameLoop gameLoop = new GameLoop(
                    terminal, renderer, gameManager
                );

                gameLoop.run();
            }
            else if (input == '2') {
                terminal.resetColorAndSGR();
                terminal.clearScreen();

                textGraphics.putString(2, 1, "About");
                textGraphics.putString(2, 3, "Simple Tetrue clone by TEIC.");
                textGraphics.putString(2, 5, "Press any key to continue...");
                terminal.readInput();

            }
            else if (input == '0') {
                break;
            }
        }
    }
}
