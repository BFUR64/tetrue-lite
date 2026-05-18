package com.teic.trueris.game;

import java.time.Duration;
import java.util.List;

import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.block.BlockRegistry;

public interface GameState {
    List<BlockRegistry.BlockTemplate> viewBlockQueue();
    int getScore();
    boolean isGameOver();
    Duration getGravity();
    BlockData getActiveBlockCopy();
    BlockData getGhostBlockCopy();
}
