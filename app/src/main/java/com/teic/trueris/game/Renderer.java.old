package com.teic.trueris.game;


import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.TextColor.Indexed;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockRegistry;
import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.Color;
import com.teic.trueris.game.grid.GridData;

public class Renderer {
    private final TextGraphics textGraphics;
    private final GridData gridData;
    private final GameState gameState;

    private final int borderHeight;
    private final int borderWidth;

    public Renderer(
        TextGraphics textGraphics, 
        GridData gridData, 
        GameState gameState
    ) {
        this.textGraphics = textGraphics;
        this.gridData = gridData;
        this.gameState = gameState;

        borderHeight = Config.GRID_HEIGHT + 2;
        borderWidth = Config.GRID_WIDTH + 2;
    }

    public void renderBorder() throws IOException {
        for (int row = 0; row < borderHeight; row++) {
            for (int col = 0; col < borderWidth; col++) {
                if (
                    row == 0 || row == borderHeight -1 
                    || col == 0 || col == borderWidth - 1 
                    ) {
                    drawTile(
                        col, 
                        row, 
                        "" + Symbols.BLOCK_SOLID, 
                        Color.GREY
                    );
                    continue;
                }
            }
        }
    }

    public void updateScreen() {
        updateGrid();

        drawString(Config.GRID_WIDTH + 3, 1, "Score: " + gameState.getScore());
        
        updateQueue();
    }

    private void updateGrid() {
        clearBorderContents();

        for (int row = 0; row < Config.GRID_HEIGHT; row++) {
            for (int col = 0; col < Config.GRID_WIDTH; col++) {
                Cell cell = gridData.getSolidCell(row + Config.SPAWN_BUFFER, col);
                if (!cell.isEmpty()) {
                    drawTile(col + 1, row + 1, "" + Symbols.BLOCK_SOLID, cell.color);
                }

                cell = gridData.getGhostCell(row + Config.SPAWN_BUFFER, col);
                if (!cell.isEmpty()) {
                    drawTile(col + 1, row + 1, "" + Symbols.BLOCK_SPARSE, cell.color);
                }

                cell = gridData.getActiveCell(row + Config.SPAWN_BUFFER, col);
                if (!cell.isEmpty()) {
                    drawTile(col + 1, row + 1, "" + Symbols.BLOCK_SOLID, cell.color);
                }
            }
        }
    }

    private void clearBorderContents() {
        for (int row = 0; row < Config.GRID_HEIGHT; row++) {
            for (int col = 0; col < Config.GRID_WIDTH; col++) {
                drawTile(col + 1, row + 1, " ", Color.DEFAULT);
            }
        }
    }

    private void updateQueue() {
        clearArea(
            Config.GRID_WIDTH + 3, 
            Config.GRID_WIDTH + 7, 
            3, 17
        );

        List<BlockRegistry.BlockTemplate> blocks = gameState.viewBlockQueue();

        int rowPointer = 3;
        int counter = 0;

        for (BlockRegistry.BlockTemplate block : blocks) {
            drawBlock(
                Config.GRID_WIDTH + 3, 
                rowPointer, 
                block.copyBlock()
            );

            if (counter >= 2) {
                break;
            }

            rowPointer += 5;
            counter++;
        }
    }

    private void clearArea(int colStart, int colEnd, int rowStart, int rowEnd) {
        for (int row = rowStart; row < rowEnd; row++) {
            for (int col = colStart; col < colEnd; col++) {
                drawTile(col, row, " ", Color.DEFAULT);
            }
        }
    }

    private void drawBlock(int colStart, int rowStart, Cell[][] block) {
        for (int row = 0; row < block.length; row++) {
            for (int col = 0; col < block[0].length; col++) {
                Cell cell = block[row][col];

                if (cell.isEmpty()) {
                    continue;
                }

                drawTile(col + colStart, row + rowStart, "" + Symbols.BLOCK_SOLID, cell.color);
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
    
    private void drawTile(
        int col, 
        int row, 
        String out, 
        Color color
    ) {
        textGraphics.setForegroundColor(getTextColor(color));
        
        textGraphics.putString(col * 2, row, out);
        textGraphics.putString(col * 2 + 1, row, out);
        
        textGraphics.setForegroundColor(getTextColor(Color.DEFAULT));
    }

    private void drawString(
        int col, 
        int row, 
        String out
    ) {
        textGraphics.putString(col * 2, row, out);
    }
}
