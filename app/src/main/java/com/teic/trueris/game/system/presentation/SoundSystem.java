package com.teic.trueris.game.system.presentation;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.event.BlockPlaceEvent;
import com.teic.trueris.game.event.GameOverEvent;
import com.teic.trueris.game.event.LineClearEvent;
import io.github.bfur64.MicroSound;
import io.github.bfur64.Playback;
import io.github.bfur64.Sound;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class SoundSystem {
    private @Nullable Playback bgMusicPlayback;

    public SoundSystem(EventBus eventBus) {
        if (Config.soundEnabled.get()) {
            MicroSound sound = new MicroSound();

            Sound blockPlace = sound.load("/blockPlace.wav");
            Sound lineClear = sound.load("/lineClear.wav");
            Sound gameOver = sound.load("/gameOver.wav");
            Sound bgMusic = sound.load("/bgMusic.wav");

            eventBus.subscribe(BlockPlaceEvent.class, event -> sound.play(blockPlace, false));

            eventBus.subscribe(LineClearEvent.class, event -> sound.play(lineClear, false));

            eventBus.subscribe(GameOverEvent.class, event -> {
                if (bgMusicPlayback != null) {
                bgMusicPlayback.stop();
                }

                sound.play(gameOver, false);
            });

            bgMusicPlayback = sound.play(bgMusic, true);
        }
    }
}
