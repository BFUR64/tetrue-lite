package com.teic.trueris.game;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.Color;
import com.teic.trueris.game.grid.GridData;
import io.github.bfur64.terminal.interfaces.TerminalBackend;

import java.util.List;

public class GameRenderer {
    private static final char EMPTY = ' ';
    private static final char SOLID = '█';
    private static final char GHOST = '░';

    private final int BUFFER_HEIGHT;
    private final int BUFFER_WIDTH;
    private final int BORDER_THICKNESS = 1;
    private final int BORDER_OFFSET = 2;

    private final TerminalBackend terminal;
    private final GridData gridData;
    private final GameState gameState;

    private RenderCell[][] previousBuffer;
    private RenderCell[][] currentBuffer;

    public GameRenderer(TerminalBackend terminal, GridData gridData, GameState gameState) {
        this.terminal = terminal;
        this.gridData = gridData;
        this.gameState = gameState;

        BUFFER_HEIGHT = terminal.getYSize();
        BUFFER_WIDTH = terminal.getXSize();

        previousBuffer = new RenderCell[BUFFER_HEIGHT][BUFFER_WIDTH];
        currentBuffer = new RenderCell[BUFFER_HEIGHT][BUFFER_WIDTH];
        clearBuffer(previousBuffer);
        clearBuffer(currentBuffer);
    }

    public void update() {
        int gameBorderWidth = Config.gridWidth.get() + BORDER_OFFSET;
        int gameBorderHeight = Config.gridHeight.get() + BORDER_OFFSET;

        // Game & Blocks
        writeBorder(0, 0, gameBorderWidth, gameBorderHeight);
        writeGameCells();

        // Score & Difficulty / Gravity
        int leftPadding = gameBorderWidth + 1;
        writeString(leftPadding, 1, "Score: " + gameState.getScore());
        if (!Config.showGravity.get()) {
            writeString(leftPadding, 3, "Difficulty: " + calculateDifficulty() + "x");
        }
        else {
            writeString(leftPadding, 3, "Gravity: " + gameState.getGravity().toMillis() + " ms");
        }

        // Hold Block
        String holdName = "Hold";
        int holdY = 5;
        writeString(leftPadding, holdY, holdName);
        BlockData blockData = gameState.getHeldBlockCopy();
        if (blockData != null) {
            writeBlock(leftPadding + holdName.length() + 1, holdY, blockData.getCellCopy(), SOLID);
        }

        // Queue
        int textQueueY = 8;
        writeString(leftPadding, textQueueY, "Next");
        writeQueue(leftPadding + 2, textQueueY + 2);

        updateBufferAndPrint();
    }

    private void updateBufferAndPrint() {
        for (int row = 0; row < BUFFER_HEIGHT; row++ ) {
            for (int col = 0; col < BUFFER_WIDTH; col++) {
                if (!previousBuffer[row][col].isEquals(currentBuffer[row][col])) {
                    RenderCell cell = currentBuffer[row][col];
                    draw(col, row, cell);
                }
            }
        }
        terminal.flush();

        previousBuffer = currentBuffer;
        currentBuffer = new RenderCell[BUFFER_HEIGHT][BUFFER_WIDTH];
        clearBuffer(currentBuffer);
    }

    // =====================
    // Border
    // =====================
    private void writeBorder(int x, int y, int xSize, int ySize) {
        for (int row = 0; row < ySize; row++) {
            for (int col = 0; col < xSize; col++) {
                if ((row == 0 || row == ySize - 1 ||
                        col == 0 || col == xSize - 1) && isWithinBuffer(row, col)
                ) {
                    currentBuffer[row + y][col + x] = new RenderCell(SOLID, Color.GREY);
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

                if (!cell.isEmpty() && isWithinBuffer(rowOffset, colOffset)) {
                    currentBuffer[rowOffset][colOffset] = new RenderCell(SOLID, cell.color);
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
    private void writeBlock(int colStart, int rowStart, Cell[][] block, char out) {
        for (int row = 0; row < block.length; row++) {
            for (int col = 0; col < block[0].length; col++) {
                Cell cell = block[row][col];
                int rowOffset = row + rowStart;
                int colOffset = col + colStart;

                if (!cell.isEmpty() && isWithinBuffer(rowOffset, colOffset)) {
                    currentBuffer[row + rowStart][col + colStart] = new RenderCell(out, cell.color);
                }
            }
        }
    }

    private void writeString(int col, int row, String out) {
        char[] charArray = out.toCharArray();

        for (int pointer = 0; pointer < charArray.length; pointer++) {
            if (isWithinBuffer(row, col + pointer)) {
                currentBuffer[row][col + pointer] = new RenderCell(charArray[pointer], Color.WHITE);
                currentBuffer[row][col + pointer].isCharacter = true;
            }
        }
    }

    private boolean isWithinBuffer(int row, int col) {
        return row < BUFFER_HEIGHT && col < BUFFER_WIDTH;
    }

    private double calculateDifficulty() {
        // Assume 500 is 500 ms and is 1x
        return Math.ceil((double) 500 / gameState.getGravity().toMillis() * 100) / 100;
    }

    private void clearBuffer(RenderCell[][] buffer) {
        for (int row = 0; row < BUFFER_HEIGHT; row++) {
            for (int col = 0; col < BUFFER_WIDTH; col++) {
                buffer[row][col] = new RenderCell(EMPTY, Color.DEFAULT);
                buffer[row][col].isEmpty = true;
            }
        }
    }

    // =====================
    // Screen Drawing
    // =====================
    private void draw(int col, int row, RenderCell cell) {
        int colOffset = col * 2;

        if (colOffset > terminal.getXSize() || colOffset + 1 > terminal.getXSize() || row > terminal.getYSize()) {
            return;
        }

        int[] textColor = getTextColor(cell.color);
        terminal.setForegroundColor(textColor[0], textColor[1], textColor[2]);

        char out1 = cell.symbol;
        char out2 = out1;

        if (cell.isCharacter) {
            out2 = ' ';
        }

        terminal.put(colOffset, row, String.valueOf(out1));
        terminal.put(colOffset + 1, row, String.valueOf(out2));

        terminal.resetColorAndStyle();
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

    private static class  RenderCell {
        private final char symbol;
        private final Color color;

        private boolean isEmpty;
        private boolean isCharacter;

        public RenderCell(char symbol, Color color) {
            this.symbol = symbol;
            this.color = color;
        }

        public boolean isEquals(RenderCell renderCell) {
            return this.symbol == renderCell.symbol &&
                this.color == renderCell.color &&
                this.isEmpty == renderCell.isEmpty &&
                this.isCharacter == renderCell.isCharacter;
        }
    }
}
