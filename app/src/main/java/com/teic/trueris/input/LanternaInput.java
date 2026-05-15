package com.teic.trueris.input;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import org.jspecify.annotations.NonNull;

import java.io.IOException;

public class LanternaInput implements Input {
    private final Terminal terminal;

    public LanternaInput(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public @NonNull Key readInput() {
        try {
            return getKey(terminal.readInput());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NonNull Key pollInput() {
        try {
            KeyStroke keyStroke = terminal.pollInput();

            if (keyStroke == null) {
                return Key.UNKNOWN;
            }

            return getKey(keyStroke);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Key getKey(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character) {
            char input = keyStroke.getCharacter();

            if (input == 'q') {
                return Key.COUNTER_CLOCKWISE;
            } else if (input == 'e') {
                return Key.CLOCKWISE;
            }

            Key key = Key.fromCharacter(input);

            if (key == null) return Key.UNKNOWN;

            return key;
        }
        else {
            return switch (keyStroke.getKeyType()) {
                case Escape -> Key.ESCAPE;

                case ArrowUp -> Key.UP;

                case ArrowDown -> Key.DOWN;

                case ArrowLeft -> Key.LEFT;

                case ArrowRight -> Key.RIGHT;

                default -> Key.UNKNOWN;
            };
        }
    }

    @Override
    public void close() throws IOException {

    }
}
