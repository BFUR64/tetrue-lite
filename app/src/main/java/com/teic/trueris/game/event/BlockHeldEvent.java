package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record BlockHeldEvent(@Nullable Integer previousHeldBlock) {}
