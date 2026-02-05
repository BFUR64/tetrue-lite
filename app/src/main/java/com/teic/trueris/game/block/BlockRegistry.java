package com.teic.trueris.game.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.teic.trueris.game.cell.Cell;
import com.teic.trueris.game.cell.CellRegistry;

public class BlockRegistry {
    private static final List<BlockTemplate> BLOCK_SET = new ArrayList<>();

    static {
        BLOCK_SET.add(new BlockTemplate(
            2, 
            List.of(
                CellRegistry.OCELL, CellRegistry.OCELL, 
                CellRegistry.OCELL, CellRegistry.OCELL
            )
        ));

        BLOCK_SET.add(new BlockTemplate(
            3, 
            List.of(
                CellRegistry.JCELL, CellRegistry.EMPTY, CellRegistry.EMPTY, 
                CellRegistry.JCELL, CellRegistry.JCELL, CellRegistry.JCELL, 
                CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY
            )
        ));

        BLOCK_SET.add(new BlockTemplate(
            3, 
            List.of(
                CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.LCELL, 
                CellRegistry.LCELL, CellRegistry.LCELL, CellRegistry.LCELL, 
                CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY
            )
        ));

        BLOCK_SET.add(new BlockTemplate(
            3, 
            List.of(
                CellRegistry.EMPTY, CellRegistry.SCELL, CellRegistry.SCELL, 
                CellRegistry.SCELL, CellRegistry.SCELL, CellRegistry.EMPTY, 
                CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY
            )
        ));

        BLOCK_SET.add(new BlockTemplate(
            3, 
            List.of(
                CellRegistry.ZCELL, CellRegistry.ZCELL, CellRegistry.EMPTY, 
                CellRegistry.EMPTY, CellRegistry.ZCELL, CellRegistry.ZCELL, 
                CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY
            )
        ));

        BLOCK_SET.add(new BlockTemplate(
            3, 
            List.of(
                CellRegistry.EMPTY, CellRegistry.TCELL, CellRegistry.EMPTY, 
                CellRegistry.TCELL, CellRegistry.TCELL, CellRegistry.TCELL, 
                CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY
            )
        ));

        BLOCK_SET.add(new BlockTemplate(
            4, 
            List.of(
                CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY, 
                CellRegistry.ICELL, CellRegistry.ICELL, CellRegistry.ICELL, CellRegistry.ICELL, 
                CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY, 
                CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY, CellRegistry.EMPTY
            )
        ));
    }

    public static List<BlockTemplate> values() {
        return Collections.unmodifiableList(BLOCK_SET);
    }

    public static int size() {
        return BLOCK_SET.size();
    }

    public static class BlockTemplate {
        private final int size;
        private final List<Cell> cells;

        protected BlockTemplate(int size, List<Cell> cells) {
            this.size = size;
            this.cells = cells;
        }
        
        public Cell[][] copyBlock() {
            Cell[][] blockCopy = new Cell[size][size];

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    int idx = row * size + col;
                    Cell cell = cells.get(idx);
                    blockCopy[row][col] = cell.isEmpty() ? CellRegistry.EMPTY : cell.copy();
                }
            }

            return blockCopy;
        }
    }
}
