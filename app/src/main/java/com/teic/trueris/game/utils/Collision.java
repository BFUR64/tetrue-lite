package com.teic.trueris.game.utils;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.grid.GridData;

public class Collision {
    private final GridData gridData;

    public Collision(GridData gridData) {
        this.gridData = gridData;
    }

    public boolean isPositionValid(BlockData blockData) {
        Cell[][] block = blockData.getRotatedCellCopy();

        int blockRow = blockData.getBlockRow();
        int blockCol = blockData.getBlockCol();

        return isPositionValid(block, blockRow, blockCol);
    }

    public boolean isPositionValid(Cell[][] block, int blockRow, int blockCol) {
        int blockSize = block.length;

        for (int row = 0; row < blockSize; row++) {
            for (int col = 0; col < blockSize; col++) {
                if (block[row][col].isEmpty()) {
                    continue;
                }

                int gridRow = blockRow + row;
                int gridCol = blockCol + col;

                if (
                    isOutOfBounds(gridRow, gridCol)
                    || isColliding(gridRow, gridCol)
                ) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isOutOfBounds(int gridRow, int gridCol) {
        return (
            gridRow < 0 || gridRow >= Config.gridHeight.get()
            || gridCol < 0 || gridCol >= Config.gridWidth.get()
        );
    }

    private boolean isColliding(int gridRow, int gridCol) {
        return !gridData.getCell(gridRow, gridCol).isEmpty();
    }
}
