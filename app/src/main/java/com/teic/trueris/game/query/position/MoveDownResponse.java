package com.teic.trueris.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveDownResponse(Integer entityId, boolean canDrop) {}
