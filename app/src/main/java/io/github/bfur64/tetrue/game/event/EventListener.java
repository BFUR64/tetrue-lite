package io.github.bfur64.tetrue.game.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EventListener<T> {
    void onEvent(T event);
}
