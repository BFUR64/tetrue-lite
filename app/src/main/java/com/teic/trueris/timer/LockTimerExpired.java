package com.teic.trueris.timer;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LockTimerExpired(Integer entityId) {}
