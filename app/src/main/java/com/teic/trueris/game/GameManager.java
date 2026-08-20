package com.teic.trueris.game;

import java.time.Duration;
import java.util.List;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.block.BlockManager;
import com.teic.trueris.game.block.BlockQueue;
import com.teic.trueris.game.grid.GridData;
import com.teic.trueris.game.grid.GridManager;
import com.teic.trueris.game.utils.ScoreTracker;

public class GameManager implements GameState {
    private final BlockManager blockManager;
    private final GridManager gridManager;
    private final BlockQueue blockQueue;
    private final ScoreTracker scoreTracker;

    private BlockData activeBlock;
    private BlockData ghostBlock;
    private BlockData heldBlock;

    // Game Variables
    private boolean isBlockGrounded;

    private boolean isBlockHeld;

    private long gravityTimer;

    private long lockTimer;

    private boolean gameOver;

    public GameManager(GridData gridData) {
        this.blockManager = new BlockManager(gridData);
        this.gridManager = new GridManager(gridData);
        this.blockQueue = new BlockQueue();
        this.scoreTracker = new ScoreTracker();

        generateActiveBlock();
        generateGhostBlock();
    }

    public void cleanUp() {
        Config.gravity.set(Config.gravityDef);
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
            isBlockHeld = false;

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
        isBlockHeld = false;

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
    // Block Holding
    // =====================
    public void holdBlock() {
        if (!isBlockHeld) {
            switchHoldAndActiveBlocks();
            isBlockHeld = true;
        }
    }

    public void switchHoldAndActiveBlocks() {
        BlockData tempBlock = new BlockData(activeBlock.getCellCopy());
        activeBlock = heldBlock;
        heldBlock = tempBlock;

        if (activeBlock == null) {
            generateActiveBlock();
        }

        generateGhostBlock();
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
            isBlockGrounded = true;
            return;
        }

        isBlockGrounded = false;
    }

    private void updateGravityThreshold() {
        // TODO Replace `hasLineCleared()` with a better mode
        long gravity = Duration.ofMillis(Config.gravity.get()).toNanos();
        long gravityMin = Duration.ofMillis(Config.GRAVITY_MIN).toNanos();

        long gravityStep = Duration.ofMillis(20).toNanos();

        if (gravity >= (gravityMin + gravityStep) && scoreTracker.hasLineCleared()) {
            scoreTracker.setLineCleared(false);
            Config.gravity.set(Math.toIntExact(Duration.ofNanos(gravity - gravityStep).toMillis()));
        }
    }

    private void updateGravity(long delta) {
        if (isBlockGrounded) {
            gravityTimer = 0;
            return;
        }

        gravityTimer += delta;

        long gravity = Duration.ofMillis(Config.gravity.get()).toNanos();

        while (gravityTimer >= gravity) {
            gravityTimer -= gravity;
            
            moveBlockDown();
        }
    }

    private void updateLockGrace(long delta) {
        if (!isBlockGrounded) {
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
        activeBlock = blockQueue.getFirstBlock();

        if (!blockManager.isPositionValid(activeBlock)) {
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
    public List<BlockData> viewBlockQueue() {
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
        return Duration.ofMillis(Config.gravity.get());
    }

    @Override
    public BlockData getActiveBlockCopy() {
        return activeBlock.copyBlockData();
    }

    @Override
    public BlockData getGhostBlockCopy() {
        return ghostBlock.copyBlockData();
    }

    @Override
    public BlockData getHeldBlockCopy() {
        if (heldBlock != null) {
             return heldBlock.copyBlockData();
        }

        return null;
    }
}
