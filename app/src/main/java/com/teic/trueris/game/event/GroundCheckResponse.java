package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record GroundCheckResponse(Integer entityId, boolean isClear) {}
