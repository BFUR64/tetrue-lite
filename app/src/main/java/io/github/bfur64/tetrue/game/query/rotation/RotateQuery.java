package io.github.bfur64.tetrue.game.query.rotation;

import io.github.bfur64.tetrue.game.utils.RotationPair;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RotateQuery(int entityId, RotationPair rotationPair) {}
