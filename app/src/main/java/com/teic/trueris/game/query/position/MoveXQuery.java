package com.teic.trueris.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveXQuery(int entityId, int dx) {}
