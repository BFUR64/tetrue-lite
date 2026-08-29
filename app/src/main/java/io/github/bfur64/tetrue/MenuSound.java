package io.github.bfur64.tetrue;

import io.github.bfur64.MicroSound;
import io.github.bfur64.Sound;
import io.github.bfur64.menu.Event;
import io.github.bfur64.menu.event.CursorChangeEvent;
import io.github.bfur64.menu.event.ItemSelectEvent;

public class MenuSound {
    public MenuSound(Event event) {
        MicroSound sound = new MicroSound();

        Sound buttonClick = sound.load("/buttonClick.wav");
        Sound cursorChange = sound.load("/cursorChange.wav");

        event.subscribe(ItemSelectEvent.class, e -> sound.play(buttonClick, false));
        event.subscribe(CursorChangeEvent.class, e -> sound.play(cursorChange, false));
    }
}
