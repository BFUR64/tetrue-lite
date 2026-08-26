package com.teic.trueris.game.query.rotation;

import com.teic.trueris.game.block.Direction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RotateResponse(Integer entityId, boolean isValid, Direction direction, int dx, int dy) {}
