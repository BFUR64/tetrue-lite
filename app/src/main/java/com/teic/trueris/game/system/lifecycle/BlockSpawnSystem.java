package com.teic.trueris.game.system.lifecycle;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.event.BlockHeldEvent;
import com.teic.trueris.game.event.BlockPlaceEvent;
import com.teic.trueris.game.event.BlockSpawnEvent;
import com.teic.trueris.game.event.BlockSwitchEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BlockSpawnSystem {
    private final BlockQueueSystem sevenBagSystem;

    public BlockSpawnSystem(BlockQueueSystem sevenBagSystem, World world, EventBus eventBus) {
        this.sevenBagSystem = sevenBagSystem;

        eventBus.subscribe(BlockPlaceEvent.class, event -> {
            world.remove(event.entityId());

            Integer newEntityId = spawnBlock();
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
