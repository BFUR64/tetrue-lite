
package com.teic.trueris;

import java.io.IOException;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

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
        terminal.resetColorAndSGR();
        terminal.clearScreen();

        textGraphics.putString(0, 0, "Hello World!");

        while (true) {
            terminal.flush();
            break;
        }

        terminal.readInput();
    }
}
