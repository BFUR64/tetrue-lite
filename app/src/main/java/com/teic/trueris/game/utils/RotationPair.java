package com.teic.trueris.game.utils;

import com.teic.trueris.game.block.Direction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RotationPair(Direction first, Direction second) {}
