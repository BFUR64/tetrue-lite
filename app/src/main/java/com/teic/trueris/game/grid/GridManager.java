package com.teic.trueris.game.grid;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.CellRegistry;

public class GridManager {
    private final GridData gridData;

    public GridManager(GridData gridData) {
        this.gridData = gridData;
    }

    public void writeGrid(GridType gridType, BlockData blockData) {
        Cell[][] block = blockData.getRotatedBlockCopy();

        int blockRow = blockData.blockRow();
        int blockCol = blockData.blockCol();

        int blockSize = blockData.blockSize();

        for (int row = 0; row < blockSize; row++) {
            for (int col = 0; col < blockSize; col++) {
                Cell cell = block[row][col];

                if (cell.isEmpty()) {
                    continue;
                }

                switch (gridType) {
                    case SOLID -> { gridData.setSolidCell(cell, row + blockRow, col + blockCol); }
                    case ACTIVE -> { gridData.setActiveCell(cell, row + blockRow, col + blockCol); }
                    case GHOST -> { gridData.setGhostCell(cell, row + blockRow, col + blockCol); }
                }
            }
        }
    }

    public void eraseGrid(GridType gridType) {
        int gridRow = Config.GRID_HEIGHT + Config.SPAWN_BUFFER;
        int gridCol = Config.GRID_WIDTH;

        for (int row = 0; row < gridRow; row++) {
            for (int col = 0; col < gridCol; col++) {
                switch (gridType) {
                    case SOLID -> { gridData.setSolidCell(CellRegistry.EMPTY, row, col); }
                    case ACTIVE -> { gridData.setActiveCell(CellRegistry.EMPTY, row, col); }
                    case GHOST -> { gridData.setGhostCell(CellRegistry.EMPTY, row, col); }
                }
            }
        }
    }

    public boolean[] clearFilledRows() {
        boolean[] filledRows = returnFilledRows();

        clearFilledRows(filledRows);

        return filledRows;
    }

    private void clearFilledRows(boolean[] filledRows) {
        boolean hasFilled = false;
        for (int row = 0; row < filledRows.length; row++) {
            if (!filledRows[row]) continue;

            hasFilled = true;
            shiftSolidGridRowFrom(row);
        }

        if (hasFilled) clearFirstRow();
    }

    private boolean[] returnFilledRows() {
        int totalGridRow = Config.GRID_HEIGHT + Config.SPAWN_BUFFER;
        boolean[] filledRows = new boolean[totalGridRow];

        for (int row = 0; row < totalGridRow; row++) {
            boolean isEmpty = false;
            for (int col = 0; col < Config.GRID_WIDTH; col++) {
                Cell cell = gridData.getSolidCell(row, col);

                if (!cell.isEmpty()) continue;

                isEmpty = true;
                break;
            }

            if (isEmpty) continue;

            filledRows[row] = true;
        }

        return filledRows;
    }

    private void shiftSolidGridRowFrom(int rowStart) {
        for (int row = rowStart; row > 0; row--) {
            for (int col = 0; col < Config.GRID_WIDTH; col++) {
                Cell cell = gridData.getSolidCell(row - 1, col);
                gridData.setSolidCell(cell, row, col);
            }
        }
    }

    private void clearFirstRow() {
        for (int col = 0; col < Config.GRID_WIDTH; col++) {
            gridData.setSolidCell(CellRegistry.EMPTY, 0, col);
        }
    }
}
