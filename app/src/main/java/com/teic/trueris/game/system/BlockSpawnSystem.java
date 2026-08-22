package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.event.BlockSpawnEvent;
import com.teic.trueris.game.event.MoveBlockRejected;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BlockSpawnSystem {
    private final BlockFactory blockFactory;
    private final World world;
    private final EventBus eventBus;

    public BlockSpawnSystem(BlockFactory blockFactory, World world, EventBus eventBus) {
        this.blockFactory = blockFactory;
        this.world = world;
        this.eventBus = eventBus;

        eventBus.subscribe(MoveBlockRejected.class, event -> {
            if (event.grounded()) {
                world.remove(event.entityId());
                spawnBlock();
            }
        });
    }

    public int spawnBlock() {
        Integer entityId = blockFactory.createBlock(CellType.S);

        eventBus.publish(new BlockSpawnEvent(entityId));

        return entityId;
    }
}
