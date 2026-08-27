package io.github.bfur64.tetrue.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record ScoreChangeEvent(int score, int oneClear, int twoClear, int threeClear, int fourClear) {}
