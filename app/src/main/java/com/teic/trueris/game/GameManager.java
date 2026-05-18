package com.teic.trueris.game;

import java.time.Duration;
import java.util.List;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.block.BlockManager;
import com.teic.trueris.game.block.BlockQueue;
import com.teic.trueris.game.block.BlockRegistry;
import com.teic.trueris.game.grid.GridData;
import com.teic.trueris.game.grid.GridManager;

public class GameManager implements GameState {
    private final BlockManager blockManager;
    private final GridManager gridManager;
    private final BlockQueue blockQueue;
    private final ScoreTracker scoreTracker;

    private BlockData activeBlock;
    private BlockData ghostBlock;

    // Game Variables
    private boolean blockGrounded;

    private long gravityTimer;

    private long lockTimer;

    private boolean gameOver;

//    private long gravityThreshold = 500_000_000; // 0.5 Seconds

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
        if (!blockManager.moveBlockDown(activeBlock)) {
            gridManager.writeGrid(activeBlock);
            scoreTracker.updateScore(gridManager.clearFilledRows());

            generateActiveBlock();
            generateGhostBlock();

            return;
        }
        
        gravityTimer = 0;
    }

    public void dropBlock() {
        blockManager.dropBlock(activeBlock);

        gridManager.writeGrid(activeBlock);
        scoreTracker.updateScore(gridManager.clearFilledRows());

        generateActiveBlock();
        generateGhostBlock();

        gravityTimer = 0;
    }

    public void moveBlockLeft() {
        if (blockManager.moveBlockLeft(activeBlock)) {
            generateGhostBlock();

            lockTimer = 0;
        }
    }

    public void moveBlockRight() {
        if (blockManager.moveBlockRight(activeBlock)) {
            generateGhostBlock();

            lockTimer = 0;
        }
    }

    // =====================
    // Rotation
    // =====================
    public void rotateBlockLeft() {
        if (blockManager.rotateBlockLeft(activeBlock)) {
            generateGhostBlock();

            lockTimer = 0;
        }
    }

    public void rotateBlockRight() {
        if (blockManager.rotateBlockRight(activeBlock)) {
            generateGhostBlock();

            lockTimer = 0;
        }
    }

    // =====================
    // Delta
    // =====================
    public void update(long delta) {
        updateBlockGrounded();
        updateGravityThreshold();
        updateGravity(delta);
        updateLockGrace(delta);
    }

    private void updateBlockGrounded() {
        if (!blockManager.canMoveBlockDown(activeBlock)) {
            blockGrounded = true;
            return;
        }

        blockGrounded = false;
    }

    private void updateGravityThreshold() {
        // TODO Replace `hasLineCleared()` with a better mode
        long gravity = Config.getGravity().toNanos();
        long gravityMin = Config.GRAVITY_MIN.toNanos();

        long gravityStep = Duration.ofMillis(20).toNanos();

        if (gravity >= (gravityMin + gravityStep) && scoreTracker.hasLineCleared()) {
            scoreTracker.setLineCleared(false);
            Config.setGravity(Duration.ofNanos(gravity - gravityStep));
        }
    }

    private void updateGravity(long delta) {
        if (blockGrounded) {
            gravityTimer = 0;
            return;
        }

        gravityTimer += delta;

        long gravity = Config.getGravity().toNanos();

        while (gravityTimer >= gravity) {
            gravityTimer -= gravity;
            
            moveBlockDown();
        }
    }

    private void updateLockGrace(long delta) {
        if (!blockGrounded) {
            lockTimer = 0;
            return;
        }

        lockTimer += delta;

        long lockThreshold = 500_000_000; // 0.5 Seconds

        while (lockTimer >= lockThreshold) {
            lockTimer -= lockThreshold;

            moveBlockDown();
        }
    }

    // =====================
    // Utilities
    // =====================
    private void generateActiveBlock() {
        activeBlock = new BlockData(blockQueue.getRandomBlock());

        if (!blockManager.isPositionValid(activeBlock)) {
            Config.setGravity(Config.GravityDef);
            gameOver = true;
        }
    }

    private void generateGhostBlock() {
        ghostBlock = activeBlock.copyBlockData();

        blockManager.dropBlock(ghostBlock);
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

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public Duration getGravity() {
        return Config.getGravity();
    }

    @Override
    public BlockData getActiveBlockCopy() {
        return activeBlock.copyBlockData();
    }

    @Override
    public BlockData getGhostBlockCopy() {
        return ghostBlock.copyBlockData();
    }
}
