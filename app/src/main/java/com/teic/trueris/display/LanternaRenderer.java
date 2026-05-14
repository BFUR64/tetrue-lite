package com.teic.trueris.display;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class LanternaRenderer implements Renderer {
    private final Terminal terminal;

    public LanternaRenderer() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        terminal.setCursorVisible(false);

        terminal.enterPrivateMode();
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    @Override
    public void clearScreen() {
        try {
            terminal.clearScreen();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putString(int x, int y, String out) {
        try {
            terminal.setCursorPosition(x, y);
            terminal.putString(out);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flush() {
        try {
            terminal.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        try {
            terminal.putString(String.format("\u001b[38;2;%s;%s;%sm", r, g, b));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        try {
            terminal.putString(String.format("\u001b[48;2;%s;%s;%sm", r, g, b));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetColorAndStyle() {
        try {
            terminal.resetColorAndSGR();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        terminal.exitPrivateMode();
        terminal.close();
    }
}
