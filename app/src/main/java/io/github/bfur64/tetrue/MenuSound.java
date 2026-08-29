package io.github.bfur64.tetrue;

import io.github.bfur64.MicroSound;
import io.github.bfur64.Sound;
import io.github.bfur64.menu.Event;
import io.github.bfur64.menu.event.CursorChangeEvent;
import io.github.bfur64.menu.event.ItemSelectEvent;

public final class MenuSound {
    private static final MicroSound sound;

    private static final Sound buttonClick;
    private static final Sound cursorChange;

    static {
        sound = new MicroSound();
        buttonClick = sound.load("/buttonClick.wav");
        cursorChange = sound.load("/cursorChange.wav");
    }

    public static void registerSounds(Event event) {
        event.subscribe(ItemSelectEvent.class,
                e -> sound.play(buttonClick, false));

        event.subscribe(CursorChangeEvent.class,
                e -> sound.play(cursorChange, false));
    }
}
