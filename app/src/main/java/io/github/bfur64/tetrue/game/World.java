package io.github.bfur64.tetrue.game;

import org.jspecify.annotations.NullMarked;

import java.util.*;

@NullMarked
public class World {
    private final Map<Class<?>, Map<Integer, Object>> components = new HashMap<>();

    public void put(int entityId, Object component) {
        components
            .computeIfAbsent(component.getClass(), ignored -> new HashMap<>())
            .put(entityId, component);
    }

    public void remove(int entityId, Class<?> componentType) {
        Map<Integer, Object> storage = components.get(componentType);

        if (storage != null) {
            storage.remove(entityId);
        }
    }

    public void remove(int entityId) {
        for (Map<Integer, Object> storage : components.values()) {
            storage.remove(entityId);
        }
    }

    public <T> T get(int entityId, Class<T> componentType) {
        Map<Integer, Object> storage = components.get(componentType);

        if (storage == null) {
            throw new IllegalStateException(
                "Entity " + entityId + "does not have " +
                componentType.getSimpleName() + ". Have you done `query` first?");
        }

        return componentType.cast(storage.get(entityId));
    }

    public boolean has(int entityId, Class<?> ...componentTypes) {
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

    public boolean exists(int entityId) {
        for (Map<Integer, Object> component : components.values()) {
            if (component.containsKey(entityId)) {
                return true;
            }
        }

        return false;
    }

    public List<Integer> query(Class<?>... componentTypes) {
        if (componentTypes.length == 0) {

            Set<Integer> result = new HashSet<>();
            for (Map<Integer, Object> component: components.values()) {
                result.addAll(component.keySet());
            }

            return new ArrayList<>(result);
        }

        Class<?> first = componentTypes[0];

        if (!contains(first)) {
            return new ArrayList<>();
        }

        Set<Integer> result = new HashSet<>(components.get(first).keySet());

        for (int i = 1; i < componentTypes.length; i++) {
            Class<?> componentType = componentTypes[i];

            if (!contains(componentType)) {
                return new ArrayList<>();
            }

            result.retainAll(components.get(componentType).keySet()
            );
        }

        return new ArrayList<>(result);
    }
}
