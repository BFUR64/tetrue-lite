package io.github.bfur64.tetrue.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record BlockPlaceEvent(int entityId) {}
