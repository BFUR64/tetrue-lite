package com.teic.trueris.game;

import com.teic.trueris.game.event.EventListener;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public class EventBus {
    private final Map<Class<?>, List<EventListener<?>>> listeners = new HashMap<>();

    public <T> void subscribe(Class<T> eventType, EventListener<T> listener) {
        listeners
            .computeIfAbsent(eventType, ignore -> new ArrayList<>())
            .add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        List<EventListener<?>> listenersForEvent = listeners.get(event.getClass());

        if (listenersForEvent == null) {
            return;
        }

        for (EventListener<?> listener : listenersForEvent) {
            ((EventListener<T>) listener).onEvent(event);
        }
    }
}
