package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveBlockQueryResponse(Integer entityId, boolean isValid) {}
