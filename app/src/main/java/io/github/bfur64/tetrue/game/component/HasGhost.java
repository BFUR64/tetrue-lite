package io.github.bfur64.tetrue.game.component;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record HasGhost(@Nullable Integer childId) {}
