package io.github.bfur64.tetrue.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveXResponse(int entityId, boolean isValid, int dx) {}
