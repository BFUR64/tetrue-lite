package com.teic.trueris.game;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.Indexed;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockRegistry.BlockTemplate;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.Color;
import com.teic.trueris.game.grid.GridData;

import java.util.InputMismatchException;
import java.util.List;

public class Renderer {
    // Calculations
    // Grid Height (20) + Border Weight
    private final int BUFFER_HEIGHT = 22;
    private final int BUFFER_WIDTH = 12;

    private final int BORDER_SIZE = 1;
    private final int GAME_HEIGHT = Config.GRID_HEIGHT + (BORDER_SIZE * 2);
    private final int GAME_WIDTH = Config.GRID_WIDTH + (BORDER_SIZE * 2);

    private final TextGraphics textGraphics;
    private final GridData gridData;
    private final GameState gameState;

    // Previous Buffer
    // Current Buffer

    // Write to Current Buffer
    // Diff check on previous then write the difference?
    // Use Current Buffer and its variables to print, disregard previous buffer variables (e.g. Block)?

    // Then... Prev = Curr;
    private RenderCell[][] previousBuffer;
    private RenderCell[][] currentBuffer;

    public Renderer(
        TextGraphics textGraphics,
        GridData gridData,
        GameState gameState
    ) {
        this.textGraphics = textGraphics;
        this.gridData = gridData;
        this.gameState = gameState;

        this.previousBuffer = new RenderCell[BUFFER_HEIGHT][BUFFER_WIDTH];
        this.currentBuffer = new RenderCell[BUFFER_WIDTH][BUFFER_WIDTH];

        clearBuffer(previousBuffer);
        clearBuffer(currentBuffer);
    }

    public void update() {
        writeBorder();
        writeBlocks();
        writeString(Config.GRID_WIDTH + 3, 1, "Score: " + gameState.getScore());
        writeBlockQueue();

        updateScreen();
    }

    private void updateScreen() {
        for (int row = 0; row < BUFFER_HEIGHT; row++) {
            for (int col = 0; col < BUFFER_WIDTH; col++) {
                if (!previousBuffer[row][col].isEquals(currentBuffer[row][col])) {
                    RenderCell cell = currentBuffer[row][col];
                    draw(col, row, "" + cell.symbol, cell.color, cell.isCharacter);
                }
            }
        }

        previousBuffer = currentBuffer;
        currentBuffer = new RenderCell[BUFFER_HEIGHT][BUFFER_WIDTH];
        clearBuffer(currentBuffer);
    }

    private void clearBuffer(RenderCell[][] buffer) {
        for (int row = 0; row < BUFFER_HEIGHT; row++) {
            for (int col = 0; col < BUFFER_WIDTH; col++) {
                buffer[row][col] = new RenderCell('\0', Color.DEFAULT);
                buffer[row][col].isEmpty = true;
            }
        }
    }

    private void writeBorder() {
        for (int row = 0; row < GAME_HEIGHT; row++) {
            for (int col = 0; col < GAME_WIDTH; col++) {
                if (
                    row == 0 || row == GAME_HEIGHT - 1 ||
                    col == 0 || col == GAME_WIDTH - 1
                ) {
                    currentBuffer[row][col] = new RenderCell(Symbols.BLOCK_SOLID, Color.GREY);
                }
            }
        }
    }

    private void writeBlocks() {
        for (int row = 0; row < Config.GRID_HEIGHT; row++) {
            for (int col = 0; col < Config.GRID_WIDTH; col++) {
                Cell cell = gridData.getSolidCell(row + Config.SPAWN_BUFFER, col);
                if (!cell.isEmpty()) {
                    currentBuffer[row + BORDER_SIZE][col + BORDER_SIZE] = new RenderCell(Symbols.BLOCK_SOLID, cell.color);
                }

                cell = gridData.getGhostCell(row + Config.SPAWN_BUFFER, col);
                if (!cell.isEmpty()) {
                    currentBuffer[row + BORDER_SIZE][col + BORDER_SIZE] = new RenderCell(Symbols.BLOCK_SPARSE, cell.color);
                }

                cell = gridData.getActiveCell(row + Config.SPAWN_BUFFER, col);
                if (!cell.isEmpty()) {
                    currentBuffer[row + BORDER_SIZE][col + BORDER_SIZE] = new RenderCell(Symbols.BLOCK_SOLID, cell.color);
                }
            }
        }
    }

    private void writeString(int col, int row, String out) {
        char[] charArray = out.toCharArray();

        for (int pointer = 0; pointer < charArray.length; pointer++) {
            currentBuffer[row][col + pointer] = new RenderCell(charArray[pointer], Color.DEFAULT);
            currentBuffer[row][col + pointer].isCharacter = true;
        }
    }

    private void writeBlockQueue() {
        List<BlockTemplate> blocks = gameState.viewBlockQueue();

        int rowPointer = 3;
        int counter = 0;

        for (BlockTemplate block : blocks) {
            Cell[][] blockArr = block.copyBlock();

            writeBlock(Config.GRID_WIDTH + 3, rowPointer, blockArr);

            if (counter >= 2 ) {
                break;
            }

            rowPointer += blockArr.length + 1;
            counter++;
        }
    }

    private void writeBlock(int colStart, int rowStart, Cell[][] block) {
        for (int row = 0; row < block.length; row++) {
            for (int col = 0; col < block[0].length; col++) {
                Cell cell = block[row][col];

                if (cell.isEmpty()) {
                    continue;
                }

                currentBuffer[row + rowStart][col + colStart] = new RenderCell(Symbols.BLOCK_SOLID, cell.color);
            }
        }
    }

    private TextColor getTextColor(Color color) {
        return switch (color) {
            case DEFAULT -> ANSI.DEFAULT;
            case GREY -> Indexed.fromRGB(96, 96, 96);
            case YELLOW -> Indexed.fromRGB(205, 205, 0);
            case BLUE -> Indexed.fromRGB(0, 0, 205);
            case ORANGE -> Indexed.fromRGB(205, 102, 0);
            case GREEN -> Indexed.fromRGB(0, 205, 0);
            case RED -> Indexed.fromRGB(205, 0, 0);
            case PURPLE -> Indexed.fromRGB(154, 0, 205);
            case CYAN -> Indexed.fromRGB(0, 205, 205);
            case WHITE -> ANSI.WHITE;
            default -> throw new InputMismatchException("Undefined color");
        };
    }

    private void draw(int col, int row, String out, Color color, boolean isCharacter) {
        textGraphics.setForegroundColor(getTextColor(color));

        textGraphics.putString(col * 2, row, out);
        if (!isCharacter) textGraphics.putString(col * 2 + 1, row, out);

        textGraphics.setForegroundColor(getTextColor(Color.DEFAULT));
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
            return this.symbol == renderCell.symbol |
                    this.color == renderCell.color |
                    this.isEmpty == renderCell.isEmpty |
                    this.isCharacter == renderCell.isCharacter;
        }
    }
}
