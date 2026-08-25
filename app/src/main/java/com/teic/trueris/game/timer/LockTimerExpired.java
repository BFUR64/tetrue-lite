package com.teic.trueris.game.timer;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LockTimerExpired(Integer entityId) {}
