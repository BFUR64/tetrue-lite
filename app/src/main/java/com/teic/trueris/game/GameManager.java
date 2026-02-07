package com.teic.trueris.game;

import java.util.List;

import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.block.BlockManager;
import com.teic.trueris.game.block.BlockQueue;
import com.teic.trueris.game.block.BlockRegistry;
import com.teic.trueris.game.grid.GridData;
import com.teic.trueris.game.grid.GridManager;
import com.teic.trueris.game.grid.GridType;

public class GameManager implements GameState {
    private final BlockManager blockManager;
    private final GridManager gridManager;
    private final BlockQueue blockQueue;
    private final ScoreTracker scoreTracker;

    private BlockData activeBlock;
    private BlockData ghostBlock;

    public GameManager(GridData gridData) {
        this.blockManager = new BlockManager(gridData);
        this.gridManager = new GridManager(gridData);
        this.blockQueue = new BlockQueue();
        this.scoreTracker = new ScoreTracker();

        generateActiveBlock();
        generateGhostBlock();
    }

    // =====================
    // Movement
    // =====================
    public void moveBlockDown() {
        gridManager.eraseGrid(GridType.ACTIVE);

        if (!blockManager.moveBlockDown(activeBlock)) {
            gridManager.writeGrid(GridType.SOLID, activeBlock);
            scoreTracker.updateScore(gridManager.clearFilledRows());

            generateActiveBlock();
            generateGhostBlock();

            return;
        }

        gridManager.writeGrid(GridType.ACTIVE, activeBlock);
    }

    public void dropBlock() {
        blockManager.dropBlock(activeBlock);

        gridManager.writeGrid(GridType.SOLID, activeBlock);
        scoreTracker.updateScore(gridManager.clearFilledRows());

        generateActiveBlock();
        generateGhostBlock();
    }

    public void moveBlockLeft() {
        if (blockManager.moveBlockLeft(activeBlock)) {
            gridManager.eraseGrid(GridType.ACTIVE);
            gridManager.writeGrid(GridType.ACTIVE, activeBlock);

            generateGhostBlock();
        }
    }

    public void moveBlockRight() {
        if (blockManager.moveBlockRight(activeBlock)) {
            gridManager.eraseGrid(GridType.ACTIVE);
            gridManager.writeGrid(GridType.ACTIVE, activeBlock);

            generateGhostBlock();
        }
    }

    // =====================
    // Rotation
    // =====================
    public void rotateBlockLeft() {
        if (blockManager.rotateBlockLeft(activeBlock)) {
            gridManager.eraseGrid(GridType.ACTIVE);
            gridManager.writeGrid(GridType.ACTIVE, activeBlock);

            generateGhostBlock();
        }
    }

    public void rotateBlockRight() {
        if (blockManager.rotateBlockRight(activeBlock)) {
            gridManager.eraseGrid(GridType.ACTIVE);
            gridManager.writeGrid(GridType.ACTIVE, activeBlock);

            generateGhostBlock();
        }
    }

    // =====================
    // Utilities
    // =====================
    private void generateActiveBlock() {
        activeBlock = new BlockData(blockQueue.getRandomBlock());
        gridManager.eraseGrid(GridType.ACTIVE);
        gridManager.writeGrid(GridType.ACTIVE, activeBlock);
    }

    private void generateGhostBlock() {
        ghostBlock = activeBlock.copyBlockData();

        blockManager.dropBlock(ghostBlock);

        gridManager.eraseGrid(GridType.GHOST);
        gridManager.writeGrid(GridType.GHOST, ghostBlock);
    }

    // =====================
    // Game State Interface
    // =====================
    @Override
    public List<BlockRegistry.BlockTemplate> viewBlockQueue() {
        return blockQueue.viewBlockQueue();
    }

    @Override
    public int getScore() {
        return scoreTracker.getScore();
    }
}
