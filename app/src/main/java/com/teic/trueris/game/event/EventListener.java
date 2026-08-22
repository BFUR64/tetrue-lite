package com.teic.trueris.game.event;

public interface EventListener<T> {
    void onEvent(T event);
}
