package com.teic.trueris.game.system.gameplay;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.event.LineClearEvent;
import com.teic.trueris.game.event.ScoreChangeEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ScoreTrackerSystem {
    private int score;

    private int oneClear;
    private int twoClear;
    private int threeClear;
    private int fourClear;

    public ScoreTrackerSystem(EventBus eventBus) {
        eventBus.subscribe(LineClearEvent.class, event -> {
            updateScore(event.rowsFilled());

            eventBus.publish(new ScoreChangeEvent(score, oneClear, twoClear, threeClear, fourClear));
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
            case 1 -> {
                oneClear++;
                yield 100;
            }
            case 2 -> {
                twoClear++;
                yield 300;
            }
            case 3 -> {
                threeClear++;
                yield 600;
            }
            case 4 -> {
                fourClear++;
                yield 1500;
            }
            default -> 0;
        };
    }
}
