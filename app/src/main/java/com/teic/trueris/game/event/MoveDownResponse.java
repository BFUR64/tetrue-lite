package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveDownResponse(Integer entityId, boolean canDrop) {}
