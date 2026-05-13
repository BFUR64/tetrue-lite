package com.teic.trueris.display;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;

import java.io.IOException;
import java.io.PrintWriter;

public class JLine3Renderer implements Renderer {
    private final Terminal terminal;
    private final PrintWriter printWriter;

    public JLine3Renderer() throws IOException {
        terminal = TerminalBuilder.builder()
                .dumb(false)
                .build();

        terminal.enterRawMode();
        terminal.puts(Capability.cursor_invisible);

        printWriter = terminal.writer();
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    @Override
    public void clearScreen() {
        terminal.puts(Capability.clear_screen);
    }

    @Override
    public void putString(int x, int y, String out) {
        terminal.puts(Capability.cursor_address, y, x);
        printWriter.print(out);
    }

    @Override
    public void flush() {
        printWriter.flush();
    }

    @Override
    public void close() throws IOException {
        terminal.puts(Capability.cursor_visible);
        terminal.close();
    }
}
