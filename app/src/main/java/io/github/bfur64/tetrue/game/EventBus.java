package io.github.bfur64.tetrue.game;

import io.github.bfur64.tetrue.game.event.EventListener;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public final class EventBus {
    private final Map<Class<?>, List<Dispatcher<?>>> listeners = new HashMap<>();

    public <T> void subscribe(Class<T> eventType, EventListener<T> listener) {
        listeners
            .computeIfAbsent(eventType, ignore -> new ArrayList<>())
            .add(new Dispatcher<>(eventType, listener));
    }

    public void publish(Object event) {
        List<Dispatcher<?>> listenersForEvent = listeners.get(event.getClass());

        if (listenersForEvent == null) {
            return;
        }

        for (Dispatcher<?> listener : listenersForEvent) {
            listener.dispatch(event);
        }
    }
}
