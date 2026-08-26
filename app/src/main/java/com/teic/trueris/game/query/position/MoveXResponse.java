package com.teic.trueris.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveXResponse(int entityId, boolean isValid, int dx) {}
