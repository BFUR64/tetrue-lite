package io.github.bfur64.tetrue.game.utils;

import io.github.bfur64.tetrue.game.block.Direction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RotationPair(Direction first, Direction second) {}
