package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveBlockAccepted(Integer entityId, int dx, int dy) {}
