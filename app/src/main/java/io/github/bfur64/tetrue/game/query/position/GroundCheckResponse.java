package io.github.bfur64.tetrue.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record GroundCheckResponse(int entityId, boolean isClear) {}
