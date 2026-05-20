package com.teic.trueris.game.utils;

import com.teic.trueris.game.block.Direction;

public record RotationResult(boolean success, Direction newRotation, int rowOffset, int colOffset) {}
