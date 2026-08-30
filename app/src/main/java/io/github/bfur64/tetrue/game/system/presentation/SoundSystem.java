package io.github.bfur64.tetrue.game.system.presentation;

import io.github.bfur64.tetrue.Config;
import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.event.BlockPlaceEvent;
import io.github.bfur64.tetrue.game.event.GameOverEvent;
import io.github.bfur64.tetrue.game.event.LineClearEvent;
import io.github.bfur64.MicroSound;
import io.github.bfur64.Playback;
import io.github.bfur64.Sound;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class SoundSystem {
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
