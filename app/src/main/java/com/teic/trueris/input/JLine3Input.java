package com.teic.trueris.input;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

public class JLine3Input implements Input {
    private final Terminal terminal;
    private final BindingReader bindingReader;
    private final KeyMap<Key> keyMap;

    public JLine3Input(Terminal terminal) {
        this.terminal = terminal;
        this.bindingReader = new BindingReader(terminal.reader());
        this.keyMap = buildKeyMap();
    }

    @Override
    public Key readInput() {
        terminal.puts(Capability.keypad_xmit);
        terminal.flush();

        Key key = bindingReader.readBinding(keyMap);

        terminal.puts(Capability.keypad_local);
        terminal.flush();

        return key;
    }

    private KeyMap<Key> buildKeyMap() {
        KeyMap<Key> keyMap = new KeyMap<>();

        keyMap.bind(Key.UP , KeyMap.key(terminal, Capability.key_up));
        keyMap.bind(Key.DOWN,  KeyMap.key(terminal, Capability.key_down));
        keyMap.bind(Key.LEFT,  KeyMap.key(terminal, Capability.key_left));
        keyMap.bind(Key.RIGHT, KeyMap.key(terminal, Capability.key_right));

        keyMap.bind(Key.ENTER, KeyMap.key(terminal, Capability.key_enter));
        keyMap.bind(Key.ENTER, "\r");
        keyMap.bind(Key.ENTER, "\n");

        keyMap.bind(Key.COUNTER_CLOCKWISE, "q");
        keyMap.bind(Key.CLOCKWISE, "e");
        
        // 0 -> 9, A -> Z, a -> z
        for (char c = '0'; c <= 'z'; c++) {
            if (Character.isLetterOrDigit(c)) {
                keyMap.bind(Key.fromCharacter(c), String.valueOf(c));
            }
        }

        keyMap.setNomatch(Key.UNKNOWN);

        return keyMap;
    }
}
