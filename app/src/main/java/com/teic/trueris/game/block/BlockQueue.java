package com.teic.trueris.game.block;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class BlockQueue {
    private static final int MIN_BLOCK_QUEUE_SIZE = 3;
    
    private final List<BlockData> blockQueue;

    public BlockQueue() {
        blockQueue = new LinkedList<>();
    }

    public BlockData getFirstBlock() {
        if (blockQueue.size() <= MIN_BLOCK_QUEUE_SIZE) {
            addToBlockQueue(createRandomizedBag());
        }

        BlockData blockData = blockQueue.getFirst();
        blockQueue.removeFirst();

        return blockData;
    }

    public List<BlockData> viewBlockQueue() {
        return Collections.unmodifiableList(blockQueue);
    }

    private void addToBlockQueue(List<BlockData> blocks) {
        blockQueue.addAll(blocks);
    }

    private List<BlockData> createRandomizedBag() {
//        List<BlockRegistry.BlockTemplate> sevenBag = new LinkedList<>(BlockRegistry.values());

        List<BlockRegistry.BlockTemplate> sevenBagTemplates = new LinkedList<>(BlockRegistry.values());
        List<BlockData> sevenBagBlockData = new LinkedList<>();

        for (BlockRegistry.BlockTemplate template : sevenBagTemplates) {
            sevenBagBlockData.add(new BlockData(template.copyBlock()));
        }

//        Collections.shuffle(sevenBag);
        Collections.shuffle(sevenBagBlockData);

        return sevenBagBlockData;
    }
}
