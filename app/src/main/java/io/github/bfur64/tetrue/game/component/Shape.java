package io.github.bfur64.tetrue.game.component;

import io.github.bfur64.tetrue.game.block.BlockTemplate;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record Shape(BlockTemplate blockTemplate) {}
