package com.teic.trueris.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EventListener<T> {
    void onEvent(T event);
}
