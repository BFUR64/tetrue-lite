package com.teic.trueris.game.event.timer;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LockTimerExpired(Integer entityId) {}
