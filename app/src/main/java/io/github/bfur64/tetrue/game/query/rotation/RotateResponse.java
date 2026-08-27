package io.github.bfur64.tetrue.game.query.rotation;

import io.github.bfur64.tetrue.game.block.Direction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RotateResponse(int entityId, boolean isValid, Direction direction, int dx, int dy) {}
