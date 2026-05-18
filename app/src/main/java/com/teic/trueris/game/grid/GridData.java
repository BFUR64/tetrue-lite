package com.teic.trueris.game.grid;

import com.teic.trueris.Config;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.CellRegistry;

public class GridData {
    private final Cell[][] grid;

    public GridData() {
        int gridRow = Config.getGridHeight() + Config.SPAWN_BUFFER;
        int gridCol = Config.getGridWidth();

        grid = new Cell[gridRow][gridCol];
        for (int row = 0; row < gridRow; row++) {
            for (int col = 0; col < gridCol; col++) {
                grid[row][col] = CellRegistry.EMPTY;
            }
        }
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    void setCell(Cell cell, int row, int col) {
        grid[row][col] = cell;
    }
}
