package com.teic.trueris.game.system;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.event.BlockPlaceEvent;
import com.teic.trueris.game.event.LineClearEvent;
import io.github.bfur64.MicroSound;
import io.github.bfur64.Sound;

public class SoundSystem {
    public SoundSystem(EventBus eventBus) {
        if (Config.soundEnabled.get()) {
            MicroSound sound = new MicroSound();

            Sound blockPlace = sound.load("/blockPlace.wav");
            Sound lineClear = sound.load("/lineClear.wav");

            eventBus.subscribe(BlockPlaceEvent.class, event -> sound.play(blockPlace, false));

            eventBus.subscribe(LineClearEvent.class, event -> sound.play(lineClear, false));
        }
    }
}
