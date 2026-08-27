package io.github.bfur64.tetrue.game.system.lifecycle;

import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.block.BlockFactory;
import io.github.bfur64.tetrue.game.event.BlockHeldEvent;
import io.github.bfur64.tetrue.game.event.BlockSpawnEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class BlockHoldSystem {
    private final BlockFactory blockFactory;
    private final EventBus eventBus;

    private @Nullable Integer entityId;

    private boolean blockSwitched = false;

    public BlockHoldSystem(BlockFactory blockFactory, EventBus eventBus) {
        this.blockFactory = blockFactory;
        this.eventBus = eventBus;

        eventBus.subscribe(BlockSpawnEvent.class, event -> blockSwitched = false);
    }

    public void holdBlock(int entityId) {
        if (blockSwitched) return;

        Integer currentHeldBlock = this.entityId;
        blockFactory.convertBlockToHeldBlock(entityId);
        this.entityId = entityId;

        if (currentHeldBlock != null) {
            blockFactory.convertHeldBlockToBlock(currentHeldBlock);
        }

        blockSwitched = true;
        eventBus.publish(new BlockHeldEvent(currentHeldBlock));
    }
}
