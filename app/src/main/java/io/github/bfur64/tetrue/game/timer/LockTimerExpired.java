package io.github.bfur64.tetrue.game.timer;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LockTimerExpired(int entityId) {}
