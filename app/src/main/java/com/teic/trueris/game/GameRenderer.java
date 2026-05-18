package com.teic.trueris.game;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.block.BlockRegistry;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.Color;
import com.teic.trueris.game.grid.GridData;
import io.github.bfur64.terminal.Terminal;

import java.util.List;

public class GameRenderer {
    private static final char EMPTY = ' ';
    private static final char SOLID = '█';
    private static final char GHOST = '░';

    private final int BUFFER_HEIGHT;
    private final int BUFFER_WIDTH;
    private final int BORDER_THICKNESS = 1;
    private final int BORDER_OFFSET = 2;

    private final Terminal terminal;
    private final GridData gridData;
    private final GameState gameState;

    private RenderCell[][] previousBuffer;
    private RenderCell[][] currentBuffer;

    public GameRenderer(Terminal terminal, GridData gridData, GameState gameState) {
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
        writeBorder();
        writeGameCells();

        int leftPadding = Config.getGridWidth() + BORDER_OFFSET + 1;
        writeString(leftPadding, 1, "Score: " + gameState.getScore());
        writeString(leftPadding, 3, "Difficulty: " + calculateDifficulty() + "x");

        writeQueue(5);

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
    private void writeBorder() {
        int gameHeight = Config.getGridHeight() + BORDER_OFFSET;
        int gameWidth = Config.getGridWidth() + BORDER_OFFSET;

        for (int row = 0; row < gameHeight; row++) {
            for (int col = 0; col < gameWidth; col++) {
                if ((row == 0 || row == gameHeight - 1 ||
                        col == 0 || col == gameWidth - 1) && isWithinBuffer(row, col)
                ) {
                    currentBuffer[row][col] = new RenderCell(SOLID, Color.GREY);
                }
            }
        }
    }

    // =====================
    // Game Cells
    // =====================
    private void writeGameCells() {
        writeLockedCells();
        writeQueueBlock(gameState.getGhostBlockCopy(), GHOST);
        writeQueueBlock(gameState.getActiveBlockCopy(), SOLID);
    }

    private void writeLockedCells() {
        for (int row = 0; row < Config.getGridHeight(); row++) {
            for (int col = 0; col < Config.getGridWidth(); col++) {
                Cell cell = gridData.getCell(row + Config.SPAWN_BUFFER, col);
                int rowOffset = row + BORDER_THICKNESS;
                int colOffset = col + BORDER_THICKNESS;

                if (!cell.isEmpty() && isWithinBuffer(rowOffset, colOffset)) {
                    currentBuffer[rowOffset][colOffset] = new RenderCell(SOLID, cell.color);
                }
            }
        }
    }

    private void writeQueueBlock(BlockData blockData, char out) {
        Cell[][] cellBlock = blockData.getRotatedBlockCopy();

        for (int row = 0; row < blockData.blockSize(); row++) {
            for (int col = 0; col < blockData.blockSize(); col++) {
                Cell cell = cellBlock[row][col];
                int rowOffset = row + blockData.blockRow() + BORDER_THICKNESS - Config.SPAWN_BUFFER;
                int colOffset = col + blockData.blockCol() + BORDER_THICKNESS;

                if (!cell.isEmpty() && isWithinGameBorder(rowOffset, colOffset) && isWithinBuffer(rowOffset, colOffset)) {
                    currentBuffer[rowOffset][colOffset] = new RenderCell(out, cell.color);
                }
            }
        }
    }

    // =====================
    // Block Queue
    // =====================
    private void writeQueue(int rowPointer) {
        List<BlockRegistry.BlockTemplate> blocks = gameState.viewBlockQueue();

        int blocksShown = 3;

        for (int counter = 0; counter < blocksShown; counter++) {
            Cell[][] cellBlock = blocks.get(counter).copyBlock();

            int leftPadding = BORDER_OFFSET + 1;
            writeQueueBlock(Config.getGridWidth() + leftPadding, rowPointer, cellBlock);

            int topPadding = 1;
            rowPointer += cellBlock.length + topPadding;
        }
    }

    private void writeQueueBlock(int colStart, int rowStart, Cell[][] block) {
        for (int row = 0; row < block.length; row++) {
            for (int col = 0; col < block[0].length; col++) {
                Cell cell = block[row][col];
                int rowOffset = row + rowStart;
                int colOffset = col + colStart;

                if (!cell.isEmpty() && isWithinBuffer(rowOffset, colOffset)) {
                    currentBuffer[row + rowStart][col + colStart] = new RenderCell(SOLID, cell.color);
                }
            }
        }
    }

    // =====================
    // Helpers
    // =====================
    private boolean isWithinGameBorder(int row, int col) {
        return row >= BORDER_THICKNESS && col >= BORDER_THICKNESS &&
                row < BORDER_THICKNESS + Config.getGridHeight() &&
                col < BORDER_THICKNESS + Config.getGridWidth();
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

        terminal.putString(colOffset, row, String.valueOf(out1));
        terminal.putString(colOffset + 1, row, String.valueOf(out2));

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

    private static class RenderCell {
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
