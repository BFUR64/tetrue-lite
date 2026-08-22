package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveXQuery(Integer entityId, int dx) {}
