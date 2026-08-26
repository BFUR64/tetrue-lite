package com.teic.trueris.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record GhostPositionResponse(int entityId, int dy) {}
