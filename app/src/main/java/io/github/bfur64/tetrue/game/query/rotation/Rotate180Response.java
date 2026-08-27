package io.github.bfur64.tetrue.game.query.rotation;

import io.github.bfur64.tetrue.game.block.Direction;

public record Rotate180Response(int entityId, boolean isValid, Direction direction, int dx, int dy) {}
