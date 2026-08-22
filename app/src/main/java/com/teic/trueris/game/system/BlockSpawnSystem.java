package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.event.BlockPlaceEvent;
import com.teic.trueris.game.event.BlockSpawnEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BlockSpawnSystem {
    private final SevenBagSystem sevenBagSystem;
    private final EventBus eventBus;

    public BlockSpawnSystem(SevenBagSystem sevenBagSystem, World world, EventBus eventBus) {
        this.sevenBagSystem = sevenBagSystem;
        this.eventBus = eventBus;

        eventBus.subscribe(BlockPlaceEvent.class, event -> {
            world.remove(event.entityId());
            spawnBlock();
        });
    }

    public int spawnBlock() {
        Integer entityId = sevenBagSystem.getFirstBlock();

        eventBus.publish(new BlockSpawnEvent(entityId));

        return entityId;
    }
}
