package com.teic.trueris.game;

import java.time.Duration;
import java.util.List;

import com.teic.trueris.game.block.BlockData;

public interface GameState {
    List<BlockData> viewBlockQueue();
    int getScore();
    boolean isGameOver();
    Duration getGravity();
    BlockData getActiveBlockCopy();
    BlockData getGhostBlockCopy();
    BlockData getHeldBlockCopy();
}
