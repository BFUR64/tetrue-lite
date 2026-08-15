package com.teic.trueris.game.utils;

import com.teic.trueris.Config;
import com.teic.trueris.game.block.BlockData;
import com.teic.trueris.game.block.Direction;
import com.teic.trueris.game.cell.Cell;

import java.util.Map;

public class RotationSystem {
    private final Collision collision;

    public RotationSystem(Collision collision) {
        this.collision = collision;
    }

    public RotationResult computeRotation(BlockData blockData, boolean rightDirection) {
        Direction currentRotation = blockData.rotation();

        boolean success = false;
        int rowOffset = 0;
        int colOffset = 0;

        int nextRotation;
        if (rightDirection) {
            nextRotation = ((currentRotation.ordinal() + 1) % 4 + 4) % 4;}
        else {
            nextRotation = ((currentRotation.ordinal() - 1) % 4 + 4) % 4;
        }

        Cell[][] cellBlock = blockData.getRawBlock();
        Map<String, int[][]> kickTable;

        if (!Config.noSRS.get()) {
            kickTable = cellBlock.length == 3 ? KickTable.KICKS_JLSZT : KickTable.KICKS_I;
        }
        else {
            kickTable = KickTable.KICKS_NONE;
        }

        int[][] offsets = kickTable.get(currentRotation.ordinal() + ">" + nextRotation);

        for (int[] offset : offsets) {
            int dx = offset[0];
            int dy = offset[1];

            int targetRow = blockData.getBlockRow() + dy;
            int targetCol = blockData.getBlockCol() + dx;

            if (collision.isPositionValid(cellBlock, nextRotation, targetRow, targetCol)) {
                rowOffset += dy;
                colOffset += dx;
                success = true;
                break;
            }
        }

        return new RotationResult(success, Direction.fromId(nextRotation), rowOffset, colOffset);
    }
}
