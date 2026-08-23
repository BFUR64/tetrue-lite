package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.event.LineClearEvent;
import com.teic.trueris.game.event.ScoreChangeEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ScoreTrackerSystem {
    private int score;

    public ScoreTrackerSystem(EventBus eventBus) {
        eventBus.subscribe(LineClearEvent.class, event -> {
            updateScore(event.rowsFilled());

            eventBus.publish(new ScoreChangeEvent(score));
        });
    }

    public void updateScore(boolean[] filledRows) {
        int totalFilledRows = 0;

        for (boolean filledRow : filledRows) {
            if (!filledRow) {
                continue;
            }

            totalFilledRows++;
        }

        score += switch (totalFilledRows) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 600;
            case 4 -> 1500;
            default -> 0;
        };
    }
}
