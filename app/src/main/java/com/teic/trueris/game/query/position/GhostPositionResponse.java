package com.teic.trueris.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record GhostPositionResponse(Integer entityId, int dy) {}
