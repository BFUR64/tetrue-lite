package com.teic.trueris.input;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

public class JLine3Input implements Input {
    private final Terminal terminal;
    private final BindingReader bindingReader;
    private final KeyMap<Action> keyMap;

    public JLine3Input(Terminal terminal) {
        this.terminal = terminal;
        this.bindingReader = new BindingReader(terminal.reader());
        this.keyMap = buildKeyMap();
    }

    @Override
    public Action readInput() {
        terminal.puts(Capability.keypad_xmit);
        terminal.flush();

        Action action = bindingReader.readBinding(keyMap);

        terminal.puts(Capability.keypad_local);
        terminal.flush();

        return action;
    }

    private KeyMap<Action> buildKeyMap() {
        KeyMap<Action> keyMap = new KeyMap<>();

        keyMap.bind(Action.UP , KeyMap.key(terminal, Capability.key_up));
        keyMap.bind(Action.DOWN,  KeyMap.key(terminal, Capability.key_down));
        keyMap.bind(Action.LEFT,  KeyMap.key(terminal, Capability.key_left));
        keyMap.bind(Action.RIGHT, KeyMap.key(terminal, Capability.key_right));

        keyMap.bind(Action.ENTER, KeyMap.key(terminal, Capability.key_enter));
        keyMap.bind(Action.ENTER, "\r");
        keyMap.bind(Action.ENTER, "\n");

        keyMap.bind(Action.UP, "w");
        keyMap.bind(Action.DOWN, "s");
        keyMap.bind(Action.LEFT, "a");
        keyMap.bind(Action.RIGHT, "d");

        keyMap.bind(Action.COUNTER_CLOCKWISE_ROTATE, "q");
        keyMap.bind(Action.CLOCKWISE_ROTATE, "e");

        return keyMap;
    }
}
