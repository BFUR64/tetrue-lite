package com.teic.trueris.game.utils;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.grid.GridData;

public class Collision {
    private final GridData gridData;
    private final int cachedWidth;
    private final int cachedHeight;

    public Collision(GridData gridData) {
        this.gridData = gridData;
        this.cachedHeight = Config.gridHeight.get();
        this.cachedWidth = Config.gridWidth.get();
    }

    public boolean isPositionValid(BlockData blockData) {
        return isPositionValidVirtual(
                blockData.getRawBlock(),
                blockData.rotation().ordinal(),
                blockData.getBlockRow(),
                blockData.getBlockCol()
        );
    }

    public boolean isPositionValid(Cell[][] rawBlock, int targetRotationId, int blockRow, int blockCol) {
        return isPositionValidVirtual(rawBlock, targetRotationId, blockRow, blockCol);
    }

    private boolean isPositionValidVirtual(Cell[][] block, int rotation, int blockRow, int blockCol) {
        int size = block.length;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int origRow = row;
                int origCol = col;

                for (int i = 0; i < rotation; i++) {
                    int tempRow = origRow;
                    origRow = size - 1 - origCol;
                    origCol = tempRow;
                }

                if (block[origRow][origCol].isEmpty()) {
                    continue;
                }

                int gridRow = blockRow + row;
                int gridCol = blockCol + col;

                if (gridRow < 0 || gridRow >= cachedHeight || gridCol < 0 || gridCol >= cachedWidth) {
                    return false;
                }

                if (!gridData.getCell(gridRow, gridCol).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
