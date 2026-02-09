package com.teic.trueris.game.block;

import com.teic.trueris.game.Collision;
import com.teic.trueris.game.grid.GridData;

public class BlockManager {
    private final Collision collision;

    public BlockManager(GridData gridData) {
        collision = new Collision(gridData);
    }

    // =====================
    // Movement
    // =====================
    public boolean moveBlockDown(BlockData blockData) {
        blockData.moveDown();

        boolean isPositionValid = collision.isPositionValid(blockData);

        if (!isPositionValid) {
            blockData.revertRowPosition();

            return false;
        }

        return true;
    }

    public boolean canMoveBlockDown(BlockData blockData) {
        blockData.moveDown();

        boolean collisionResult = collision.isPositionValid(blockData);
        
        blockData.revertRowPosition();

        return collisionResult;
    }

    public void dropBlock(BlockData blockData) {
        while (moveBlockDown(blockData)) {}
    }

    public boolean moveBlockLeft(BlockData blockData) {
        blockData.moveLeft();

        boolean isPositionValid = collision.isPositionValid(blockData);

        if (!isPositionValid) {
            blockData.revertColPosition();

            return false;
        }

        return true;
    }

    public boolean moveBlockRight(BlockData blockData) {
        blockData.moveRight();

        boolean isPositionValid = collision.isPositionValid(blockData);

        if (!isPositionValid) {
            blockData.revertColPosition();

            return false;
        }

        return true;
    }

    // =====================
    // Rotation
    // =====================
    public boolean rotateBlockLeft(BlockData blockData) {
        blockData.rotateLeft();

        boolean isPositionValid = collision.isPositionValid(blockData);

        if (!isPositionValid) {
            blockData.revertBlockRotation();

            return false;
        }

        return true;
    }

    public boolean rotateBlockRight(BlockData blockData) {
        blockData.rotateRight();

        boolean isPositionValid = collision.isPositionValid(blockData);

        if (!isPositionValid) {
            blockData.revertBlockRotation();

            return false;
        }

        return true;
    }
}
