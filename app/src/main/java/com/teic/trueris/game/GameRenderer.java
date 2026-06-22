package com.teic.trueris.game;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.Color;
import com.teic.trueris.game.grid.GridData;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.output.TextColor;

import java.util.List;

public class GameRenderer {
    private static final String SOLID = "█";
    private static final String GHOST = "░";

    private final int BORDER_THICKNESS = 1;
    private final int BORDER_OFFSET = 2;

    private final Terminal terminal;
    private final GridData gridData;
    private final GameState gameState;

    public GameRenderer(Terminal terminal, GridData gridData, GameState gameState) {
        this.terminal = terminal;
        this.gridData = gridData;
        this.gameState = gameState;
    }

    public void update(long delta) {
        terminal.clear();

        int gameBorderWidth = Config.gridWidth.get() + BORDER_OFFSET;
        int gameBorderHeight = Config.gridHeight.get() + BORDER_OFFSET;

        // Game & Blocks
        writeBorder(0, 0, gameBorderWidth, gameBorderHeight);
        writeGameCells();

        terminal.put(0, 23, String.valueOf(Math.round(1_000_000_000.0d / delta)));

        // Score & Difficulty / Gravity
        int leftPadding = gameBorderWidth + 1;
        putString(leftPadding, 1, "Score: " + gameState.getScore());
        if (!Config.showGravity.get()) {
            putString(leftPadding, 3, "Difficulty: " + calculateDifficulty() + "x");
        }
        else {
            putString(leftPadding, 3, "Gravity: " + gameState.getGravity().toMillis() + " ms");
        }

        // Hold Block
        String holdName = "Hold";
        int holdY = 5;
        putString(leftPadding, holdY, holdName);
        BlockData blockData = gameState.getHeldBlockCopy();
        if (blockData != null) {
            writeBlock(leftPadding + holdName.length() + 1, holdY, blockData.getCellCopy(), SOLID);
        }

        // Queue
        int textQueueY = 8;
        putString(leftPadding, textQueueY, "Next");
        writeQueue(leftPadding + 2, textQueueY + 2);

        terminal.flush();
    }

    // =====================
    // Border
    // =====================
    private void writeBorder(int x, int y, int xSize, int ySize) {
        for (int row = 0; row < ySize; row++) {
            for (int col = 0; col < xSize; col++) {
                if ((row == 0 || row == ySize - 1 ||
                        col == 0 || col == xSize - 1)
                ) {
                    putCell(col + x, row + y, SOLID, Color.GREY);
                }
            }
        }
    }

    // =====================
    // Game Cells
    // =====================
    private void writeGameCells() {
        writeLockedCells();

        BlockData ghostBlock = gameState.getGhostBlockCopy();
        int colShift = ghostBlock.getBlockCol() + BORDER_THICKNESS;
        int rowShift = ghostBlock.getBlockRow() + BORDER_THICKNESS;
        writeBlock(colShift, rowShift, ghostBlock.getRotatedCellCopy(), GHOST);

        BlockData activeBlock = gameState.getActiveBlockCopy();
        colShift = activeBlock.getBlockCol() + BORDER_THICKNESS;
        rowShift = activeBlock.getBlockRow() + BORDER_THICKNESS;
        writeBlock(colShift, rowShift, activeBlock.getRotatedCellCopy(), SOLID);
    }

    private void writeLockedCells() {
        for (int row = 0; row < Config.gridHeight.get(); row++) {
            for (int col = 0; col < Config.gridWidth.get(); col++) {
                Cell cell = gridData.getCell(row, col);
                int rowOffset = row + BORDER_THICKNESS;
                int colOffset = col + BORDER_THICKNESS;

                if (!cell.isEmpty()) {
                    putCell(colOffset, rowOffset,SOLID, cell.color);
                }
            }
        }
    }

    // =====================
    // Block Queue
    // =====================
    private void writeQueue(int x, int rowPointer) {
        List<BlockData> blocks = gameState.viewBlockQueue();

        int blocksShown = 3;

        for (int counter = 0; counter < blocksShown; counter++) {
            Cell[][] cellBlock = blocks.get(counter).getRotatedCellCopy();

            writeBlock(x, rowPointer, cellBlock, SOLID);

            int topPadding = 1;
            rowPointer += cellBlock.length + topPadding;
        }
    }

    // =====================
    // Helpers
    // =====================
    private void writeBlock(int colStart, int rowStart, Cell[][] block, String out) {
        for (int row = 0; row < block.length; row++) {
            for (int col = 0; col < block[0].length; col++) {
                Cell cell = block[row][col];
                if (!cell.isEmpty()) {
                    putCell(col + colStart, row + rowStart, out, cell.color);
                }
            }
        }
    }

    private double calculateDifficulty() {
        // Assume 500 is 500 ms and is 1x
        return Math.ceil((double) 500 / gameState.getGravity().toMillis() * 100) / 100;
    }

    // =====================
    // Screen Drawing
    // =====================
    private void putCell(int col, int row, String out, Color color) {
        int colOffset = col * 2;

        int[] textColor = getTextColor(color);
        terminal.setFg(textColor[0], textColor[1], textColor[2]);

        terminal.put(colOffset, row, out);
        terminal.put(colOffset + 1, row, out);

        terminal.reset();
    }

    private void putString(int col, int row, String out) {
        int colOffset = col * 2;

        char[] charArray = out.toCharArray();

        terminal.setFg(TextColor.WHITE);

        for (int pointer = 0; pointer < charArray.length; pointer++) {
            terminal.put(colOffset + pointer * 2, row, String.valueOf(charArray[pointer]));
        }

        terminal.reset();
    }

    private int[] getTextColor(Color color) {
        return switch (color) {
            case DEFAULT -> new int[]{0, 0, 0};
            case GREY -> new int[]{96, 96, 96};
            case YELLOW -> new int[]{205, 205, 0};
            case BLUE -> new int[]{0, 0, 205};
            case ORANGE -> new int[]{205, 102, 0};
            case GREEN -> new int[]{0, 205, 0};
            case RED -> new int[]{205, 0, 0};
            case PURPLE -> new int[]{154, 0, 205};
            case CYAN -> new int[]{0, 205, 205};
            case WHITE -> new int[]{255, 255, 255};
        };
    }
}
