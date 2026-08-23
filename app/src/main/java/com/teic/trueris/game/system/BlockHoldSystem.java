package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.event.BlockHeldEvent;
import com.teic.trueris.game.event.BlockSpawnEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class BlockHoldSystem {
    private final BlockFactory blockFactory;
    private final World world;
    private final EventBus eventBus;

    private @Nullable Integer entityId;

    private boolean blockSwitched = false;

    public BlockHoldSystem(BlockFactory blockFactory, World world, EventBus eventBus) {
        this.blockFactory = blockFactory;
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(BlockSpawnEvent.class, event -> blockSwitched = false);
    }

    public void holdBlock(Integer entityId) {
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
