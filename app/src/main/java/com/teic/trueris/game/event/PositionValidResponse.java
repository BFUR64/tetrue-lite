package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record PositionValidResponse(Integer entityId, boolean isValid) {}
