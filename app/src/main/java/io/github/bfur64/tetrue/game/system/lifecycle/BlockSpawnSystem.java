package io.github.bfur64.tetrue.game.system.lifecycle;

import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.World;
import io.github.bfur64.tetrue.game.event.BlockHeldEvent;
import io.github.bfur64.tetrue.game.event.BlockPlaceEvent;
import io.github.bfur64.tetrue.game.event.BlockSpawnEvent;
import io.github.bfur64.tetrue.game.event.BlockSwitchEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BlockSpawnSystem {
    private final BlockQueueSystem sevenBagSystem;

    public BlockSpawnSystem(BlockQueueSystem sevenBagSystem, World world, EventBus eventBus) {
        this.sevenBagSystem = sevenBagSystem;

        eventBus.subscribe(BlockPlaceEvent.class, event -> {
            world.remove(event.entityId());

            int newEntityId = spawnBlock();
            eventBus.publish(new BlockSpawnEvent(newEntityId));
        });

        eventBus.subscribe(BlockHeldEvent.class, event -> {
            Integer previousHeldBlock = event.previousHeldBlock();
            Integer currentBlock = previousHeldBlock;

            if (previousHeldBlock == null) {
                currentBlock = spawnBlock();
            }

            eventBus.publish(new BlockSwitchEvent(currentBlock));
        });
    }

    public int spawnBlock() {
        return sevenBagSystem.getFirstBlock();
    }
}
