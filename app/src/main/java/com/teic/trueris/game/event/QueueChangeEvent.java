package com.teic.trueris.game.event;

import java.util.List;

public record QueueChangeEvent(List<Integer> entityIds) {
    public QueueChangeEvent {
        entityIds = List.copyOf(entityIds);
    }
}
