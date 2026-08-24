package com.teic.trueris.game;

import com.teic.trueris.game.event.BlockPlaceEvent;
import com.teic.trueris.game.event.LineClearEvent;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.bfur64.MicroSound;
import io.github.bfur64.Sound;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class SoundManager {
    @SuppressFBWarnings(
        value = "DE",
        justification = "Logging has not been implemented yet."
    )
    public SoundManager(EventBus eventBus) {
        try {
            MicroSound sound = new MicroSound();

            Sound blockPlace = sound.load("/blockPlace.wav");
            Sound lineClear = sound.load("/lineClear.wav");

            eventBus.subscribe(BlockPlaceEvent.class, event -> {
                sound.play(blockPlace, false);
            });

            eventBus.subscribe(LineClearEvent.class, event -> {
                sound.play(lineClear, false);
            });
        }
        catch (UnsupportedAudioFileException | IOException ignored) {}
    }
}
