package com.teic.trueris.game;

import java.util.List;

import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.block.BlockRegistry;

public interface GameState {
    List<BlockRegistry.BlockTemplate> viewBlockQueue();
    int getScore();
    boolean isGameOver();
    long getGravityThreshold();
    BlockData getActiveBlockCopy();
    BlockData getGhostBlockCopy();
}
