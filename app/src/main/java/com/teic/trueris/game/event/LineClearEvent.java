package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LineClearEvent(boolean[] rowsFilled) {}
