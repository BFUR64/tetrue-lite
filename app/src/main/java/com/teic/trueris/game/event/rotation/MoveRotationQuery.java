package com.teic.trueris.game.event.rotation;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveRotationQuery(Integer entityId, int direction, int dx, int dy) {}
