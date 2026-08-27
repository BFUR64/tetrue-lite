package io.github.bfur64.tetrue.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LineClearEvent(boolean[] rowsFilled) {
    public LineClearEvent {
        rowsFilled = rowsFilled.clone();
    }

    @Override
    public boolean[] rowsFilled() {
        return rowsFilled.clone();
    }
}
