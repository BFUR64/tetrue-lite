package com.teic.trueris.game.query.rotation;

import com.teic.trueris.game.block.Direction;

public record Rotate180Response(int entityId, boolean isValid, Direction direction, int dx, int dy) {}
