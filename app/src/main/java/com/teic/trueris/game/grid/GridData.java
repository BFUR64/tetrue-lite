package com.teic.trueris.game.grid;

import com.teic.trueris.Config;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.CellRegistry;

public class GridData {
    private final Cell[][] solidGrid;
    private final Cell[][] activeGrid;
    private final Cell[][] ghostGrid;

    private final int gridRow;
    private final int gridCol;

    public GridData() {
        gridRow = Config.GRID_HEIGHT + Config.SPAWN_BUFFER;
        gridCol = Config.GRID_WIDTH;

        solidGrid = new Cell[gridRow][gridCol];
        activeGrid = new Cell[gridRow][gridCol];
        ghostGrid = new Cell[gridRow][gridCol];

        for (int row = 0; row < gridRow; row++) {
            for (int col = 0; col < gridCol; col++) {
                solidGrid[row][col] = CellRegistry.EMPTY;
                activeGrid[row][col] = CellRegistry.EMPTY;
                ghostGrid[row][col] = CellRegistry.EMPTY;
            }
        }
    }

    public Cell getSolidCell(int row, int col) {
        return solidGrid[row][col];
    }

    public Cell getActiveCell(int row, int col) {
        return activeGrid[row][col];
    }

    public Cell getGhostCell(int row, int col) {
        return ghostGrid[row][col];
    }

    void setSolidCell(Cell cell, int row, int col) {
        solidGrid[row][col] = cell;
    }

    void setActiveCell(Cell cell, int row, int col) {
        activeGrid[row][col] = cell;
    }

    void setGhostCell(Cell cell, int row, int col) {
        ghostGrid[row][col] = cell;
    }
}
