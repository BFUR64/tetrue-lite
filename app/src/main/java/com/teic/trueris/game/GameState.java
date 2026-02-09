package com.teic.trueris.game;

import java.util.List;

import com.teic.trueris.game.block.BlockRegistry;

public interface GameState {
    public List<BlockRegistry.BlockTemplate> viewBlockQueue();
    public int getScore();
    public boolean isGameOver();
}
