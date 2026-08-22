package com.teic.trueris.game.system;

import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.cell.CellType;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@NullMarked
public class SevenBagSystem {
    private static final int MIN_BLOCK_QUEUE_SIZE = 4;
    private final List<Integer> blockIdQueue = new LinkedList<>();
    private final BlockFactory blockFactory;

    public SevenBagSystem(BlockFactory blockFactory) {
        this.blockFactory = blockFactory;
    }

    public Integer getFirstBlock() {
        if (blockIdQueue.size() < MIN_BLOCK_QUEUE_SIZE) {
            blockIdQueue.addAll(createRandomizedBag());
        }

        Integer blockId = blockIdQueue.getFirst();
        blockIdQueue.removeFirst();

        blockFactory.convertBagBlockToBlock(blockId);

        return blockId;
    }

    private List<Integer> createRandomizedBag() {
        List<Integer> blockIds = new LinkedList<>();

        int position = MIN_BLOCK_QUEUE_SIZE;
        for (CellType cell : CellType.values()) {
            blockIds.add(blockFactory.createBagBlock(cell, position));
            position++;
        }

        Collections.shuffle(blockIds);

        return blockIds;
    }
}
