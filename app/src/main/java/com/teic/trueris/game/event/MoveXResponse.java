package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveXResponse(Integer entityId, boolean isValid, int dx) {}
