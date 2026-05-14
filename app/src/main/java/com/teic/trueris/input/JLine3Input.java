package com.teic.trueris.input;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class JLine3Input implements Input {
    private final Terminal terminal;
    private final BindingReader bindingReader;
    private final KeyMap<Key> keyMap;

    private final BlockingQueue<Key> inputQueue = new LinkedBlockingQueue<>(1);
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private final Thread pollReader;

    public JLine3Input(Terminal terminal) {
        this.terminal = terminal;
        this.bindingReader = new BindingReader(terminal.reader());
        this.keyMap = buildKeyMap();

        this.pollReader = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && isRunning.get()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    inputQueue.offer(bindingReader.readBinding(keyMap), 5, TimeUnit.MILLISECONDS);

                } catch (InterruptedException | IOError e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        pollReader.start();
    }

    @Override
    public Key readInput() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return null;
    }

    @Override
    public Key pollInput() {
        return inputQueue.poll();
    }

    private KeyMap<Key> buildKeyMap() {
        KeyMap<Key> keyMap = new KeyMap<>();

        // Windows specific capabilities
        keyMap.bind(Key.UP , KeyMap.key(terminal, Capability.key_up));
        keyMap.bind(Key.DOWN,  KeyMap.key(terminal, Capability.key_down));
        keyMap.bind(Key.LEFT,  KeyMap.key(terminal, Capability.key_left));
        keyMap.bind(Key.RIGHT, KeyMap.key(terminal, Capability.key_right));

        // Linux specific capabilities
        keyMap.bind(Key.UP , "\033[A");
        keyMap.bind(Key.DOWN,  "\033[B");
        keyMap.bind(Key.LEFT,  "\033[D");
        keyMap.bind(Key.RIGHT, "\033[C");

        // General capabilities
        keyMap.bind(Key.ENTER, KeyMap.key(terminal, Capability.key_enter));
        keyMap.bind(Key.ENTER, "\r");
        keyMap.bind(Key.ENTER, "\n");

        keyMap.bind(Key.ESCAPE, "\033\033");

        keyMap.bind(Key.COUNTER_CLOCKWISE, "q");
        keyMap.bind(Key.CLOCKWISE, "e");

        // post-hoc to make `COUNTER_CLOCKWISE` and `CLOCKWISE` work
        // 0 -> 9, A -> Z, a -> z
        for (char c = '0'; c <= 'z'; c++) {
            if (Character.isLetterOrDigit(c)) {
                if (c == 'q' || c == 'e') continue;
                keyMap.bind(Key.fromCharacter(c), String.valueOf(c));
            }
        }

        return keyMap;
    }

    @Override
    public void close() throws IOException {
        isRunning.set(false);
        pollReader.interrupt();
    }
}
