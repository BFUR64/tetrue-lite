package com.teic.trueris.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveXResponse(Integer entityId, boolean isValid, int dx) {}
