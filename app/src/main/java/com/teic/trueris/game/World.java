package com.teic.trueris.game;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;

@NullMarked
public class World {
    private final EventBus events = new EventBus();
    private final Map<Class<?>, Map<Integer, Object>> components = new HashMap<>();

    public void add(Integer entityId, Object component) {
        components
            .computeIfAbsent(component.getClass(), ignored -> new HashMap<>())
            .put(entityId, component);
    }

    public void remove(Integer entityId, Class<?> componentType) {
        Map<Integer, Object> storage = components.get(componentType);

        if (storage != null) {
            storage.remove(entityId);
        }
    }

    @Nullable
    public <T> T get(Integer entityId, Class<T> componentType) {
        Map<Integer, Object> storage = components.get(componentType);

        if (storage == null) {
            return null;
        }

        return componentType.cast(storage.get(entityId));
    }

    public boolean has(Integer entityId, Class<?> ...componentTypes) {
        for (Class<?> componentType : componentTypes) {
            if (!contains(componentType)) return false;
            if (!components.get(componentType).containsKey(entityId)) return false;
        }

        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean contains(Class<?> componentType) {
        return components.containsKey(componentType);
    }

    public List<Integer> query(Class<?>... componentTypes) {
        if (componentTypes.length == 0) {
            return new ArrayList<>();
        }

        Class<?> first = componentTypes[0];

        if (!contains(first)) {
            return new ArrayList<>();
        }

        Set<Integer> result =
                new HashSet<>(components.get(first).keySet());

        for (int i = 1; i < componentTypes.length; i++) {
            Class<?> componentType = componentTypes[i];

            if (!contains(componentType)) {
                return new ArrayList<>();
            }

            result.retainAll(
                    components.get(componentType).keySet()
            );
        }

        return new ArrayList<>(result);
    }

    public EventBus getEvents() {
        return events;
    }
}
