package com.teic.trueris.game.event;

import java.util.List;

public record BlockQueueChangeEvent(List<Integer> entityIds) {
    public BlockQueueChangeEvent {
        entityIds = List.copyOf(entityIds);
    }
}
