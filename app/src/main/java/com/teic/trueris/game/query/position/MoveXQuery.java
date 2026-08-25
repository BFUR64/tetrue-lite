package com.teic.trueris.game.query.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record MoveXQuery(Integer entityId, int dx) {}
