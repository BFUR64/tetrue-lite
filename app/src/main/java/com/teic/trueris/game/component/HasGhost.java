package com.teic.trueris.game.component;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record HasGhost(@Nullable Integer childId) {}
