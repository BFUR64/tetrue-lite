package com.teic.trueris.input;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Key {
    public static final Key UP = new Key(null);
    public static final Key DOWN = new Key(null);
    public static final Key LEFT = new Key(null);
    public static final Key RIGHT = new Key(null);
    public static final Key ENTER = new Key(null);
    public static final Key ESCAPE = new Key(null);


    public static final Key CLOCKWISE = new Key(null);
    public static final Key COUNTER_CLOCKWISE = new Key(null);

    public static final Key UNKNOWN = new Key(null);

    private static final Map<Character, Key> characters;

    private final Character character;

    static {
        characters = new HashMap<>();

        // 0 -> 9, A -> Z, a -> z
        for (char c = '0'; c <= 'z'; c++) {
            if (Character.isLetterOrDigit(c)) {
                characters.put(c, new Key(c));
            }
        }
    }

    public static Key fromCharacter(Character c) {
        return characters.get(c);
    }

    private Key(Character character) {
        this.character = character;
    }

    public boolean hasCharacter() {
        return character != null;
    }

    public boolean matches(Key target) {
        return this == target;
    }

    public boolean matches(Character target) {
        return Objects.equals(target, character);
    }

    public Character getCharacter() {
        return character;
    }
}
