package com.teic.trueris.game.component;

import com.teic.trueris.game.block.BlockTemplate;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record Shape(BlockTemplate blockTemplate) {}
