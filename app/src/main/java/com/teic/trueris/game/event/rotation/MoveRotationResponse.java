package com.teic.trueris.game.event.rotation;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveRotationResponse(Integer entityId, boolean isValid, int direction, int dx, int dy) {}
