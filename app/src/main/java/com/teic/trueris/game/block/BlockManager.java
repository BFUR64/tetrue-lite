package com.teic.trueris.game.block;

import com.teic.trueris.game.utils.Collision;
import com.teic.trueris.game.utils.RotationResult;
import com.teic.trueris.game.utils.RotationSystem;
import com.teic.trueris.game.grid.GridData;

public class BlockManager {
    private final Collision collision;
    private final RotationSystem rotationSystem;

    public BlockManager(GridData gridData) {
        collision = new Collision(gridData);
        rotationSystem = new RotationSystem(collision);
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

    @SuppressWarnings("StatementWithEmptyBody")
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
        RotationResult rotationResult = rotationSystem.computeRotation(blockData, false);

        rotateBlock(blockData, rotationResult);

        return rotationResult.success();
    }

    public boolean rotateBlockRight(BlockData blockData) {
        RotationResult rotationResult = rotationSystem.computeRotation(blockData, true);

        rotateBlock(blockData, rotationResult);

        return rotationResult.success();
    }

    private void rotateBlock(BlockData blockData, RotationResult rotationResult) {
        if (rotationResult.success()) {
            blockData.setRotation(rotationResult.newRotation());
            blockData.setBlockRow(blockData.getBlockRow() + rotationResult.rowOffset());
            blockData.setBlockCol(blockData.getBlockCol() + rotationResult.colOffset());
        }
    }
    
    // =====================
    // Utilities
    // =====================
    public boolean canMoveBlockDown(BlockData blockData) {
        blockData.moveDown();

        boolean collisionResult = collision.isPositionValid(blockData);
        
        blockData.revertRowPosition();

        return collisionResult;
    }

    public boolean isPositionValid(BlockData blockData) {
        return collision.isPositionValid(blockData);
    }
}
