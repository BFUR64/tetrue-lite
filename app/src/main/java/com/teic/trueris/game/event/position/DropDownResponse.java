package com.teic.trueris.game.event.position;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record DropDownResponse(Integer entityId, boolean canDrop) {}
