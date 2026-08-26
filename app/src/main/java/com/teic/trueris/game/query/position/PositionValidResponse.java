package com.teic.trueris.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record PositionValidResponse(int entityId, boolean isValid) {}
