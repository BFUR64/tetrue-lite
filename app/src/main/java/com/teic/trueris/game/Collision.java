package com.teic.trueris.game;

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
        Cell[][] block = blockData.getRotatedBlockCopy();

        int blockSize = block.length;
        int blockRow = blockData.blockRow();
        int blockCol = blockData.blockCol();

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
            gridRow < 0 || gridRow >= Config.GRID_HEIGHT + Config.SPAWN_BUFFER
            || gridCol < 0 || gridCol >= Config.GRID_WIDTH
        );
    }

    private boolean isColliding(int gridRow, int gridCol) {
        return !gridData.getSolidCell(gridRow, gridCol).isEmpty();
    }
}
