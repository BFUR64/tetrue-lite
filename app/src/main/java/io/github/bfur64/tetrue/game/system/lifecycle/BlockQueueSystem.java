package io.github.bfur64.tetrue.game.system.lifecycle;

import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.block.BlockFactory;
import io.github.bfur64.tetrue.game.cell.CellType;
import io.github.bfur64.tetrue.game.event.BlockQueueChangeEvent;
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

    public int getFirstBlock() {
        if (blockIdQueue.size() < MIN_BLOCK_QUEUE_SIZE) {
            blockIdQueue.addAll(createRandomizedBag());
        }

        int blockId = blockIdQueue.getFirst();
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
