package com.teic.trueris.game.block;

import com.teic.trueris.Config;
import com.teic.trueris.game.cell.Cell;

public class BlockData {
    private final Cell[][] block;
    private Direction blockRotation = Direction.UP;
    private int blockRow;
    private int blockCol;

    private Direction prevBlockRotation = blockRotation;
    private int prevBlockRow;
    private int prevBlockCol;
    
    public BlockData(Cell[][] block) {
        this.block = block;

        @SuppressWarnings("UnnecessaryLocalVariable") int blockOffset = (Config.getGridWidth() / 2) - 2;

        this.blockCol = blockOffset;
    }

    private BlockData(Cell[][] block, int blockRow, int blockCol, Direction blockRotation) {
        this.block = block;
        this.blockRow = blockRow;
        this.blockCol = blockCol;
        this.blockRotation = blockRotation;
    }

    // =====================
    // Movement
    // =====================
    void moveDown() {
        prevBlockRow = blockRow;
        blockRow++;
    }

    void moveLeft() {
        prevBlockCol = blockCol;
        blockCol--;
    }

    void moveRight() {
        prevBlockCol = blockCol;
        blockCol++;
    }

    void revertRowPosition() {
        blockRow = prevBlockRow;
        prevBlockRow = 0;
    }

    void revertColPosition() {
        blockCol = prevBlockCol;
        prevBlockCol = 0;
    }

    // =====================
    // Rotation
    // =====================
    void rotateLeft() {
        Direction[] directions = Direction.values();

        int rotationIndex = (
            ((blockRotation.ordinal() - 1) % 4 + 4) % 4
        );

        prevBlockRotation = blockRotation;
        
        blockRotation = directions[rotationIndex];
    }

    void rotateRight() {
        Direction[] directions = Direction.values();

        int rotationIndex = (
            ((blockRotation.ordinal() + 1) % 4 + 4) % 4
        );

        prevBlockRotation = blockRotation;
        
        blockRotation = directions[rotationIndex];
    }

    void revertBlockRotation() {
        blockRotation = prevBlockRotation;
        prevBlockRotation = Direction.UP;
    }

    // =====================
    // Block Array Copying
    // =====================

    // rotateArrayRight → used by rotateBlockNTimes → used by getRotatedBlockCopy

    public Cell[][] getRotatedBlockCopy() {
        return rotateBlockNTimes(blockRotation.ordinal());
    }

    private Cell[][] rotateBlockNTimes(int amount) {
        Cell[][] newBlock = copy(block);

        for (int i = 0; i < amount; i++) {
            newBlock = rotateArrayRight(newBlock);
        }

        return newBlock;
    }

    private Cell[][] rotateArrayRight(Cell[][] block) {
        int blockSize = block.length;
        Cell[][] newBlock = new Cell[blockSize][blockSize];

        for (int row = 0; row < blockSize; row++) {
            for (int col = 0; col < blockSize; col++) {
                newBlock[row][blockSize - 1 - col] = 
                    block[col][row];
            }
        }

        return newBlock;
    }

    // =====================
    // Block Object Copying
    // =====================
    public BlockData copyBlockData() {
        return new BlockData(copy(this.block), this.blockRow, this.blockCol, this.blockRotation);
    }

    private Cell[][] copy(Cell[][] original) {
        int blockSize = original.length;
        Cell[][] copy = new Cell[blockSize][blockSize];

        for (int row = 0; row < blockSize; row++) {
            System.arraycopy(original[row], 0, copy[row], 0, blockSize);
        }

        return copy;
    }

    // =====================
    // Size / Position Info
    // =====================
    public int blockRow() {
        return blockRow;
    }

    public int blockCol() {
        return blockCol;
    }

    public int blockSize() {
        return block.length;
    }
}
