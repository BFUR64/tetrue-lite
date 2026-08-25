package com.teic.trueris.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record PositionValidResponse(Integer entityId, boolean isValid) {}
