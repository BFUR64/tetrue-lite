package io.github.bfur64.tetrue.game.component;

import io.github.bfur64.tetrue.game.block.Direction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record Rotation(Direction direction) {}
