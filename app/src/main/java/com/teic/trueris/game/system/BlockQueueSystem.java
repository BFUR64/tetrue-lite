package com.teic.trueris.game.system;

import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.event.BlockQueueChangeEvent;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@NullMarked
public class BlockQueueSystem {
    private static final int MIN_BLOCK_QUEUE_SIZE = 4;
    private final List<Integer> blockIdQueue = new LinkedList<>();
    private final BlockFactory blockFactory;
    private final EventBus eventBus;

    public BlockQueueSystem(BlockFactory blockFactory, EventBus eventBus) {
        this.blockFactory = blockFactory;
        this.eventBus = eventBus;
    }

    public Integer getFirstBlock() {
        if (blockIdQueue.size() < MIN_BLOCK_QUEUE_SIZE) {
            blockIdQueue.addAll(createRandomizedBag());
        }

        Integer blockId = blockIdQueue.getFirst();
        blockIdQueue.removeFirst();

        blockFactory.convertBagBlockToBlock(blockId);

        eventBus.publish(new BlockQueueChangeEvent(blockIdQueue));

        return blockId;
    }

    private List<Integer> createRandomizedBag() {
        List<Integer> blockIds = new LinkedList<>();

        for (CellType cell : CellType.values()) {
            blockIds.add(blockFactory.createBagBlock(cell));
        }

        Collections.shuffle(blockIds);

        return blockIds;
    }
}
