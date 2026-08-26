package com.teic.trueris.game.query.rotation;

import com.teic.trueris.game.utils.RotationPair;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RotateQuery(Integer entityId, RotationPair rotationPair) {}
