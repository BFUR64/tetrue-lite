package com.teic.trueris.game;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.block.BlockRegistry.BlockTemplate;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.Color;
import com.teic.trueris.game.grid.GridData;
import io.github.bfur64.terminal.Terminal;

import java.util.List;

public class GameRenderer {
    private final int BUFFER_HEIGHT = 22;
    private final int BUFFER_WIDTH = 30;

    private final int BORDER_SIZE = 1;

    private final Terminal terminal;
    private final GridData gridData;
    private final GameState gameState;

    private RenderCell[][] previousBuffer;
    private RenderCell[][] currentBuffer;

    public GameRenderer(
        Terminal terminal,
        GridData gridData,
        GameState gameState
    ) {
        this.terminal = terminal;
        this.gridData = gridData;
        this.gameState = gameState;

        this.previousBuffer = new RenderCell[BUFFER_HEIGHT][BUFFER_WIDTH];
        this.currentBuffer = new RenderCell[BUFFER_HEIGHT][BUFFER_WIDTH];

        clearBuffer(previousBuffer);
        clearBuffer(currentBuffer);
    }

    public void update() {
        writeBorder();
        writeBlocks();
        writeString(Config.GRID_WIDTH + 3, 1, "Score: " + gameState.getScore());
        writeString(Config.GRID_WIDTH + 3, 3, "Difficulty: " + calculateDifficulty() + "x");
        writeBlockQueue();

        updateScreen();
    }

    private double calculateDifficulty() {
        // Assume 500_000_000 is 0.5 Seconds and is 1x
        return Math.ceil((double) 500_000_000 / gameState.getGravityThreshold() * 100) / 100;
    }

    private void updateScreen() {
        for (int row = 0; row < BUFFER_HEIGHT; row++) {
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

    private void clearBuffer(RenderCell[][] buffer) {
        for (int row = 0; row < BUFFER_HEIGHT; row++) {
            for (int col = 0; col < BUFFER_WIDTH; col++) {
                buffer[row][col] = new RenderCell('\0', Color.DEFAULT);
                buffer[row][col].isEmpty = true;
            }
        }
    }

    private void writeBorder() {
        int GAME_HEIGHT = Config.GRID_HEIGHT + (BORDER_SIZE * 2);
        int GAME_WIDTH = Config.GRID_WIDTH + (BORDER_SIZE * 2);

        for (int row = 0; row < GAME_HEIGHT; row++) {
            for (int col = 0; col < GAME_WIDTH; col++) {
                if (
                    row == 0 || row == GAME_HEIGHT - 1 ||
                    col == 0 || col == GAME_WIDTH - 1
                ) {
                    currentBuffer[row][col] = new RenderCell('█', Color.GREY);
                }
            }
        }
    }

    private void writeBlocks() {
        for (int row = 0; row < Config.GRID_HEIGHT; row++) {
            for (int col = 0; col < Config.GRID_WIDTH; col++) {
                Cell cell = gridData.getCell(row + Config.SPAWN_BUFFER, col);
                if (!cell.isEmpty()) {
                    currentBuffer[row + BORDER_SIZE][col + BORDER_SIZE] = new RenderCell('█', cell.color);
                }
            }
        }

        writeBlock(gameState.getGhostBlockCopy(), '░');
        writeBlock(gameState.getActiveBlockCopy(), '█');
    }

    private void writeBlock(BlockData blockData, char out) {
        Cell[][] block = blockData.getRotatedBlockCopy();

        for (int row = 0; row < blockData.blockSize(); row++) {
            for (int col = 0; col < blockData.blockSize(); col++) {
                Cell cell = block[row][col];

                int rowOffset = row + blockData.blockRow() + BORDER_SIZE - Config.SPAWN_BUFFER;
                int colOffset = col + blockData.blockCol() + BORDER_SIZE;

                if (rowOffset >= BORDER_SIZE && colOffset >= BORDER_SIZE &&
                    rowOffset < BORDER_SIZE + Config.GRID_HEIGHT &&
                    colOffset < BORDER_SIZE + Config.GRID_WIDTH && !cell.isEmpty()
                ) {
                    currentBuffer[rowOffset][colOffset] = new RenderCell(out, cell.color);
                }
            }
        }
    }

    private void writeString(int col, int row, String out) {
        char[] charArray = out.toCharArray();

        for (int pointer = 0; pointer < charArray.length; pointer++) {
            currentBuffer[row][col + pointer] = new RenderCell(charArray[pointer], Color.WHITE);
            currentBuffer[row][col + pointer].isCharacter = true;
        }
    }

    private void writeBlockQueue() {
        List<BlockTemplate> blocks = gameState.viewBlockQueue();

        int rowPointer = 6;
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

                currentBuffer[row + rowStart][col + colStart] = new RenderCell('█', cell.color);
            }
        }
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

    private void draw(int col, int row, RenderCell cell) {
        int[] textColor = getTextColor(cell.color);

        terminal.setForegroundColor(textColor[0], textColor[1], textColor[2]);

		String out = cell.isEmpty ? " " : "" + cell.symbol;

		terminal.putString(col * 2, row, out);

		if (cell.isEmpty || !cell.isCharacter) {
			terminal.putString(col * 2 + 1, row, out);
		}

        textColor = getTextColor(Color.WHITE);

        terminal.setForegroundColor(textColor[0], textColor[1], textColor[2]);
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
