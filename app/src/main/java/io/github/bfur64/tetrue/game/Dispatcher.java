package io.github.bfur64.tetrue.game;

import io.github.bfur64.tetrue.game.event.EventListener;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Dispatcher<T> {
    private final Class<T> eventType;
    private final EventListener<T> eventListener;

    public Dispatcher(Class<T> eventType, EventListener<T> eventListener) {
        this.eventType = eventType;
        this.eventListener = eventListener;
    }

    public void dispatch(Object event) {
        eventListener.onEvent(eventType.cast(event));
    }
}
